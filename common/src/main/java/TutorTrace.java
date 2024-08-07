import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.lang.reflect.Array;
import com.sun.jdi.*;
import com.sun.jdi.connect.*;
import com.sun.jdi.request.*;
import com.sun.jdi.event.*;
import static com.sun.jdi.request.StepRequest.STEP_LINE;
import static com.sun.jdi.request.StepRequest.STEP_INTO;

public class TutorTrace {
    /** The class whose main() method we will trace */
    private Class<?> targetClass;

    /** The command-line arguments to the program */
    private String[] args;

    public TutorTrace(Class<?> target, String... args) {
        targetClass = target;
        this.args = args;
    }

    public File traceToFile() throws DebuggingFailure, ProgramCrashed {
        return traceToFile(targetClass.getName() + ".trace");
    }

    public File traceToFile(String fileNamePrefix) throws DebuggingFailure, ProgramCrashed {
        VirtualMachine vm = null;
        Thread outputWatcher = null;
        StringBuilder programOutput = new StringBuilder();

        long stepCount = 0;
        ZipOutputStream zipOut = null;
        File zipFile = null;
        PrintWriter writer = null;

        try {
            zipFile = new File(fileNamePrefix + ".zip");
            zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
            writer = new PrintWriter(zipOut);

            vm = connectAndLaunchVirtualMachine(args, targetClass.getName());

            var outputReader = getProgramOutputReader(vm);
            outputWatcher = startOutputWatcher(outputReader, programOutput);

            enableClassPrepareRequest(vm, targetClass.getName());

            EventSet eventSet = null;
            while ((eventSet = vm.eventQueue().remove()) != null) {
                for (Event event : eventSet) {
                    if (event instanceof ClassPrepareEvent e) {
                        handleClassPrepareEvent(vm, e);

                    } else if (event instanceof BreakpointEvent e) {
                        startStepping(vm, e.thread());

                    } else if (event instanceof StepEvent e) {
                        if (shouldSaveStepToFile(e)) {
                            stepCount++;

                            List<StackFrame> stackFrames = e.thread().frames();
                            String currentOutput = programOutput.toString();

                            Frame[] frames = toFrameRecords(stackFrames);
                            Step step = new Step(frames, currentOutput);

                            String repr = Json.repr(step);

                            zipOut.putNextEntry(new ZipEntry(fileNamePrefix + "." + stepCount + ".json"));
                            writer.append(repr + System.lineSeparator());
                            writer.flush();
                            zipOut.closeEntry();
                        }
                    }

                    // must call resume() after getting any event
                    vm.resume();
                }
            }

        } catch (VMDisconnectedException ignored) {
            try {
                writer.close();
                zipOut.close();
            } catch (IOException e) {
                throw new DebuggingFailure(e);
            }

        } catch (IOException | InterruptedException | IllegalConnectorArgumentsException | VMStartException
                | IncompatibleThreadStateException | AbsentInformationException e) {
            throw new DebuggingFailure(e);
        }

        try {
            boolean crashed = false;
            if (vm.process().waitFor() != 0) {
                crashed = true;
            }
            outputWatcher.join();

            if (crashed) {
                throw new ProgramCrashed();
            }
        } catch (InterruptedException ignored) {
        }

        return zipFile;
    }

    private boolean shouldSaveStepToFile(StepEvent event) {
        String className = event.location().declaringType().name();
        return className.equals(targetClass.getName()) && !event.location().method().isConstructor();
    }

    private BufferedReader getProgramOutputReader(VirtualMachine vm) {
        var inputStream = vm.process().getInputStream();
        var inputStreamReader = new InputStreamReader(inputStream);
        return new BufferedReader(inputStreamReader);
    }

    /** Launch another JVM, inheriting the classpath, and tell it to run the specified class */
    private static VirtualMachine connectAndLaunchVirtualMachine(String[] args, String className)
            throws IOException, IllegalConnectorArgumentsException, VMStartException {
        LaunchingConnector launchingConnector = Bootstrap.virtualMachineManager()
                .defaultConnector();

        Map<String, Connector.Argument> arguments = launchingConnector.defaultArguments();

        String mainClassAndArgs = className + " " + String.join(" ", args);
        arguments.get("main").setValue(mainClassAndArgs);
        arguments.get("options").setValue("-classpath " + System.getProperty("java.class.path"));
        return launchingConnector.launch(arguments);
    }

    /** Ask to receive an event every time execution reaches another line */
    private void startStepping(VirtualMachine vm, ThreadReference thread) {
        StepRequest request = vm.eventRequestManager().createStepRequest(thread, STEP_LINE, STEP_INTO);
        request.enable();
    }

    /** In another thread, read output from the target JVM and save it into a buffer */
    private Thread startOutputWatcher(BufferedReader input, StringBuilder output) {
        Thread t = new Thread(() -> {
            String line;
            try {
                while ((line = input.readLine()) != null) {
                    output.append(line);
                    output.append(System.lineSeparator());
                }
            } catch (IOException ignored) {
            }
        });
        t.start();
        return t;
    }

    /** Ask to receive an event when classes are loaded into the virtual machine */
    private static void enableClassPrepareRequest(VirtualMachine vm, String className) {
        ClassPrepareRequest classPrepareRequest = vm.eventRequestManager().createClassPrepareRequest();
        classPrepareRequest.addClassFilter(className);
        classPrepareRequest.enable();
    }

