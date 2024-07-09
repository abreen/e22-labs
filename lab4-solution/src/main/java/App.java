/**
 * An application that generates a trace of StringNode
 */
public class App {
    public static void main(String[] args) {
        try {
            String[] input = {"hello"};
            var file = new TutorTrace(StringNode.class, input).traceToFile();
            System.out.println("saved file: " + file.getName());
        } catch (TutorTrace.DebuggingFailure e) {
            System.err.println("failed to debug program: " + e.getCause().getMessage());
        } catch (TutorTrace.ProgramCrashed e) {
            System.out.println("program crashed");
        }
    }
}
