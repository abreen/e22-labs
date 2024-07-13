import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StringNodeTest {

    @Test
    public void testConvert() {
        StringNode str = StringNode.convert("fine");
        assertEquals("fine", str.toString());
    }

    @Test
    public void testCharAt() {
        StringNode str = StringNode.convert("foobar");
        assertEquals('f', StringNode.charAt(str, 0));
        assertEquals('r', StringNode.charAt(str, 5));

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            StringNode.charAt(str, 100);
        });
    }

    @Test
    public void testEmptyLength() {
        assertEquals(0, StringNode.length(null));
    }

    @Test
    public void testLength() {
        StringNode str = StringNode.convert("fine");
        assertEquals(4, StringNode.length(str));
    }

    @Test
    public void testDeleteChar() {
        StringNode str = StringNode.convert("bend");
        str = StringNode.deleteChar(str, 2);
        assertEquals("bed", str.toString());
    }

    @Test
    public void testDeleteFirstChar() {
        StringNode str = StringNode.convert("cable");
        str = StringNode.deleteChar(str, 0);
        assertEquals("able", str.toString());
    }

    @Test
    public void testDeleteLastChar() {
        StringNode str = StringNode.convert("beard");
        str = StringNode.deleteChar(str, 4);
        assertEquals("bear", str.toString());
    }

    @Test
    public void testCopy() {
        StringNode str1 = StringNode.convert("original");
        StringNode str2 = StringNode.copy(str1);

        str2 = StringNode.deleteChar(str2, 0);

        // check that the original was not modified
        assertEquals('o', StringNode.charAt(str1, 0));

        assertEquals('r', StringNode.charAt(str2, 0));
    }

    @Test
    public void testInsertChar() {
        StringNode str = StringNode.convert("seam");
        str = StringNode.insertChar(str, 1, 't');
        assertEquals("steam", str.toString());
    }

    @Test
    public void testDeleteAndInsertChar() {
        StringNode str = StringNode.convert("beer");
        str = StringNode.deleteChar(str, 2);
        str = StringNode.insertChar(str, 2, 'a');
        assertEquals("bear", str.toString());
    }

    @Test
    public void testInsertSorted() {
        StringNode str = StringNode.convert("abcxyz");
        str = StringNode.insertSorted(str, 'm');
        assertEquals("abcmxyz", str.toString());
    }

    @Test
    public void testUpperCase() {
        StringNode str = StringNode.convert("fine");
        StringNode.toUpperCase(str);
        assertEquals("FINE", str.toString());
    }

    @Test
    public void testNumOccur() {
        StringNode str = StringNode.convert("hello");
        assertEquals(2, StringNode.numOccur(str, 'l'));
        assertEquals(0, StringNode.numOccur(str, 'z'));
    }

    @Test
    public void testNumOccurIterative() {
        StringNode str = StringNode.convert("hello");
        assertEquals(2, StringNode.numOccurIterative(str, 'l'));
        assertEquals(0, StringNode.numOccurIterative(str, 'z'));
    }

    @Test
    public void testRead() throws IOException {
        var input = getStreamForString("hello\n");
        StringNode str = StringNode.read(input);
        assertEquals("hello", str.toString());
    }

    @Test
    public void testReadIterative() throws IOException {
        var input = getStreamForString("hello\n");
        StringNode str = StringNode.readIterative(input);
        assertEquals("hello", str.toString());
    }

    private InputStream getStreamForString(String str) {
        return new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
    }
}