    /** Now that the target class is loaded, set a breakpoint on the first line of main() */
    private void handleClassPrepareEvent(VirtualMachine vm, ClassPrepareEvent event) throws AbsentInformationException {
        ClassType classType = (ClassType) event.referenceType();

        List<Method> mainMethods = classType.methodsByName("main", "([Ljava/lang/String;)V");
        if (mainMethods.isEmpty()) {
            throw new IllegalArgumentException("Class " + classType.name() + " has no main(String[]) method");
        }

        // request a breakpoint on the first line of the main() method
        List<Location> lines = mainMethods.get(0).allLineLocations();
        Location firstLine = lines.get(0);
        BreakpointRequest request = vm.eventRequestManager().createBreakpointRequest(firstLine);
        request.enable();
    }

    /** Converts a Java Debug Interface stack to an array of Frame records */
    private Frame[] toFrameRecords(List<StackFrame> frames) throws AbsentInformationException {
        StackFrame frameForMain = frames.getLast();
        Frame[] frameRecords = new Frame[frames.size()];

        for (int i = 0; i < frames.size(); i++) {
            StackFrame frame = frames.get(i);
            Variable[] locals = toVariableRecords(frame, frame.equals(frameForMain));

            String className = frame.location().declaringType().name();
            String methodName = frame.location().method().name();

            List<Location> methodLocations = frame.location().method().allLineLocations();
            int methodBeginLine = methodLocations.getFirst().lineNumber();
            int methodEndLine = methodLocations.getLast().lineNumber();
            String beginEnd = methodBeginLine + ":" + methodEndLine;

            int lineNumber = frame.location().lineNumber();

            frameRecords[i] = new Frame(className, methodName + ":" + lineNumber + "&" + beginEnd, locals);
        }

        return frameRecords;
    }

    /** Converts a Java Debug Interface stack frame to an array of Variable records */
    private Variable[] toVariableRecords(StackFrame frame, boolean isMain) throws AbsentInformationException {
        List<LocalVariable> jdiLocals = frame.visibleVariables();
        List<Variable> variables = new ArrayList<>(jdiLocals.size());

        for (LocalVariable local : jdiLocals) {
            if (isMain && local.isArgument()) {
                continue;
            }

            String variableName = local.name();
            Object variableValue = jdiValueToJavaObject(frame.getValue(local));
            String variableType = local.typeName();

            variables.add(new Variable(variableName, variableValue, variableType));
        }

        return variables.toArray(Variable[]::new);
    }

    private Object jdiValueToJavaObject(Value value) {
        if (value == null) {
            return null;
        }

        if (value instanceof ByteValue x) {
            return x.byteValue();
        } else if (value instanceof ShortValue x) {
            return x.shortValue();
        } else if (value instanceof IntegerValue x) {
            return x.intValue();
        } else if (value instanceof LongValue x) {
            return x.longValue();
        } else if (value instanceof FloatValue x) {
            return x.floatValue();
        } else if (value instanceof DoubleValue x) {
            return x.doubleValue();
        } else if (value instanceof BooleanValue x) {
            return x.booleanValue();
        } else if (value instanceof CharValue x) {
            return x.charValue();
        } else if (value instanceof StringReference x) {
            return x.value();
        } else if (value instanceof ArrayReference x) {
            Class<?> arrayType = null;
            if (x.type() instanceof ArrayType at) {
                try {
                    Type componentType = at.componentType();
                    if (componentType instanceof ByteValue) {
                        arrayType = byte.class;
                    } else if (componentType instanceof ShortValue) {
                        arrayType = short.class;
                    } else if (componentType instanceof IntegerValue) {
                        arrayType = int.class;
                    } else if (componentType instanceof LongValue) {
                        arrayType = long.class;
                    } else if (componentType instanceof FloatValue) {
                        arrayType = float.class;
                    } else if (componentType instanceof DoubleValue) {
                        arrayType = double.class;
                    } else if (componentType instanceof BooleanValue) {
                        arrayType = boolean.class;
                    } else if (componentType instanceof CharValue) {
                        arrayType = char.class;
                    }
                } catch (ClassNotLoadedException ignored) {
                }
            }

            if (arrayType == null) {
                arrayType = Object.class;
            }

            List<Value> values = x.getValues();
            Object arr = Array.newInstance(arrayType, values.size());
            for (int i = 0; i < values.size(); i++) {
                Array.set(arr, i, jdiValueToJavaObject(values.get(i)));
            }
            return arr;

        } else if (value instanceof ObjectReference x) {
            String className = x.referenceType().name();
            Map<Field, Value> jdiFields = x.getValues(x.referenceType().fields());
            Map<String, Object> fields = new HashMap<>(jdiFields.size());

            for (Map.Entry<Field, Value> entry : jdiFields.entrySet()) {
                String fieldName = entry.getKey().name();
                Object fieldValue = jdiValueToJavaObject(entry.getValue());

                fields.put(fieldName, fieldValue);
            }

            return new Json.Fake(className, fields, x.toString());
        }

        return null;
    }

    public record Step(Frame[] stack, String output) {
    }

    public record Frame(String className, String methodName, Variable[] visible) {
        public Frame(String className, String methodName) {
            this(className, methodName, null);
        }
    }

    public record Variable(String name, Object value, String type) {
    }

    /** An exception thrown when something went wrong trying to debug the target program */
    public class DebuggingFailure extends Exception {
        public DebuggingFailure(Throwable cause) {
            super(cause);
        }
    }

    /** An exception thrown when the target program crashed while trying to debug it */
    public class ProgramCrashed extends Exception {
    }
}
