public class NestedLoops {

    public static int counter = 0;

    public static void forI(int n) {
        for (int i = 0; i < n; i++) {
            System.out.println("i = " + i);
            counter++;
        }
    }

    public static void forIforJ(int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.println("i = " + i + ", j = " + j);
                counter++;
            }
        }
    }

    public static void forIforJforK(int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    System.out.println("i = " + i + ", j = " + j + ", k = " + k);
                    counter++;
                }
            }
        }
    }

    public static void mystery1(int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.println("i = " + i + ", j = " + j);
                counter++;
            }
        }
    }

    public static void mystery2(int n) {
        for (int i = 0; i < n; i++) {
            // j starts at 0, j stops at the current value of i
            for (int j = 0; j < i; j++) {
                System.out.println("i = " + i + ", j = " + j);
                counter++;
            }
        }
    }
}
