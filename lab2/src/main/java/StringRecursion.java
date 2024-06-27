/**
 * A class containing various recursive methods over Strings
 */
public class StringRecursion {

    /** Return a new String without any capital (uppercase) letters */
    public static String removeCapitals(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }

        if (s.equals("")) {
            return "";
        }

        // TODO recursive call
        String removedFromRest = null;

        return null;
    }

    /** Return the number of times a char appears in a String */
    public static int numOccur(char ch, String str) {
        if (str == null || str.equals("")) {
            return 0;
        }

        int numOccurInRest = numOccur(ch, str.substring(1));
        if (ch == str.charAt(0)) {
            return 1 + numOccurInRest;
        } else {
            return numOccurInRest;
        }
    }

    /** Return a new String without any vowels ('a', 'e', 'i', 'o', 'u') */
    public static String removeVowels(String str) {
        if (str == null) {
            return null;
        }

        if (str.equals("")) {
            return "";
        }

        String removedFromRest = removeVowels(str.substring(1));

        // If the first character in str is a vowel,
        // we don't include it in the return value.
        // If it isn't a vowel, we do include it.
        char first = str.charAt(0);
        if (first == 'a' || first == 'e' || first == 'i' ||
                first == 'o' || first == 'u') {
            return removedFromRest;
        } else {
            return first + removedFromRest;
        }
    }
}
