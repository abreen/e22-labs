public class App {
    public static void main(String[] args) {
        try {
            new TutorTrace(ComboSum.class, "5", "1", "2", "3").traceToFile("ComboSum.trace.json");

        } catch (TutorTrace.ProgramCrashed e) {
            System.out.println("program crashed (exited non-zero)");
        } catch (TutorTrace.DebuggingFailure e) {
            System.err.println("failed to debug program: " + e.getCause().getMessage());
        }
    }
}
