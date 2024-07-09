/**
 * An implementation of quicksort
 */
public class Quicksort extends Sort {
    public void sort(int[] arr) {
        quicksort(arr, 0, arr.length - 1);
    }

    private void quicksort(int[] arr, int first, int last) {
        int split = partition(arr, first, last);

        if (first < split) {
            quicksort(arr, first, split);
        }
        if (last > split + 1) {
            quicksort(arr, split + 1, last);
        }
    }

    private int partition(int[] arr, int first, int last) {
        int pivot = arr[(first + last) / 2];
        int i = first - 1;
        int j = last + 1;

        while (true) {
            // moving from left to right, find an element >= the pivot
            do {
                i++;
            } while (arr[i] < pivot);

            // moving from right to left, find an element <= the pivot
            do {
                j--;
            } while (arr[j] > pivot);

            if (i < j) {
                swap(arr, i, j);
            } else {
                return j; // index of last element in the left subarray
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = parseInput(args);
        new Quicksort().sort(arr);
        printArray(arr);
    }
}
