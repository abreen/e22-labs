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

    /** Allows you to create a fake object of any class (so it appears in JSON as something else) */
    public record Fake(String kind, String className, Map<String, Object> fields, String fakeToString) {
        public Fake(String className, Map<String, Object> fields) {
            this("class", className, fields, className + "" + fields);
        }
    }

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
                        reprs[i] = reprFallback(element);
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
        } else if (value instanceof java.util.List list) {
            int length = list.size();
            String[] reprs = new String[length];

            for (int i = 0; i < length; i++) {
                Object element = list.get(i);
                if (seen.contains(element)) {
                    reprs[i] = reprFallback(element);
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
        } else if (value instanceof java.util.Map<?, ?> map) {
            String[] reprs = new String[map.size()];

            int i = 0;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                Object mapKey = entry.getKey();
                Object mapValue = entry.getValue();

                if (seen.contains(mapValue)) {
                    reprs[i] = reprFallback(mapKey.toString()) + ": " + reprFallback(mapValue);
                } else {
                    if (mapValue != null && !(mapValue instanceof String)) {
                        seen.add(mapValue);
                    }

                    reprs[i] = "\"" + mapKey.toString() + "\": " + repr(mapValue, seen);
                    if (mapValue != null && !(mapValue instanceof String)) {
                        seen.remove(mapValue);
                    }
                }
            }

            return "{" + Arrays.stream(reprs).collect(Collectors.joining(", ")) + "}";

        } else {
            // mark this value as seen (avoids infinite recursion)
            seen.add(value);

            String kind = "class";
            String className = c.getName();

            Map<String, String> fields;

            if (value instanceof Fake fake) {
                className = fake.className();
                fields = fake.fields().entrySet().stream()
                        .collect(Collectors.toMap(
                                entry -> entry.getKey(),
                                entry -> {
                                    Object fieldValue = entry.getValue();
                                    if (seen.contains(fieldValue)) {
                                        return "\"" + fieldValue.toString() + "\"";
                                    } else {
                                        seen.add(fieldValue);
                                        String repr = repr(fieldValue, seen);
                                        seen.remove(fieldValue);
                                        return repr;
                                    }
                                }));
            } else {
                fields = Arrays.stream(c.getDeclaredFields())
                        .filter(f -> !Modifier.isPrivate(f.getModifiers()))
                        .collect(Collectors.toMap(Field::getName, (f) -> {
                            try {
                                Object fieldValue = f.get(value);
                                if (seen.contains(fieldValue)) {
                                    return reprFallback(fieldValue);
                                } else {
                                    seen.add(fieldValue);
                                    String repr = repr(fieldValue, seen);
                                    seen.remove(fieldValue);
                                    return repr;
                                }
                            } catch (IllegalAccessException e) {
                                return "\"<unknown>\"";
                            }
                        }));
            }

            // convert an arbitrary Object (not an array) to JSON

            // if the Object is a record, try to call accessor & pass to repr()
            RecordComponent[] components = c.getRecordComponents();
            if (!(value instanceof Fake) && components != null) {
                kind = "record";
                fields.putAll(Arrays.stream(components).collect(Collectors.toMap(
                        (r) -> r.getName(),
                        (r) -> {
                            try {
                                Object fieldValue = r.getAccessor().invoke(value);
                                if (seen.contains(fieldValue)) {
                                    return reprFallback(fieldValue);
                                } else {
                                    seen.add(fieldValue);
                                    String repr = repr(fieldValue, seen);
                                    seen.remove(fieldValue);
                                    return repr;
                                }
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                return "\"<unknown>\"";
                            }
                        })));
            }

            String fieldsRepr = fields.entrySet().stream()
                    .map((Map.Entry<String, String> e) -> repr(e.getKey()) + ": " + e.getValue())
                    .collect(Collectors.joining(", "));

            return "{\":java:" + kind + "\": " + repr(className) + ", " + fieldsRepr + "}";
        }
    }

    /** Used when avoiding cycles in the object graph by not calling repr() on the same object twice */
    private static String reprFallback(Object value) {
        if (value instanceof Fake f) {
            return repr(f.fakeToString());
        }
        // don't call repr() again, just use the toString()
        return repr(value.toString());
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
