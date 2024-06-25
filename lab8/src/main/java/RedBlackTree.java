/**
 * A red-black tree
 *
 * Similar in structure to 2-3 trees or B-trees of order 4, this balanced tree
 * implementation was invented by Robert Sedgewick in 2008.
 *
 * Nodes are colored either black or red. The black nodes are normal nodes.
 * The red nodes are used to simulate the structure of a 3-node
 * by attaching as a left child to a black node:
 *
 *                b                         b
 *              /   \                     /   \
 *             b     b                   r     b
 *                                     /   \
 *                                    b     b
 *             (2-node)                 (3-node)
 *
 * Newly inserted nodes are red, but if that results in a red node with a red
 * child, a "rotation" occurs, which changes the structure of the tree to
 * ensure that no red node has a red child, which maintains balance:
 *
 *        10b                              10b
 *       /   \                            /   \
 *      5b    15b                        5b    20b
 *              \                             /   \
 *               20r                        15r    25r
 *                 \
 *                  25r (new node)
 *
 *    (before rotation)                (after rotation)
 */
public class RedBlackTree implements Tree {

    private final boolean RED = true;
    private final boolean BLACK = false;

    private class Node {
        public int key;
        public Object data;
        public boolean color;
        public Node left;
        public Node right;

        public Node(int key, Object data, boolean color) {
            this.key = key;
            this.data = data;
            this.color = color;
        }
    }

    private Node root;

    public void insert(int key, Object val) {
        root = insert(root, key, val);
        root.color = BLACK;
    }

    private Node insert(Node n, int key, Object data) {
        if (n == null) {
            return new Node(key, data, RED);
        }

        // insert the the new node/data (same code as non-balanced trees)
        if (key < n.key) {
            n.left = insert(n.left, key, data);
        } else if (key > n.key) {
            n.right = insert(n.right, key, data);
        } else {
            n.data = data;
        }

        // if n has only one red child, it must be on the left ("left-leaning")
        if (isRed(n.right) && !isRed(n.left)) {
            n = rotateLeft(n);
        }

        // two red nodes in a row: n.left and n.left.left
        if (isRed(n.left) && isRed(n.left.left)) {
            n = rotateRight(n);
        }

        // now, both of n's children could be red; if so, swap the colors
        if (isRed(n.left) && isRed(n.right)) {
            swapColors(n);
        }

        return n;
    }

    /**
     * before:           after:
     *          N                  R
     *         / \                / \
     *        L   R              N   RR
     *           / \            / \
     *          RL  RR         L   RL
     *
     * The right node R moves up to the top.
     * The root N becomes the left child of R.
     * The left child RL of the formerly right node R becomes
     *     the right child of the post-rotation left node N.
     * L and RR do not change their relative position.
     */
    private Node rotateLeft(Node n) {
        Node x = n.right;
        n.right = x.left;
        x.left = n;
        x.color = n.color;
        n.color = RED;
        return x;
    }

    /**
     * before:           after:
     *          N                  L
     *         / \                / \
     *        L   R              LL  N
     *       / \                    / \
     *      LL  LR                 LR  R
     *
     * The left node L moves up to the top.
     * The root N becomes the right child of L.
     * The right child LR of the formerly left node L becomes
     *     the left child of the post-rotation right node N.
     * LL and R do not change their relative position.
     */
    private Node rotateRight(Node n) {
        Node x = n.left;
        n.left = x.right;
        x.right = n;
        x.color = n.color;
        n.color = RED;
        return x;
    }

    /** Toggle the colors of a node and its children */
    private void swapColors(Node n) {
        n.color = !n.color;
        n.left.color = !n.left.color;
        n.right.color = !n.right.color;
    }

    /**
     * For a red-black tree with n nodes, the height is at most 2 * log_2(n + 1).
     * This method traverses the tree to find the longest path.
     */
    public int height() {
        return height(root);
    }

    private int height(Node n) {
        if (n == null) {
            return -1;
        }

        if (n.left == null && n.right == null) {
            return 0;
        }

        return Math.max(height(n.left), height(n.right)) + 1;
    }

    public Object search(int key) {
        return search(key, root);
    }

    private Object search(int key, Node root) {
        if (root == null) {
            return null;
        } else if (key == root.key) {
            return root.data;
        } else if (key < root.key) {
            return search(key, root.left);
        } else {
            return search(key, root.right);
        }
    }

    private boolean isRed(Node x) {
        if (x == null) {
            return false;
        }
        return x.color == RED;
    }
}
