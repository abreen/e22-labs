import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertTrue;

public class SortTest {

    private static int[] input;

    @Before
    public void setup() {
        input = new int[] { 7, 39, 20, 11, 16, 5 };
    }

    @Test
    public void testQuicksort() {
        new Quicksort().sort(input);
        assertTrue("array should be sorted", Sort.isSorted(input));
    }

    @Test
    public void testMergeSort() {
        new MergeSort().sort(input);
        assertTrue("array should be sorted", Sort.isSorted(input));
    }

}
