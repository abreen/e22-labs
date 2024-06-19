import org.junit.Test;
import org.junit.Assert;

public class ArrayBagTest {
    @Test
    public void testAddAndGrab() {
        var bag = new ArrayBag();
        bag.add("foo");

        Assert.assertEquals("foo", bag.grab());

        bag.add("bar");

        Object secondGrab = bag.grab();

        // important: we need to save to a variable. why?
        Assert.assertTrue(
                "grab returns either foo or bar",
                secondGrab.equals("foo") || secondGrab.equals("bar"));
    }
}
