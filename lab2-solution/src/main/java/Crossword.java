/**
 * A crossword-style example of recursive backtracking
 */
public class Crossword {
    private int numRows;
    private int numCols;

    private String[] dictionary;

    private boolean[] usedWords;

    // e.g., "7 > 3 2 3" stands for: 7 across, starting at 3x2, 3 chars long
    private String[] spaces;

    // e.g., "d 3" stands for: letter d here, from word index 3
    private String[][] state;

    public Crossword(int numRows, int numCols, String[] words, String... tuples) {
        this.numRows = numRows;
        this.numCols = numCols;
        dictionary = words;
        usedWords = new boolean[dictionary.length];
        spaces = tuples;
        state = new String[numRows][numCols];
    }

    private static Integer getInt(String tuple, int i) {
        String[] parts = tuple.split(" ");
        return Integer.parseInt(parts[i]);
    }

    private static char getChar(String tuple, int i) {
        return tuple.split(" ")[i].charAt(0);
    }

    private static String tuple(char ch, int wordIndex) {
        return String.format("%s %d", ch, wordIndex);
    }

    private boolean spaceIsAcross(int spaceIndex) {
        return getChar(spaces[spaceIndex], 1) == '>';
    }

    private String spaceToString(int spaceIndex) {
        return getInt(spaces[spaceIndex], 0) + " " +
                (spaceIsAcross(spaceIndex) ? "across" : "down");
    }

    private boolean spaceIsFull(int spaceIndex) {
        int row = startingRow(spaceIndex);
        int col = startingColumn(spaceIndex);
        int len = spaceSize(spaceIndex);

        int count = 0;

        for (int i = 0; i < len; i++) {
            if (state[row][col] != null) {
                count++;
            }

            if (spaceIsAcross(spaceIndex)) {
                col += 1;
            } else {
                row += 1;
            }
        }

        return count == len;
    }

    private int spaceSize(int spaceIndex) {
        return getInt(spaces[spaceIndex], 4);
    }

    private int startingRow(int spaceIndex) {
        return getInt(spaces[spaceIndex], 2);
    }

    private int startingColumn(int spaceIndex) {
        return getInt(spaces[spaceIndex], 3);
    }

    private void applyValue(int wordIndex, int spaceIndex) {
        int row = startingRow(spaceIndex);
        int col = startingColumn(spaceIndex);

        for (int i = 0; i < spaceSize(spaceIndex); i++) {
            if (state[row][col] == null) {
                state[row][col] = tuple(dictionary[wordIndex].charAt(i), wordIndex);
            }

            if (spaceIsAcross(spaceIndex)) {
                col += 1;
            } else {
                row += 1;
            }
        }

        usedWords[wordIndex] = true;
    }

    private void removeValue(int wordIndex, int spaceIndex) {
        int row = startingRow(spaceIndex);
        int col = startingColumn(spaceIndex);

        for (int i = 0; i < spaceSize(spaceIndex); i++) {
            if (state[row][col] != null) {
                if (getInt(state[row][col], 1) == wordIndex) {
                    state[row][col] = null;
                }
            }

            if (spaceIsAcross(spaceIndex)) {
                col += 1;
            } else {
                row += 1;
            }
        }

        usedWords[wordIndex] = false;
    }

    private boolean isValid(int wordIndex, int spaceIndex) {
        return dictionary[wordIndex].length() == spaceSize(spaceIndex) &&
                intersectionsMatch(wordIndex, spaceIndex);
    }

    private boolean intersectionsMatch(int wordIndex, int spaceIndex) {
        String word = dictionary[wordIndex];

        int row = startingRow(spaceIndex);
        int col = startingColumn(spaceIndex);

        for (int i = 0; i < word.length(); i++) {
            if (state[row][col] != null) {
                if (getChar(state[row][col], 0) != word.charAt(i)) {
                    return false;
                }
            }

            if (spaceIsAcross(spaceIndex)) {
                col += 1;
            } else {
                row += 1;
            }
        }

        return true;
    }

    public String rowToString(int row, int highlightWordIndex) {
        String yellow = "\u001b[33m", reset = "\u001b[0m";
        String str = "";

        for (int col = 0; col < numCols; col++) {
            String letter = "";

            if (state[row][col] == null) {
                // no letter filled in
                letter = " ";
            } else {
                char ch = getChar(state[row][col], 0);
                int wordIndex = getInt(state[row][col], 1);

                if (wordIndex == highlightWordIndex) {
                    letter = yellow + ch + reset;
                } else {
                    letter = "" + ch;
                }
            }

            if (col == numCols - 1) {
                str += " " + letter;
            } else {
                str += " " + letter + " |";
            }
        }

        return str;
    }

    public void displayCrossword() {
        displayCrossword(-1);
    }

    private void printBorderRow() {
        for (int k = 0; k < numCols; k++) {
            if (k == numCols - 1) {
                System.out.print("---");
            } else {
                System.out.print("---+");
            }
        }

        System.out.println();
    }

    public void displayCrossword(int highlightWordIndex) {
        for (int i = 0; i < numRows; i++) {
            if (i > 0) {
                printBorderRow();
            }

            System.out.println(rowToString(i, highlightWordIndex));
        }
    }

    public void findSolutions(int spaceIndex) {
        if (spaceIndex >= spaces.length) {
            System.out.println("found a solution:");
            displayCrossword();
            return;
        }

        if (spaceIsFull(spaceIndex)) {
            System.out.println("skipping " + spaceToString(spaceIndex)
                    + ", it's full");

            findSolutions(spaceIndex + 1);
            return;
        }

        for (int wordIndex = 0; wordIndex < dictionary.length; wordIndex++) {
            if (usedWords[wordIndex]) {
                continue;
            }

            if (isValid(wordIndex, spaceIndex)) {
                System.out.println("applying " + dictionary[wordIndex]
                        + " at " + spaceToString(spaceIndex));
                applyValue(wordIndex, spaceIndex);
                displayCrossword(wordIndex);
                findSolutions(spaceIndex + 1);
                removeValue(wordIndex, spaceIndex);
            }
        }

        System.out.println("no more valid words for " + spaceToString(spaceIndex));
    }

    public static void main(String[] args) {
        String[] dictionary = { "AFT", "ALE", "EEL", "HEEL", "HIKE", "HOSES",
                "KEEL", "LASER", "LEE", "LINE", "SAILS",
                "SHEET", "STEER", "TIE" };

        var cw = new Crossword(6, 5, dictionary,
                "1 > 0 0 5",
                "2 v 0 2 5",
                "3 v 0 4 5",
                "4 > 2 1 4",
                "5 v 2 3 4",
                "6 v 3 0 3",
                "7 > 3 2 3",
                "8 > 4 0 5");

        cw.findSolutions(0);
    }
}
