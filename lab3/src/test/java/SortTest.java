import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertTrue;

public class SortTest {

    private static int[] input;

    @Before
    public void setup() {
        input = new int[] { 7, 39, 20, 11, 16, 5 };
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
