import java.util.List;

/**
 * An application that generates traces for Quicksort and MergeSort
 */
public class App {
    public static void main(String[] args) {
        String[] input = { "7", "39", "20", "11", "16", "5" };

        List<Class<? extends Sort>> classes = List.of(Quicksort.class, MergeSort.class);

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
