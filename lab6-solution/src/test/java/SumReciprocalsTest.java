import org.junit.Test;
import org.junit.Assert;

public class SumReciprocalsTest {

    @Test
    public void testBaseCase() {
        Assert.assertEquals(0.0, SumReciprocals.sumReciprocals(0), 0.01);
    }

    @Test
    public void testSimpleCases() {
        Assert.assertEquals(1.0, SumReciprocals.sumReciprocals(1), 0.01);
        Assert.assertEquals(1.0 + (1.0 / 2), SumReciprocals.sumReciprocals(2), 0.01);
        Assert.assertEquals(1.0 + (1.0 / 2) + (1.0 / 3), SumReciprocals.sumReciprocals(3), 0.01);
    }
}
