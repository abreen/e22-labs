public class SumReciprocals {

    public static double sumReciprocals(int n) {
        if (n <= 0) {
            return 0.0;
        }

        return (1.0 / n) + sumReciprocals(n - 1);
    }
}
