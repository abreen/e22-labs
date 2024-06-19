public class App {
    // before running this, set a breakpoint in NQueens.findSolution()
    // don't forget to turn on Debug Mode in your editor
    public static void main(String[] args) {
        NQueens solver = new NQueens(5);

        if (!solver.findSolution()) {
            System.out.println("no solution");
        }
    }
}
