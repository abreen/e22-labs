import java.util.*;
import java.util.stream.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Array;
import java.text.DecimalFormat;

/**
 * A tiny implementation of a JSON serializer
 */
public class Json {
    public static String repr(Object value) {
        Set<Object> seen = new HashSet<>();
        return repr(value, seen);
    }

    public static String repr(Object value, Set<Object> seen) {
        if (value == null) {
            return "null";
        }

        Class<?> c = value.getClass();
        if (c.isArray()) {
            if (value instanceof byte[]) {
                return repr((byte[]) value);
            } else if (value instanceof short[]) {
                return repr((short[]) value);
            } else if (value instanceof int[]) {
                return repr((int[]) value);
            } else if (value instanceof long[]) {
                return repr((long[]) value);
            } else if (value instanceof float[]) {
                return repr((float[]) value);
            } else if (value instanceof double[]) {
                return repr((double[]) value);
            } else if (value instanceof boolean[]) {
                return repr((boolean[]) value);
            } else if (value instanceof char[]) {
                return repr((char[]) value);
            } else {
                int length = Array.getLength(value);
                String[] reprs = new String[length];

                for (int i = 0; i < length; i++) {
                    Object element = Array.get(value, i);

                    if (seen.contains(element)) {
                        // don't call repr() again, just use the toString()
                        reprs[i] = "\"" + element.toString() + "\"";
                    } else {
                        if (element != null && !(element instanceof String)) {
                            seen.add(element);
                        }

                        reprs[i] = repr(element, seen);
                        if (element != null && !(element instanceof String)) {
                            seen.remove(element);
                        }
                    }
                }

                return "[" + Arrays.stream(reprs).collect(Collectors.joining(", ")) + "]";
            }
        } else if (c.equals(Byte.class)) {
            Byte x = (Byte) value;
            return repr(x.byteValue());
        } else if (c.equals(Short.class)) {
            Short x = (Short) value;
            return repr(x.shortValue());
        } else if (c.equals(Integer.class)) {
            Integer x = (Integer) value;
            return repr(x.intValue());
        } else if (c.equals(Long.class)) {
            Long x = (Long) value;
            return repr(x.longValue());
        } else if (c.equals(Float.class)) {
            Float x = (Float) value;
            return repr(x.floatValue());
        } else if (c.equals(Double.class)) {
            Double x = (Double) value;
            return repr(x.doubleValue());
        } else if (c.equals(Boolean.class)) {
            Boolean x = (Boolean) value;
            return repr(x.booleanValue());
        } else if (c.equals(Character.class)) {
            Character x = (Character) value;
            return repr(x.charValue());
        } else if (c.equals(String.class)) {
            return repr((String) value);
        } else if (value instanceof java.util.List) {
            List vals = (List) value;

            int length = vals.size();
            String[] reprs = new String[length];

            for (int i = 0; i < length; i++) {
                Object element = vals.get(i);
                if (seen.contains(element)) {
                    // don't call repr() again, just use the toString()
                    reprs[i] = "\"" + element.toString() + "\"";
                } else {
                    if (element != null && !(element instanceof String)) {
                        seen.add(element);
                    }

                    reprs[i] = repr(element, seen);
                    if (element != null && !(element instanceof String)) {
                        seen.remove(element);
                    }
                }
            }

            return "[" + Arrays.stream(reprs).collect(Collectors.joining(", ")) + "]";
        } else {
            // mark this value as seen (avoids infinite recursion)
            seen.add(value);

            // convert an arbitrary Object (not an array) to JSON
            Map<String, String> fields = Arrays.stream(c.getDeclaredFields())
                    .filter(f -> !Modifier.isPrivate(f.getModifiers()))
                    .collect(Collectors.toMap(Field::getName, (f) -> {
                        try {
                            Object fieldValue = f.get(value);
                            if (seen.contains(fieldValue)) {
                                // don't call repr() again, just use the toString()
                                return "\"" + fieldValue.toString() + "\"";
                            }
                            return repr(fieldValue, seen);
                        } catch (IllegalAccessException e) {
                            return "\"<unknown>\"";
                        }
                    }));

            // if the Object is a record, try to call accessor & pass to repr()
            RecordComponent[] components = c.getRecordComponents();
            if (components != null) {
                fields.putAll(Arrays.stream(components).collect(Collectors.toMap(
                        (r) -> r.getName(),
                        (r) -> {
                            try {
                                Object fieldValue = r.getAccessor().invoke(value);
                                if (seen.contains(fieldValue)) {
                                    // don't call repr() again, just use the toString()
                                    return "\"" + fieldValue.toString() + "\"";
                                }
                                return repr(fieldValue, seen);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                return "\"<unknown>\"";
                            }
                        })));
            }
            String fieldsRepr = "{" + fields.entrySet().stream()
                    .map((Map.Entry<String, String> e) -> "\"" + e.getKey() + "\": " + e.getValue())
                    .collect(Collectors.joining(", ")) + "}";
            String classNameRepr = repr(c.getName());
            return "{\"typeName\": " + classNameRepr + ", \"fields\": " + fieldsRepr + "}";
        }
    }

