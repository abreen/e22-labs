import org.junit.Test;
import org.junit.Assert;

public class RedBlackTreeTest {

    @Test
    public void testEmptyTree() {
        var t = new RedBlackTree();
        Assert.assertEquals(-1, t.height());
    }

    @Test
    public void testSimpleLookup() {
        var t = new RedBlackTree();

        Assert.assertNull(t.search(1));

        t.insert(1, "one");
        t.insert(2, "two");
        t.insert(3, "three");
        t.insert(4, "four");
        t.insert(5, "five");

        Assert.assertEquals("one", t.search(1));
        Assert.assertEquals("two", t.search(2));
        Assert.assertEquals("three", t.search(3));
        Assert.assertEquals("four", t.search(4));
        Assert.assertEquals("five", t.search(5));

        Assert.assertNull(t.search(100));
    }

    /**
     * Verifies that the balanced tree's height is less than
     * or equal to 2 * log2(n + 1) for simple trees where
     * n is less than 25,000.
     */
    @Test
    public void testLogarithmicHeight() {
        var t = new RedBlackTree();
        Assert.assertEquals(-1, t.height());

        t.insert(101, null);
        Assert.assertEquals(0, t.height());

        for (int n = 2; n < 25000; n++) {
            t.insert(100 + n, null);

            Assert.assertTrue(
                    "tree height must be less than expected",
                    t.height() <= expectedHeight(n));
        }
    }

    /**
     * Returns the maximum height of a red-black tree given the
     * specified total number of nodes. This value is 2 * log_2(n + 1).
     */
    private double expectedHeight(int numNodes) {
        // recall: log_a(b) == log(b) / log(a)
        return 2 * (Math.log(numNodes + 1) / Math.log(2));
    }

    @Test
    public void testManyInputs() {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        String[] cases = {
                alphabet,
                new StringBuilder(alphabet).reverse().toString(),
                "qwertyuiopasdfghjklzxcvbnm"
        };

        for (String input : cases) {
            var t = new RedBlackTree();

            // insert each character and check that lookup() returns the value
            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);

                t.insert(c, c);
                Assert.assertEquals(c, t.search(c));
                Assert.assertTrue("tree height is logarithmic",
                        t.height() <= expectedHeight(i + 1));
            }

            Assert.assertTrue("tree height is still logarithmic",
                    t.height() <= expectedHeight(input.length()));

            // check that every character is still in the tree
            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);
                Assert.assertEquals(c, t.search(c));
            }
        }
    }
}
