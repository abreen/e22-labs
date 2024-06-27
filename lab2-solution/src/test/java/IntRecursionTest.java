import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.Assert.assertEquals;

public class IntRecursionTest {

    private static final int[] fibonacci = {
            0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144
    };

    @Test
    public void testFibNaive() {
        for (int n = 0; n < fibonacci.length; n++) {
            int answer = fibonacci[n];
            assertEquals(answer, IntRecursion.fib(n));
        }
    }

    @Test
    public void testFibIterative() {
        for (int n = 0; n < fibonacci.length; n++) {
            int answer = fibonacci[n];
            assertEquals(answer, IntRecursion.fibIterative(n));
        }
    }

    @Test
    public void testFibFaster() {
        for (int n = 0; n < fibonacci.length; n++) {
            int answer = fibonacci[n];
            assertEquals(answer, IntRecursion.fibFaster(n));
        }
    }

    @Test
    public void testSum() {
        assertEquals(2 + 1, IntRecursion.sum(2));
        assertEquals(3 + 2 + 1, IntRecursion.sum(3));
        assertEquals(5 + 4 + 3 + 2 + 1, IntRecursion.sum(5));
    }

    @Test
    public void testSumInvalidInput() {
        assertEquals(0, IntRecursion.sum(-100));
        assertEquals(0, IntRecursion.sum(0));
    }

    @Test
    public void testPrintSeriesUsingBuffer() {
        // this object hides a char[] array (basically)
        var buffer = new ByteArrayOutputStream();

        // this object offers the print() and println() methods
        PrintStream out = new PrintStream(buffer);

        IntRecursion.printSeries(out, 1, 5);

        // see the chars put into the buffer using print() & println()
        String output = buffer.toString();

        assertEquals("1, 2, 3, 4, 5\n", output);
    }

}
