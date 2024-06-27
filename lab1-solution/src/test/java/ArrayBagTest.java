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

    @Test
    public void testRemove() {
        ArrayBag b1 = new ArrayBag();
        b1.add("hi");
        b1.add(32);
        b1.add('@');
        b1.add(false);

        b1.remove(32);

        Assert.assertEquals(false, b1.contains(32));
    }

    @Test
    public void testConstructor() {
        ArrayBag b1 = new ArrayBag();
        b1.add("hello");

        ArrayBag b2 = new ArrayBag(b1);
        b2.add("world");
        Assert.assertTrue(b2.contains("world"));
        Assert.assertFalse(b1.contains("world"));
    }

    @Test
    public void testConstructorThrowsExceptionOnNull() {
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            new ArrayBag(null);
        });
    }
}
