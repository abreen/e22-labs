import java.util.*;

/**
 * NQueens - blueprint class for an object that solves the n-queens puzzle
 * using recursive backtracking. Includes a main() method for using the solver.
 */
public class NQueens {
    private boolean[][] board; // 2-D array that keeps track of which
                               // squares on the board are occupied
    private boolean[] colEmpty; // keeps track of empty columns
    private boolean[] upDiagEmpty; // keeps track of empty up-diagonals
    private boolean[] downDiagEmpty; // keeps track of empty down-diagonals

    /**
     * Constructor - initializes the state of a chess board
     * with the specified boardSize
     */
    public NQueens(int n) {
        this.board = new boolean[n][n]; // initially all false
        this.colEmpty = new boolean[n];
        this.upDiagEmpty = new boolean[2 * n - 1];
        this.downDiagEmpty = new boolean[2 * n - 1];

        /*
         * set the entries in the the extra bookkeeping arrays to true,
         * since initially everything is empty
         */
        for (int i = 0; i < 2 * n - 1; i++) {
            if (i < n) {
                this.colEmpty[i] = true;
            }
            this.upDiagEmpty[i] = true;
            this.downDiagEmpty[i] = true;
        }
    }

    /*
     * placeQueen - place a queen at the specified row and column;
     * a private helper method, so we don't need to perform error-checking!
     */
    private void placeQueen(int row, int col) {
        this.board[row][col] = true;
        this.colEmpty[col] = false;
        this.upDiagEmpty[row + col] = false;
        this.downDiagEmpty[(this.board.length - 1) + row - col] = false;
    }

    /*
     * removeQueen - remove the queen at the specified row and column;
     * a private helper method, so we don't need to perform error-checking!
     */
    private void removeQueen(int row, int col) {
        this.board[row][col] = false;
        this.colEmpty[col] = true;
        this.upDiagEmpty[row + col] = true;
        this.downDiagEmpty[(this.board.length - 1) + row - col] = true;
    }

    /*
     * isSafe - returns true if it is safe to place a queen at the
     * specified square, and false otherwise;
     * a private helper method, so we don't need to perform error-checking!
     */
    private boolean isSafe(int row, int col) {
        return (this.colEmpty[col] &&
                this.upDiagEmpty[row + col] &&
                this.downDiagEmpty[(this.board.length - 1) + row - col]);
    }

    /*
     * findSolution(int row) - recursive function to search for solutions.
     * Tries to find a safe column in the specified row.
     * If it finds one, it makes a recursive call to handle the next row.
     * If it can't find one, it backtracks by returning from the
     * recursive call.
     * a private helper method, so we don't need to perform error-checking!
     */
    private boolean findSolution(int row) {
        if (row == this.board.length) { // base case: a solution!
            this.displayBoard();
            return true;
        }

        // Try each column in the current row.
        for (int col = 0; col < this.board.length; col++) {
            if (this.isSafe(row, col)) {
                this.placeQueen(row, col);

                // Move onto the next row by making a recursive call.
                // If the call returns true, we've already found the solution,
                // so we just keep returning true. Otherwise, we pick up
                // where we left off in the current row.
                if (this.findSolution(row + 1)) {
                    return true;
                }

                // If we get here, we've backtracked.
                this.removeQueen(row, col);
            }
        }

        return false; // tried all columns in this row, so backtrack!
    }

    /*
     * findSolution() - public "wrapper" method that takes no parameters.
     * Makes the initial call to the private recursive-backtracking method,
     * and returns whatever it returns.
     * 
     * This is an example of method *overloading* -- since we have two methods
     * with the same name but different parameter lists.
     */
    public boolean findSolution() {
        boolean foundSol = this.findSolution(0);
        return foundSol;
    }

    /**
     * displayBoard - display the current state of the board
     */
    public void displayBoard() {
        // print column indices
        System.out.print("  ");
        for (int c = 0; c < this.board.length; c++) {
            System.out.print(" " + (c % 10));
        }
        System.out.println();

        // print the contents of the board
        for (int r = 0; r < this.board.length; r++) {
            System.out.print(" " + (r % 10)); // row index
            for (int c = 0; c < this.board.length; c++) {
                if (this.board[r][c]) {
                    System.out.print(" Q");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }
}
