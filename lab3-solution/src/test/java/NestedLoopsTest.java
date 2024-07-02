import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.junit.Before;

public class NestedLoopsTest {

    @Before
    public void setup() {
        NestedLoops.counter = 0;
    }

    @Test
    public void testSingleLoop() {
        NestedLoops.forI(10);

        assertEquals(10, NestedLoops.counter);
    }

    @Test
    public void testNestedLoops() {
        NestedLoops.forIforJ(10);

        assertEquals(100, NestedLoops.counter);
    }

    @Test
    public void testNestedLoops2() {
        NestedLoops.forIforJforK(10);

        assertEquals(1000, NestedLoops.counter);
    }

    @Test
    public void testMystery1() {
        NestedLoops.mystery1(10);

        assertEquals(30, NestedLoops.counter);
    }

    @Test
    public void testMystery2() {
        NestedLoops.mystery2(10);

        assertEquals(45, NestedLoops.counter);
    }
}
