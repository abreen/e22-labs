import java.util.Random;

public class App {
    public static void main(String[] args) {
        Sort sorter = new Sort();

        Random rand = new Random();
        int[] input = randomArray(rand, 50, 100);

        ArrayPrinter printer = new ArrayPrinter(sorter, new AnsiTerminal());

        printer.bars(input, true);

        sorter.quicksort(input);
        System.out.println();

        printer.bars(input, true);
    }

    private static int[] randomArray(Random r, int size, int range) {
        int[] arr = new int[size];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = r.nextInt(2 * range) - range;
        }
        return arr;
    }
}
