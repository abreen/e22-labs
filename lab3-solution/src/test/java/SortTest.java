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
    public void testBubbleSort() {
        new BubbleSort().sort(input);
        assertTrue("array should be sorted", Sort.isSorted(input));
    }

    @Test
    public void testSelectionSort() {
        new SelectionSort().sort(input);
        assertTrue("array should be sorted", Sort.isSorted(input));
    }

    @Test
    public void testInsertionSort() {
        new InsertionSort().sort(input);
        assertTrue("array should be sorted", Sort.isSorted(input));
    }

    @Test
    public void testShellSort() {
        new ShellSort().sort(input);
        assertTrue("array should be sorted", Sort.isSorted(input));
    }

}
