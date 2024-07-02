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
        var s = new Sort();
        s.selectionSort(input);
        assertTrue("array should be sorted", isSorted(input));
    }

    @Test
    public void testInsertionSort() {
        var s = new Sort();
        s.insertionSort(input);
        assertTrue("array should be sorted", isSorted(input));
    }

    @Test
    public void testQuicksort() {
        var s = new Sort();
        s.quicksort(input);
        assertTrue("array should be sorted", isSorted(input));
    }

    private static boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }

        return true;
    }
}
