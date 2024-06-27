import org.junit.Test;
import org.junit.Assert;

public class ComboSumTest {

    @Test
    public void testInvalidInput() {
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            int[] input = { 3, 2, 1 };
            new ComboSum(-10, input);
        });

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            new ComboSum(5, null);
        });
    }

    @Test
    public void testSimpleInput() {
        int[] input = { 3, 2, 1 };
        var solver = new ComboSum(5, input);
        solver.findSolutions();
    }

    @Test
    public void testUnsolvableInput() {
        int[] input = { 3, 2, 1 };
        var solver = new ComboSum(500, input);
        solver.findSolutions();
    }
}
