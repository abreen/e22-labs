import java.util.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JsonTest {

    @Test
    public void testEmpty() {
        assertEquals("null", Json.repr((Object) null));
        assertEquals("\"\"", Json.repr(""));
        assertEquals("0", Json.repr(0L));
        assertEquals("0", Json.repr((float) 0.0));
        assertEquals("0", Json.repr((double) 0.0));
    }

    @Test
    public void testSmallInputs() {
        assertEquals("\" \"", Json.repr(' '));
        assertEquals("0.1", Json.repr((double) 0.1));
        assertEquals("123.123", Json.repr((double) 123.123));
        assertEquals("0.0001", Json.repr((double) 0.0001));
        assertEquals("1", Json.repr(1));
        assertEquals("\"z\"", Json.repr('z'));
        assertEquals("\"z\"", Json.repr("z"));
    }

    @Test
    public void testFloatingPointEdgeCases() {
        assertEquals("0.1000000015", Json.repr((float) 0.1));
        assertEquals("0.0000001", Json.repr((float) 0.0000001));
        assertEquals("123.1230010986", Json.repr((float) 123.123));
        assertEquals("1", Json.repr((float) 0.99999999));
    }

    @Test
    public void testIntegers() {
        assertEquals("123456789", Json.repr(123456789));
        assertEquals("123456789123456789", Json.repr(123456789123456789L));
    }

    @Test
    public void testChars() {
        assertEquals("\"'\"", Json.repr('\''));
        assertEquals("\"\\\"\"", Json.repr('"'));
    }

    @Test
    public void testStrings() {
        assertEquals("\"foobar\"", Json.repr("foobar"));

        assertEquals("\"foo'sball\"", Json.repr("foo'sball"));

        // foo"sball -> foo\"sball
        assertEquals("\"foo\\\"sball\"", Json.repr("foo\"sball"));

        // "foo"sball -> \"foo\"sball
        assertEquals("\"\\\"foo\\\"sball\"", Json.repr("\"foo\"sball"));

        assertEquals("\"\\\"foo\\\"\"", Json.repr("\"foo\""));
        assertEquals("\"\\\"\\\"foo\\\"\\\"\"", Json.repr("\"\"foo\"\""));

        // foo\nbar -> foo\nbar
        assertEquals("\"foo\\nbar\"", Json.repr("foo\nbar"));
    }

    @Test
    public void testArrays() {
        {
            int[] arr = { 1, 2, 3 };
            assertEquals("[1, 2, 3]", Json.repr(arr));
        }

        {
            char[] arr = { 'a', 'b', 'c' };
            assertEquals("[\"a\", \"b\", \"c\"]", Json.repr(arr));
        }

        {
            String[] arr = { "hello", "world" };
            assertEquals("[\"hello\", \"world\"]", Json.repr(arr));
        }

        {
            Person[] arr = { new Person("Jane", "Doe"), new Person("John", "Smith") };

            String jane = "{\":class\": \"Person\", \"firstName\": \"Jane\", \"lastName\": \"Doe\", \"sibling\": null}";
            String john = "{\":class\": \"Person\", \"firstName\": \"John\", \"lastName\": \"Smith\", \"sibling\": null}";

            String expected = "[" + jane + ", " + john + "]";
            assertEquals(expected, Json.repr(arr));
        }
    }

    @Test
    public void testObjects() {
        {
            var person = new Person("Jane", "Doe");
            String expected = "{\":class\": \"Person\", \"firstName\": \"Jane\", \"lastName\": \"Doe\", \"sibling\": null}";
            assertEquals(expected, Json.repr(person));
        }

        {
            // private fields should be ignored by repr()
            var dog = new Dog("Lassie", 4);
            String expected = "{\":class\": \"Dog\", \"name\": \"Lassie\"}";
            assertEquals(expected, Json.repr(dog));
        }
    }

    @Test
    public void testRecursiveData() {
        {
            var jane = new Person("Jane", "Doe");
            var john = new Person("John", "Doe", jane);
            jane.sibling = john;

            String johnStr = "{\":class\": \"Person\", \"firstName\": \"John\", \"lastName\": \"Doe\", \"sibling\": \"Person(Jane)\"}";
            String janeStr = "{\":class\": \"Person\", \"firstName\": \"Jane\", \"lastName\": \"Doe\", \"sibling\": "
                    + johnStr + "}";

            assertEquals(janeStr, Json.repr(jane));
        }
    }

    @Test
    public void testRecord() {
        var p = new Point(1, 2);
        var expected = "{\"x\": 1, \"y\": 2}";
        assertEquals(expected, Json.repr(p));
    }

    @Test
    public void testList() {
        assertEquals("[1, 2, 3]", Json.repr(List.of(1, 2, 3)));
    }

    @Test
    public void testGraph() {
        var oneChildren = new ArrayList<Node>();
        var one = new Node(1, oneChildren);
        var two = new Node(2, List.of(one));
        oneChildren.add(two);

        var expected = "{\":class\": \"Node\", \"data\": 1, \"children\": [{\":class\": \"Node\", \"data\": 2, \"children\": [\"Node(1)\"]}]}";
        assertEquals(expected, Json.repr(one));
    }
}

class Person {
    public String firstName;
    public String lastName;
    public Person sibling;

    public Person(String firstName, String lastName, Person sibling) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.sibling = sibling;
    }

    public Person(String firstName, String lastName) {
        this(firstName, lastName, null);
    }

    public String toString() {
        return "Person(" + firstName + ")";
    }
}

class Dog {
    String name;
    private int age;

    Dog(String s, int n) {
        name = s;
        age = n;
    }
}

record Point(int x, int y) {
}

class Node {
    int data;
    List<Node> children;

    Node(int n, List<Node> cs) {
        data = n;
        children = cs;
    }

    public String toString() {
        return "Node(" + data + ")";
    }
}

record NodeRecord(List<NodeRecord> children) {

}
