import java.io.*;

/**
 * A class for representing a string using a linked list

 * Each character of the string is stored in a separate node. The methods in
 * this class are static methods that take a reference to a string list as a
 * parameter. This approach allows us to use recursion to write many of the
 * methods, and it also allows the methods to handle empty strings, which are
 * represented using a value of null.
 */
public class StringNode {
    private char ch;
    private StringNode next;

    public StringNode(char c, StringNode n) {
        this.ch = c;
        this.next = n;
    }

    /**
     * getNode - private helper method that returns a reference to
     * node i in the given linked-list string.  If the string is too
     * short or if the user passes in a negative i, the method returns null.
     */
    private static StringNode getNode(StringNode str, int i) {
        if (i < 0 || str == null) { // base case 1: not found
            return null;
        } else if (i == 0) { // base case 2: just found
            return str;
        } else {
            return getNode(str.next, i - 1);
        }
    }

    /**
     * charAt - returns the character at the specified index of the
     * specified linked-list string, where the first character has
     * index 0.  If the index i is < 0 or i > length - 1, the method
     * will end up throwing an IllegalArgumentException.
     */
    public static char charAt(StringNode str, int i) {
        if (str == null) {
            throw new IllegalArgumentException("the string is empty");
        }

        StringNode node = getNode(str, i);

        if (node != null) {
            return node.ch;
        } else {
            throw new IllegalArgumentException("invalid index: " + i);
        }
    }

    /**
     * convert - converts a standard Java String object to a linked-list
     * string and returns a reference to the linked-list string
     */
    public static StringNode convert(String s) {
        if (s.length() == 0) {
            return null;
        }

        StringNode firstNode = new StringNode(s.charAt(0), null);
        StringNode prevNode = firstNode;
        StringNode nextNode;

        for (int i = 1; i < s.length(); i++) {
            nextNode = new StringNode(s.charAt(i), null);
            prevNode.next = nextNode;
            prevNode = nextNode;
        }

        return firstNode;
    }

    /**
     * copy - returns a copy of the given linked-list string
     */
    public static StringNode copy(StringNode str) {
        if (str == null) {
            return null;
        }

        // make a recursive call to copy the rest of the list
        StringNode copyRest = copy(str.next);

        // create and return a copy of the first node, 
        // with its next field pointing to the copy of the rest
        return new StringNode(str.ch, copyRest);
    }

    /**
     * deleteChar - deletes character i in the given linked-list string and
     * returns a reference to the resulting linked-list string
     */
    public static StringNode deleteChar(StringNode str, int i) {
        if (str == null) {
            throw new IllegalArgumentException("string is empty");
        } else if (i < 0) {
            throw new IllegalArgumentException("invalid index: " + i);
        } else if (i == 0) {
            str = str.next;
        } else {
            StringNode prevNode = getNode(str, i - 1);
            if (prevNode != null && prevNode.next != null) {
                prevNode.next = prevNode.next.next;
            } else {
                throw new IllegalArgumentException("invalid index: " + i);
            }
        }

        return str;
    }

    /**
     * insertChar - inserts the character ch before the character
     * currently in position i of the specified linked-list string.
     * Returns a reference to the resulting linked-list string.
     */
    public static StringNode insertChar(StringNode str, int i, char ch) {
        StringNode newNode, prevNode;

        if (i < 0) {
            throw new IllegalArgumentException("invalid index: " + i);
        } else if (i == 0) {
            newNode = new StringNode(ch, str);
            str = newNode;
        } else {
            prevNode = getNode(str, i - 1);
            if (prevNode != null) {
                newNode = new StringNode(ch, prevNode.next);
                prevNode.next = newNode;
            } else {
                throw new IllegalArgumentException("invalid index: " + i);
            }
        }

        return str;
    }

    /**
     * insertSorted - inserts character ch in the correct position
     * in a sorted list of characters (i.e., a sorted linked-list string)
     * and returns a reference to the resulting list.
     */
    public static StringNode insertSorted(StringNode str, char ch) {
        StringNode newNode, trail, trav;

        // Find where the character belongs.
        trail = null;
        trav = str;
        while (trav != null && trav.ch < ch) {
            trail = trav;
            trav = trav.next;
        }

        // Create and insert the new node.
        newNode = new StringNode(ch, trav);
        if (trail == null) {
            // We never advanced the prev and trav references, so
            // newNode goes at the start of the list.
            str = newNode;
        } else {
            trail.next = newNode;
        }

        return str;
    }

    /**
     * length - recursively determines the number of characters in the
     * linked-list string to which str refers
     */
    public static int length(StringNode str) {
        if (str == null) {
            return 0;
        } else {
            return 1 + length(str.next);
        }
    }

    /**
     * numOccur - find the number of occurrences of the character
     * ch in the linked list to which str refers
     */
    public static int numOccur(StringNode str, char ch) {
        if (str == null) {
            return 0;
        }

        int numInRest = numOccur(str.next, ch);
        if (str.ch == ch) {
            return 1 + numInRest;
        } else {
            return numInRest;
        }
    }

    /** An iterative verison of numOccur() */
    public static int numOccurIterative(StringNode str, char ch) {
        int numOccur = 0;
        StringNode trav = str;

        while (trav != null) {
            if (trav.ch == ch) {
                numOccur++;
            }

            trav = trav.next;
        }
        return numOccur;
    }

    /**
     * print - recursively writes the specified linked-list string to System.out
     */
    public static void print(StringNode str) {
        if (str == null) {
            return;
        } else {
            System.out.print(str.ch);
            print(str.next);
        }
    }

    /**
     * read - reads a string from an input stream and returns a
     * reference to a linked list containing the characters in the string
     */
    public static StringNode read(InputStream in) throws IOException {
        char ch = (char) in.read();

        if (ch == '\n') { // the string ends when we hit a newline character
            return null;
        } else {
            StringNode restOfString = read(in);
            StringNode first = new StringNode(ch, restOfString);
            return first;
        }
    }

    /** An iterative version of read() */
    public static StringNode readIterative(InputStream in) throws IOException {
        StringNode str = null;
        StringNode current;
        char ch = (char) in.read();

        if (ch == '\n') {
            return str;
        }

        current = new StringNode(ch, null);
        str = current;

        ch = (char) in.read();
        while (ch != '\n') {
            current.next = new StringNode(ch, null);

            current = current.next;
            ch = (char) in.read();
        }

        return str;
    }

    /*
     * toString - creates and returns the Java string that
     * the current StringNode represents.  Note that this
     * method -- unlike the others -- is a non-static method.
     * Thus, it will not work for empty strings, since they
     * are represented by a value of null, and we can't use
     * null to invoke this method.
     */
    public String toString() {
        String str = "";

        StringNode trav = this; // start trav on the current node
        while (trav != null) {
            str = str + trav.ch;
            trav = trav.next;
        }

        return str;
    }

    /**
     * toUpperCase - converts all of the characters in the specified
     * linked-list string to upper case.  Modifies the list itself,
     * rather than creating a new list.
     */
    public static void toUpperCase(StringNode str) {
        StringNode trav = str;
        while (trav != null) {
            trav.ch = Character.toUpperCase(trav.ch);
            trav = trav.next;
        }
    }

    public static void main(String[] args) {
        StringNode str = StringNode.convert("hello");
        StringNode.print(str);
    }
}
