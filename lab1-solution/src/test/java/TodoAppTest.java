import org.junit.Test;
import org.junit.Assert;

public class TodoAppTest {

    @Test
    public void testEmptyList() {
        // {null, null, null, null, null}
        String[] allNulls = makeArray(5);

        TodoApp app = new TodoApp(allNulls);

        Assert.assertNull(app.getTodo(0));
    }

    @Test
    public void testOneItem() {
        // {"Buy eggs and milk", null, null, null, null}
        var oneItemList = makeArray(5, "Buy eggs and milk");

        var app = new TodoApp(oneItemList);

        Assert.assertEquals("Buy eggs and milk", app.getTodo(0));
        Assert.assertNull(app.getTodo(1));
    }

    @Test
    public void testMarkDone() {
        // {"Buy eggs and milk", null, null, null, null}
        var oneItemList = makeArray(5, "Buy eggs and milk");

        var app = new TodoApp(oneItemList);

        Assert.assertFalse(app.getDone(0));
        app.markDone(0);
        Assert.assertTrue(app.getDone(0));
    }

    private static String[] makeArray(int length, String... strings) {
        var arr = new String[length];
        if (strings.length > 0) {
            System.arraycopy(strings, 0, arr, 0, strings.length);
        }
        return arr;
    }
}
