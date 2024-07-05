/**
 * An implementation of Shell sort
 */
public class ShellSort extends Sort {
    public void sort(int[] arr) {
        /*
         * Find initial increment: one less than the largest
         * power of 2 that is <= the number of objects.
         */
        int incr = 1;
        while (2 * incr <= arr.length) {
            incr = 2 * incr;
        }
        incr = incr - 1;

        // Do insertion sort for each increment
        while (incr >= 1) {
            for (int i = incr; i < arr.length; i++) {
                if (arr[i] < arr[i - incr]) {
                    int toInsert = arr[i];

                    int j = i;
                    do {
                        arr[j] = arr[j - incr];
                        j = j - incr;
                    } while (j > incr - 1 &&
                            toInsert < arr[j - incr]);

                    arr[j] = toInsert;
                }
            }

            // Calculate increment for next pass
            incr = incr / 2;
        }
    }

    public static void main(String[] args) {
        int[] arr = parseInput(args);
        new ShellSort().sort(arr);
        printArray(arr);
    }
}
