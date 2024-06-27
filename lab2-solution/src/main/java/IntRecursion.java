import java.io.*;

/**
 * A class containing various recursive methods over ints
 */
public class IntRecursion {

    /** Calculate F_n (Fibonacci number n) */
    public static int fib(int n) {
        if (n == 0 || n == 1) {
            return n;
        }

        return fib(n - 2) + fib(n - 1);
    }

    /** Calculate F_n iteratively */
    public static long fibIterative(int n) {
        if (n <= 0) {
            return 0;
        }

        long previous = 0;
        long current = 1;

        for (int i = 2; i <= n; i++) {
            long tmp = previous + current;
            previous = current;
            current = tmp;
        }

        return current;
    }

    /** A more performant (but less elegant) recursive fib() */
    public static long fibFaster(int n) {
        if (n == 0) {
            return 0;
        }
        return fibFasterHelper(n, 1, 0);
    }

    private static long fibFasterHelper(int n, long curr, long prev) {
        if (n == 1) {
            return curr;
        }
        return fibFasterHelper(n - 1, curr + prev, curr);
    }

    /** Calculate sum of integers between 1 and n */
    public static int sum(int n) {
        if (n <= 0) {
            return 0;
        }

        if (n == 1) {
            return 1;
        }

        return n + sum(n - 1);
    }

    /** Print the integers between n1 and n2 */
    public static void printSeries(int n1, int n2) {
        // allow the default PrintStream to be System.out
        printSeries(System.out, n1, n2);
    }

    /** Print the integers between n1 and n2 to the specified PrintStream */
    public static void printSeries(PrintStream out, int n1, int n2) {
        if (n1 == n2) {
            out.println(n2);
        } else {
            out.print(n1 + ", ");

            printSeries(out, n1 + 1, n2);
        }
    }
}
