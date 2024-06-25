import org.junit.Test;
import org.junit.Assert;

public class TodoAppTest {

    @Test
    public void testNoTodos() {
        var app = new TodoApp();
        Assert.assertFalse(app.deleteTodo("nonexistent"));
    }

    @Test
    public void testOneTodo() {
        var app = new TodoApp();
        app.addTodo("first");

        Assert.assertTrue(app.deleteTodo("first"));
        Assert.assertFalse(app.deleteTodo("first"));
    }

    @Test
    public void testMarkDone() {
        var app = new TodoApp();
        app.addTodo("buy eggs and milk");

        Assert.assertFalse(app.isDone("buy eggs and milk"));

        app.markDone("buy eggs and milk");

        Assert.assertTrue(app.isDone("buy eggs and milk"));
    }
}
