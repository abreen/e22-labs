import org.junit.Test;
import org.junit.Assert;

public class IntNodeTest {

    @Test
    public void testInsertFront() {
        IntNode n = new IntNode();
        n.datum = 123;

        IntNode newNode = n.insertFront(n, 456);

        Assert.assertEquals(456, newNode.datum);
        Assert.assertEquals(n, newNode.next);
        Assert.assertNull(n.next);
    }
}
