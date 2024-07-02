import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.junit.Before;

public class NestedLoopsTest {

    private static int N = 10;

    @Before
    public void setup() {
        NestedLoops.counter = 0;
    }

    @Test
    public void testSingleLoop() {
        NestedLoops.forI(N);

        // n = O(n)
        assertEquals(999, NestedLoops.counter);
    }

    @Test
    public void testNestedLoops() {
        NestedLoops.forIforJ(N);

        // n * n = O(n * n) = O(n^2)
        assertEquals(N * N, NestedLoops.counter);
    }

    @Test
    public void testNestedLoops2() {
        NestedLoops.forIforJforK(N);

        // n * n * n = O(n^3)
        assertEquals(N * N * N, NestedLoops.counter);
    }

    @Test
    public void testMystery1() {
        NestedLoops.mystery1(N);

        // 3 * n = O(3n)
        assertEquals(3 * N, NestedLoops.counter);
    }

    @Test
    public void testMystery2() {
        NestedLoops.mystery2(N);

        // 0 + 1 + 2 + 3 + ... + n - 1
        // n * (n - 1) / 2
        // n^2/2 - n/2
        // O(n^2)
        int expected = N * (N - 1) / 2;
        assertEquals(expected, NestedLoops.counter);
    }
}
