import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

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
                ShellSort.class,
                Quicksort.class,
                MergeSort.class);

        for (Class<?> type : classes) {
            try {
                var trace = new TutorTrace(type).trace(input);
                var traceString = Json.repr(trace);
                saveTraceToFile(type, traceString);

            } catch (TutorTrace.ProgramCrashed e) {
                System.out.println("program crashed (exited non-zero)");
            } catch (TutorTrace.DebuggingFailure e) {
                System.err.println("failed to debug program: " + e.getCause().getMessage());
            } catch (IOException e) {
                System.err.println("failed to save trace file: " + e.getMessage());
            }
        }
    }

    private static void saveTraceToFile(Class<?> type, String content) throws IOException {
        String className = type.getSimpleName();
        Path outputFile = Path.of(className + ".trace.json");
        Files.writeString(outputFile, content);
        System.out.println("saved trace to " + outputFile);
    }
}
