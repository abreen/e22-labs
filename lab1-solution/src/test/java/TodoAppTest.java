import org.junit.Test;
import org.junit.Assert;

public class TodoAppTest {

    @Test
    public void testNoTodos() {
        String[] empty = {};
        var app = new TodoApp(empty);

        Assert.assertFalse(app.isDone("nonexistent"));
    }

    @Test
    public void testExists() {
        String[] oneElement = { "foobar" };
        var app = new TodoApp(oneElement);

        Assert.assertTrue(app.exists("foobar"));
        Assert.assertFalse(app.exists("nonexistent"));
    }

    @Test
    public void testDeleteTodo() {
        String[] oneElement = { "foobar" };
        var app = new TodoApp(oneElement);

        Assert.assertTrue(app.deleteTodo("foobar"));
        Assert.assertFalse(app.exists("foobar"));
    }

    @Test
    public void testAddTodo() {
        String[] empty = {};
        var app = new TodoApp(empty);

        Assert.assertTrue(app.addTodo("foobar"));
        Assert.assertTrue(app.exists("foobar"));

        // new items should be added as "not done"
        Assert.assertFalse(app.isDone("foobar"));
    }

    @Test
    public void testMarkDone() {
        String[] empty = {};
        var app = new TodoApp(empty);

        app.addTodo("buy eggs and milk");
        Assert.assertFalse(app.isDone("buy eggs and milk"));

        app.markDone("buy eggs and milk");

        Assert.assertTrue(app.isDone("buy eggs and milk"));
    }
}
