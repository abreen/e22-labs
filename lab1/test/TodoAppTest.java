import org.junit.Test;
import org.junit.Assert;

public class TodoAppTest {

    @Test
    public void testEmptyList() {
        var app = new TodoApp();
        Assert.assertNull(app.getTodo(0));
    }
}
