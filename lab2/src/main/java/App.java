import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        try (Scanner console = new Scanner(System.in)) {
            System.out.print("What is the dimension of the board? ");
            int n = console.nextInt();

            NQueens solver = new NQueens(n);
            boolean foundSol = solver.findSolution();
            if (!foundSol) {
                System.out.println("Failed to find a solution!");
            }
        }
    }
}
