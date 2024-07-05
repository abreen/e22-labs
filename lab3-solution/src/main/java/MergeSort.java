/**
 * An implementation of merge sort
 */
public class MergeSort extends Sort {
    public void sort(int[] arr) {
        int[] temp = new int[arr.length];
        mergeSort(arr, temp, 0, arr.length - 1);
    }

    private static void mergeSort(int[] arr, int[] temp, int start, int end) {
        if (start >= end) {
            return;
        }

        int middle = (start + end) / 2;
        mergeSort(arr, temp, start, middle);
        mergeSort(arr, temp, middle + 1, end);
        merge(arr, temp, start, middle, middle + 1, end);
    }

    private static void merge(int[] arr, int[] temp,
            int leftStart, int leftEnd, int rightStart, int rightEnd) {
        int i = leftStart; // index into left subarray
        int j = rightStart; // index into right subarray
        int k = leftStart; // index into temp

        while (i <= leftEnd && j <= rightEnd) {
            if (arr[i] < arr[j]) {
                temp[k] = arr[i];
                i++;
                k++;
            } else {
                temp[k] = arr[j];
                j++;
                k++;
            }
        }

        while (i <= leftEnd) {
            temp[k] = arr[i];
            i++;
            k++;
        }
        while (j <= rightEnd) {
            temp[k] = arr[j];
            j++;
            k++;
        }

        for (i = leftStart; i <= rightEnd; i++) {
            arr[i] = temp[i];
        }
    }

    public static void main(String[] args) {
        int[] arr = parseInput(args);
        new MergeSort().sort(arr);
        printArray(arr);
    }
}
