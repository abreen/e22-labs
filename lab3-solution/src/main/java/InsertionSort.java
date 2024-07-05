/**
 * An implementation of insertion sort
 */
public class InsertionSort extends Sort {
    public void sort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) {
                int toInsert = arr[i];

                int j = i;
                do {
                    arr[j] = arr[j - 1];
                    j = j - 1;
                } while (j > 0 && toInsert < arr[j - 1]);

                arr[j] = toInsert;
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = parseInput(args);
        new InsertionSort().sort(arr);
        printArray(arr);
    }
}
