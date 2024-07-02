import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

public class MatrixTest {

    @Test
    public void testEmptyMatrix() {
        var m = new Matrix(3);

        int[][] expected = {
                { 0, 0, 0 },
                { 0, 0, 0 },
                { 0, 0, 0 }
        };

        assertArrayEquals(expected, m.toArray());
    }

    @Test
    public void testMatrixFromArray() {
        int[][] input = {
                { 1, 2, 3 },
                { 4, 5, 6 }
        };

        var m = new Matrix(input);
        assertArrayEquals(input, m.toArray());
    }

    @Test
    public void testMatrixConstructor() {
        var m = new Matrix(2, 3,
                1, 2, 3,
                4, 5, 6);

        int[][] expected = {
                { 1, 2, 3 },
                { 4, 5, 6 }
        };

        assertArrayEquals(expected, m.toArray());
    }

    @Test
    public void testSquareMatrixConstructor() {
        var m = new Matrix(3, 3,
                1, 2, 3,
                4, 5, 6,
                7, 8, 9);

        int[][] expected = {
                { 1, 2, 3 },
                { 4, 5, 6 },
                { 7, 8, 9 }
        };

        assertArrayEquals(expected, m.toArray());
    }

    @Test
    public void testConstructorInputChecking() {
        // reject an empty 2-D array
        assertThrows(IllegalArgumentException.class, () -> {
            new Matrix(new int[][] {});
        });

        // reject an array that is not rectangular
        assertThrows(IllegalArgumentException.class, () -> {
            new Matrix(new int[][] {
                    { 1, 2, 3 },
                    { 4, 5 },
                    { 6 }
            });
        });
    }

    @Test
    public void testMultiply() {
        var a = new Matrix(
                4, 3,
                1, 0, 1,
                2, 1, 1,
                0, 1, 1,
                1, 1, 2);
        var b = new Matrix(
                3, 3,
                1, 2, 1,
                2, 3, 1,
                4, 2, 2);

        var c = a.multiply(b);

        int[][] expected = {
                { 5, 4, 3 },
                { 8, 9, 5 },
                { 6, 5, 3 },
                { 11, 9, 6 }
        };

        assertArrayEquals(expected, c.toArray());
    }

    @Test
    public void testScale() {
        int[][] input = {
                { 1, 0 },
                { 0, 1 }
        };
        var m1 = new Matrix(input);

        var m2 = m1.scale(3);

        int[][] expected = {
                { 3, 0 },
                { 0, 3 }
        };

        assertArrayEquals(expected, m2.toArray());
        assertArrayEquals(input, m1.toArray());
    }

    @Test
    public void testToArrayMakesCopy() {
        var m = new Matrix(3, 3,
                1, 2, 3,
                4, 5, 6,
                7, 8, 9);

        int[][] values = m.toArray();

        m.scale(10);

        int[][] expected = {
                { 1, 2, 3 },
                { 4, 5, 6 },
                { 7, 8, 9 }
        };

        // scaling the matrix should not affect the "values" array
        assertArrayEquals(expected, values);
    }
}
