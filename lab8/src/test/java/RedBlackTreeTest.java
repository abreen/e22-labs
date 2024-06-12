import org.junit.Test;
import org.junit.Assert;

public class RedBlackTreeTest {

    @Test
    public void testEmptyTree() {
        var t = new RedBlackTree();
        Assert.assertEquals(-1, t.height());
    }

    @Test
    public void testLookup() {
        var t = new RedBlackTree();

        Assert.assertNull(t.lookup(1));

        t.insert(1, "one");
        t.insert(2, "two");
        t.insert(3, "three");
        t.insert(4, "four");
        t.insert(5, "five");

        Assert.assertEquals("five", t.lookup(5));
        Assert.assertEquals("one", t.lookup(1));
        Assert.assertEquals("two", t.lookup(2));
        Assert.assertNull(t.lookup(100));
    }

    /**
     * Verifies that the balanced tree's height is less than
     * or equal to 2 * log2(n + 1) for simple trees where
     * n is less than 50,000.
     */
    @Test
    public void testLogarithmicHeight() {
        var t = new RedBlackTree();
        Assert.assertEquals(-1, t.height());

        t.insert(101, null);
        Assert.assertEquals(0, t.height());

        for (int n = 2; n < 50000; n++) {
            t.insert(100 + n, null);

            // height expected to be less than or equal to: 2 * log2(n + 1)
            double expectedHeight = 2 * (Math.log(n + 1) / Math.log(2));

            Assert.assertTrue("tree height must be less than expected", t.height() <= expectedHeight);
        }
    }
}
