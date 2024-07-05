/**
 * An implementation of bubble sort
 */
public class BubbleSort extends Sort {
    public void sort(int[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = parseInput(args);
        new BubbleSort().sort(arr);
        printArray(arr);
    }
}
