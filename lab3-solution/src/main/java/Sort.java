/**
 * The superclass for all sorting implementations
 * 
 * Each Sort subclass implements a main() method that takes integers
 * from command line arguments and sorts them, printing the result.
 */
public abstract class Sort {

    /** Sort an integer array into ascending order */
    public abstract void sort(int[] arr);

    /** Swap two array elements */
    public static void swap(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    /** Convert an array of strings containing ints to an int[] */
    public static int[] parseInput(String[] args) throws NumberFormatException {
        int[] arr = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            arr[i] = Integer.parseInt(args[i]);
        }
        return arr;
    }

    /** Print an array on one line */
    public static void printArray(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println(arr[arr.length - 1]);
    }
}
