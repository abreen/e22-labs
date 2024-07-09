import java.util.List;

/**
 * An application that generates traces of sorting algorithms
 *
 * Each of the Sort subclasses implement their own main() methods.
 * This program runs each Sort program and saves a trace file based
 * on the class name (e.g., "BubbleSort.trace.json").
 */
public class App {
    public static void main(String[] args) {
        String[] input = { "7", "39", "20", "11", "16", "5" };

        List<Class<? extends Sort>> classes = List.of(
                BubbleSort.class,
                SelectionSort.class,
                InsertionSort.class,
                ShellSort.class);

        for (Class<?> type : classes) {
            try {
                var file = new TutorTrace(type, input).traceToFile();
                System.out.println("saved file: " + file.getName());

            } catch (TutorTrace.ProgramCrashed e) {
                System.out.println("program crashed (exited non-zero)");
            } catch (TutorTrace.DebuggingFailure e) {
                System.err.println("failed to debug program: " + e.getCause().getMessage());
            }
        }
    }
}
