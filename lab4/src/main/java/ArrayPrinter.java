import java.util.Arrays;

/**
 * A class that offers different ways of printing int[] arrays
 *
 * The terminal text color is modified using ANSI escape sequences.
 */
public class ArrayPrinter {

    private static final int[] ANSI_RAINBOW_COLORS = {
            196, 202, 208, 214, 220, 226, 190, 154, 118, 82, 46, 47, 48, 49, 50, 51, 45, 39, 33, 27, 21, 57, 93, 129,
            165, 201, 200, 199, 198, 197
    };

    private static final char[] blocksOutOf8 = {
            ' ', '▁', '▂', '▃', '▄', '▅', '▆', '▇', '█'
    };

    private Sort sorter;
    private Terminal term;

    public ArrayPrinter(Sort sorter, Terminal term) {
        this.sorter = sorter;
        this.term = term;
    }

    /** Print the elements with colors according to their final sorted position */
    public void rainbow(int[] arr) {
        int[] arrSorted = Arrays.copyOf(arr, arr.length);
        sorter.quicksort(arrSorted);

        int min = arrSorted[0], max = arrSorted[arrSorted.length - 1];
        int numElementChars = Math.max(String.valueOf(min).length(), String.valueOf(max).length());

        String formatString = "%" + numElementChars + "d";

        int charsOnColumn = 0;
        for (int i = 0; i < arr.length; i++) {
            if (charsOnColumn + numElementChars >= term.getColumns()) {
                term.println();
                charsOnColumn = 0;
            } else {
                charsOnColumn += numElementChars;
            }

            int ansiColor = getAnsiColor(arr, arrSorted, i);

            String element = String.format(formatString, arr[i]);

            term.setColor(ansiColor);
            term.print(element);
            term.reset();
            term.print(" ");
        }

        term.println();
    }

    private int getAnsiColor(int[] arr, int[] sortedCopy, int i) {
        int sortedIndex = Arrays.binarySearch(sortedCopy, arr[i]);
        double fractionOfSorted = (double) sortedIndex / arr.length;

        // map back to the rainbow colors (each element maps to one of these)
        int colorIndex = (int) (fractionOfSorted * ANSI_RAINBOW_COLORS.length);
        int ansiColor = ANSI_RAINBOW_COLORS[colorIndex];
        return ansiColor;
    }

    /** Print the elements as vertical bars */
    public void bars(int[] arr, boolean useColor) {
        int[] arrSorted = Arrays.copyOf(arr, arr.length);
        sorter.quicksort(arrSorted);

        int barHeightLines = term.getLines();

        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (int element : arr) {
            if (element > max) {
                max = element;
            }
            if (element < min) {
                min = element;
            }
        }

        double range = max - min + 1;
        double valuePerLine = range / barHeightLines;

        for (int h = barHeightLines - 1; h >= 0; h--) {
            double lineMin = min + (h - 1) * valuePerLine;
            double lineMax = min + h * valuePerLine;

            for (int i = 0; i < arr.length; i++) {
                if (useColor) {
                    int ansiColor = getAnsiColor(arr, arrSorted, i);
                    term.setColor(ansiColor);
                }

                if (arr[i] >= lineMin && arr[i] >= lineMax) {
                    term.print(blocksOutOf8[8]);
                } else if (arr[i] >= lineMin && arr[i] <= lineMax) {
                    double remainder = arr[i] - lineMin;
                    int index = (int) (remainder / valuePerLine * (blocksOutOf8.length - 1));
                    term.print(blocksOutOf8[index]);
                } else {
                    term.print(blocksOutOf8[0]);
                }

                if (useColor) {
                    term.reset();
                }
            }

            term.println();
        }
    }

    public void bars(int[] arr) {
        bars(arr, false);
    }
}
