import org.junit.Test;
import org.junit.Assert;

public class SumReciprocalsTest {

    @Test
    public void testBaseCase() {
        Assert.assertEquals(0.0, SumReciprocals.sumReciprocals(0), 0.01);
    }
}
