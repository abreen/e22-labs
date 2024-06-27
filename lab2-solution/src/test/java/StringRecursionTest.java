import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class StringRecursionTest {

    @Test
    public void testRemoveCapitals() {
        assertEquals("ooar", StringRecursion.removeCapitals("FooBar"));
        assertEquals("zzz", StringRecursion.removeCapitals("AAAzzzAAA"));
        assertEquals("s", StringRecursion.removeCapitals("Ms"));
    }

    @Test
    public void testRemoveCapitalsEmptyOrNull() {
        assertEquals("", StringRecursion.removeCapitals(""));

        // testing that an exception will be thrown (using a lambda)
        assertThrows(IllegalArgumentException.class,
                () -> StringRecursion.removeCapitals(null));

        // here's a simpler way using Assert.fail()
        try {
            StringRecursion.removeCapitals(null);
            Assert.fail("");
        } catch (IllegalArgumentException expected) {
        }
    }

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
