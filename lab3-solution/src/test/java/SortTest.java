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
    public void testSelectionSort() {
        new SelectionSort().sort(input);
        assertTrue("array should be sorted", isSorted(input));
    }

    @Test
    public void testInsertionSort() {
        new InsertionSort().sort(input);
        assertTrue("array should be sorted", isSorted(input));
    }

    @Test
    public void testQuicksort() {
        new Quicksort().sort(input);
        assertTrue("array should be sorted", isSorted(input));
    }

    /** Returns true if the array is in ascending order */
    private static boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }

        return true;
    }
}
