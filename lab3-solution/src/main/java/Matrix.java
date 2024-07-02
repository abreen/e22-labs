/**
 * A class representing an integer matrix
 */
public class Matrix {

    private int numRows;
    private int numCols;

    private int[][] values;

    /** Create an empty square matrix */
    public Matrix(int dimension) {
        numRows = numCols = dimension;
        values = new int[numRows][numCols];
    }

    /** Create a matrix from a 2-D array */
    public Matrix(int[][] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException();
        }

        int firstRowLength = values[0].length;
        for (int[] row : values) {
            if (row.length != firstRowLength) {
                throw new IllegalArgumentException("2-D array must be rectangular");
            }
        }

        this.values = values;
        numRows = values.length;
        numCols = values[0].length;
    }

    /** Create a matrix from a list of values */
    public Matrix(int rows, int cols, int... allValues) {
        if (allValues.length != rows * cols) {
            throw new IllegalArgumentException("wrong number of values");
        }

        values = new int[numRows = rows][numCols = cols];

        for (int i = 0; i < allValues.length; i++) {
            values[i / cols][i % cols] = allValues[i];
        }
    }

    /** Return a new matrix representing the matrix product */
    public Matrix multiply(Matrix other) {
        if (this.numCols != other.numRows) {
            throw new IllegalArgumentException();
        }

        int m = this.numRows;
        int n = this.numCols;
        int p = other.numCols;

        // this.values: m rows × n cols
        // other.values: n rows × p cols

        // result: m rows × p cols
        int[][] result = new int[m][p];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < p; j++) {
                int total = 0;

                for (int k = 0; k < n; k++) {
                    total += this.values[i][k] * other.values[k][j];
                }

                result[i][j] = total;
            }
        }

        return new Matrix(result);
    }

    /** Return a new matrix where each element is multiplied by x */
    public Matrix scale(int x) {
        int[][] copy = toArray();

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                copy[r][c] *= x;
            }
        }

        return new Matrix(copy);
    }

    /** Return a 2-D array containing the matrix's values (a copy) */
    public int[][] toArray() {
        int[][] copy = new int[numRows][numCols];

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                copy[r][c] = this.values[r][c];
            }
        }

        return copy;
    }
}
