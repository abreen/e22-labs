import org.junit.Test;
import org.junit.Assert;

public class TodoAppTest {

    @Test
    public void testEmptyList() {
        var app = new TodoApp();
        Assert.assertNull(app.getTodo(0));
    }

    @Test
    public void testOneItem() {
        var app = new TodoApp();
        app.addTodo("Buy eggs and milk");
        Assert.assertEquals("Buy eggs and milk", app.getTodo(0));
    }
}