    private static String decimal(double value) {
        DecimalFormat fmt = new DecimalFormat("#.##########");
        fmt.setMinimumFractionDigits(0);
        return fmt.format(value);
    }

    public static String repr(byte value) {
        return "" + value;
    }

    public static String repr(short value) {
        return "" + value;
    }

    public static String repr(int value) {
        return "" + value;
    }

    public static String repr(long value) {
        return "" + value;
    }

    public static String repr(float value) {
        if (value == 0 || Math.abs(value) >= 1 && (int) value == value) {
            return repr((int) value);
        }
        return decimal(value);
    }

    public static String repr(double value) {
        if (value == 0 || Math.abs(value) >= 1 && (int) value == value) {
            return repr((int) value);
        }
        return decimal(value);
    }

    public static String repr(boolean value) {
        return value ? "true" : "false";
    }

    public static String repr(char value) {
        // JSON has no character type, just use a string
        return "\"" + escape("" + value) + "\"";
    }

    public static String repr(String value) {
        return "\"" + escape(value) + "\"";
    }

    private static String escape(String value) {
        return value
                .replaceAll("\\\\", "\\\\\\\\") // replace \ with \\
                .replaceAll("\"", "\\\\\"") // replace " with \"
                .replaceAll("\\r?\\n", "\\\\n"); // replace newlines with \n
    }

    public static String repr(byte[] arr) {
        String[] reprs = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            reprs[i] = repr(arr[i]);
        }
        return "[" + Arrays.stream(reprs).collect(Collectors.joining(", ")) + "]";
    }

    public static String repr(short[] arr) {
        String[] reprs = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            reprs[i] = repr(arr[i]);
        }
        return "[" + Arrays.stream(reprs).collect(Collectors.joining(", ")) + "]";
    }

    public static String repr(int[] arr) {
        Stream<String> reprs = Arrays.stream(arr).mapToObj(Json::repr);
        return "[" + reprs.collect(Collectors.joining(", ")) + "]";
    }

    public static String repr(long[] arr) {
        Stream<String> reprs = Arrays.stream(arr).mapToObj(Json::repr);
        return "[" + reprs.collect(Collectors.joining(", ")) + "]";
    }

    public static String repr(float[] arr) {
        String[] reprs = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            reprs[i] = repr(arr[i]);
        }
        return "[" + Arrays.stream(reprs).collect(Collectors.joining(", ")) + "]";
    }

    public static String repr(double[] arr) {
        Stream<String> reprs = Arrays.stream(arr).mapToObj(Json::repr);
        return "[" + reprs.collect(Collectors.joining(", ")) + "]";
    }

    public static String repr(boolean[] arr) {
        String[] reprs = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            reprs[i] = repr(arr[i]);
        }
        return "[" + Arrays.stream(reprs).collect(Collectors.joining(", ")) + "]";
    }

    public static String repr(char[] arr) {
        String[] reprs = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            reprs[i] = repr(arr[i]);
        }
        return "[" + Arrays.stream(reprs).collect(Collectors.joining(", ")) + "]";
    }
}
