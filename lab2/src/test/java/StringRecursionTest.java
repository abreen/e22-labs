import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class StringRecursionTest {

    @Test
    public void testNumOccur() {
        String str = "Mississippi";
        assertEquals(4, StringRecursion.numOccur('s', str));
        assertEquals(0, StringRecursion.numOccur('e', str));
    }

    @Test
    public void testNumOccurEmptyOrNull() {
        assertEquals(0, StringRecursion.numOccur('?', ""));
        assertEquals(0, StringRecursion.numOccur('?', null));
    }

    @Test
    public void testRemoveVowels() {
        assertEquals("Msssspp", StringRecursion.removeVowels("Mississippi"));
        assertEquals("", StringRecursion.removeVowels("ooo"));
        assertEquals("fbr", StringRecursion.removeVowels("foobar"));
    }

    @Test
    public void testRemoveVowelsEmptyOrNull() {
        assertEquals("", StringRecursion.removeVowels(""));
        assertNull(StringRecursion.removeVowels(null));
    }
}
