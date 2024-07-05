/**
 * An implementation of selection sort
 */
public class SelectionSort extends Sort {
    public void sort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int j = indexSmallest(arr, i);
            swap(arr, i, j);
        }
    }

    /*
     * indexSmallest - returns the index of the smallest element
     * in the subarray from arr[start] to the end of the array.
     * Used by selectionSort.
     */
    private int indexSmallest(int[] arr, int start) {
        int indexMin = start;

        for (int i = start + 1; i < arr.length; i++) {
            if (arr[i] < arr[indexMin]) {
                indexMin = i;
            }
        }

        return indexMin;
    }

    public static void main(String[] args) {
        int[] arr = parseInput(args);
        new SelectionSort().sort(arr);
        printArray(arr);
    }
}
