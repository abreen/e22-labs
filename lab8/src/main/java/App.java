import java.util.Random;

// TODO take command line argument for which tree implementation to use
// TODO let WeatherApp take the Tree interface

public class App {
    public static void main(String[] args) {
        Random r = new Random();

        Tree t1 = new BinaryTree();
        Tree t2 = new RedBlackTree();

        for (int i = 0; i < 1000; i++) {
            int randomValue = r.nextInt(500) - 500;

            t1.insert(randomValue, null);
            t2.insert(randomValue, null);

            System.out.printf("%4d%8d%8d\n",
                    i + 1, t1.height(), t2.height());
        }

        System.out.println("n   tree    red-black");
    }
}
