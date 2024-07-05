import java.io.*;
import java.util.*;
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

    public TutorTrace(Class<?> target) {
        targetClass = target;
    }

    private BufferedReader getReaderForStdout(VirtualMachine vm) {
        var inputStream = vm.process().getInputStream();
        var inputStreamReader = new InputStreamReader(inputStream);
        return new BufferedReader(inputStreamReader);
    }

    public List<Step> trace(String... args) throws DebuggingFailure, ProgramCrashed {
        VirtualMachine vm = null;
        Thread outputWatcher = null;
        List<Step> steps = new LinkedList<>();

        try {
            vm = connectAndLaunchVirtualMachine(args, targetClass.getName());

            BufferedReader stdout = getReaderForStdout(vm);
            StringBuilder buffer = new StringBuilder();

            outputWatcher = startOutputWatcher(stdout, buffer);

            enableClassPrepareRequest(vm, targetClass.getName());

            EventSet eventSet = null;
            while ((eventSet = vm.eventQueue().remove()) != null) {
                for (Event event : eventSet) {
                    if (event instanceof ClassPrepareEvent e) {
                        handleClassPrepareEvent(vm, e);

                    } else if (event instanceof BreakpointEvent e) {
                        startStepping(vm, e.thread());

                    } else if (event instanceof StepEvent e) {
                        String className = e.location().declaringType().name();

                        if (className.equals(targetClass.getName())) {
                            List<StackFrame> stackFrames = e.thread().frames();
                            String currentOutput = buffer.toString();
                            steps.addLast(new Step(toFrameRecords(stackFrames), currentOutput));
                        }
                    }

                    // must call resume() after getting any event
                    vm.resume();
                }
            }

        } catch (VMDisconnectedException ignored) {
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
            return null;
        }

        return steps;
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

    public record Step(Frame[] stack, String output) {
    }

    public record Frame(String className, String methodName, int lineNumber, Variable[] visible) {
    }

    /** A variable with a name and a string representation */
    public record Variable(String name, String repr) {

        public Variable(String name, Object value) {
            this(name, Json.repr(value));
        }
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

    private Frame[] toFrameRecords(List<StackFrame> frames) throws AbsentInformationException {
        Frame[] frameRecords = new Frame[frames.size()];
        StackFrame frameForMain = frames.getLast();

        for (int i = 0; i < frames.size(); i++) {
            StackFrame frame = frames.get(i);

            Variable[] locals = toVariableRecords(frame, frame.equals(frameForMain));

            String className = frame.location().sourceName();
            String methodName = frame.location().method().name();
            int lineNumber = frame.location().lineNumber();

            frameRecords[i] = new Frame(className, methodName, lineNumber, locals);
        }

        return frameRecords;
    }

    private Variable[] toVariableRecords(StackFrame frame, boolean isMain) throws AbsentInformationException {
        List<LocalVariable> locals = frame.visibleVariables();
        Variable[] variables = new Variable[locals.size()];
        int i = 0;

        for (LocalVariable local : locals) {
            if (isMain && local.isArgument()) {
                continue;
            }

            String variableName = local.name();
            Object variableValue = valueToObject(frame.getValue(local));

            variables[i++] = new Variable(variableName, variableValue);
        }

        Variable[] temp = new Variable[i];
        System.arraycopy(variables, 0, temp, 0, i);
        return temp;
    }

    private Object valueToObject(Value value) {
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
                } catch (ClassNotLoadedException e) {
                    System.out.println("class not loaded");
                }
            }

            if (arrayType == null) {
                arrayType = Object.class;
            }

            List<Value> values = x.getValues();
            Object arr = Array.newInstance(arrayType, values.size());
            for (int i = 0; i < values.size(); i++) {
                Array.set(arr, i, valueToObject(values.get(i)));
            }
            return arr;

        } else if (value instanceof ObjectReference x) {
            List<Field> fields = x.referenceType().fields();
            Map<Field, Value> map = x.getValues(fields);
            for (Map.Entry<Field, Value> entry : map.entrySet()) {
                // TODO
                //
            }
        }

        return null;
    }
}
