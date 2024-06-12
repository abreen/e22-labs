/**
 * A balanced binary tree
 * 
 * A red-black tree is an alternative to 2-3 trees where nodes are colored
 * either black or red. The trick is to guarantee that each path from a node
 * to any of its descendant leaves has the same number of black nodes.
 * To maintain this property and the balance of the tree as a whole,
 * one or more "rotations" may occur after an insertion.
 * 
 * In a left rotation, the right child of a node becomes the new root,
 * and the original root becomes the left child of the new root.
 * 
 * In a right rotation, the left child of a node becomes the new root,
 * and the original root becomes the right child of the new root.
 */
public class RedBlackTree {

    private final boolean RED = true;
    private final boolean BLACK = false;

    private class Node {
        public int key;
        public boolean color = RED;
        public Node left;
        public Node right;
        public Node parent; // also see LinkedTree (PS 4, Problem 7)
    }

    private Node root;

    public void insert(int key) {
        Node n = new Node();
        n.key = key;

        root = insert(root, n);

        doRotations(n);
    }

    private Node insert(Node root, Node n) {
        if (root == null)
            return n;
        if (n.key < root.key) {
            root.left = insert(root.left, n);
            root.left.parent = root;
        } else if (n.key > root.key) {
            root.right = insert(root.right, n);
            root.right.parent = root;
        }
        return root;
    }

    /**
     * before:           after:
     *          N                  R
     *         / \                / \
     *        L   R              N   RR
     *           / \            / \
     *          RL  RR         L  RL
     * 
     * The right node R moves up to the top.
     * The root N becomes the left child of R.
     * The left child RL of the formerly right node R becomes
     *     the right child of the post-rotation left node N.
     * L and RR do not change their relative position.
     */
    private void rotateLeft(Node node) {
        Node rightNode = node.right;

        node.right = rightNode.left;
        if (rightNode.left != null)
            rightNode.left.parent = node;
        rightNode.parent = node.parent;

        if (node.parent == null)
            root = rightNode;
        else if (node == node.parent.left)
            node.parent.left = rightNode;
        else
            node.parent.right = rightNode;

        rightNode.left = node;
        node.parent = rightNode;
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
    private void rotateRight(Node node) {
        Node leftNode = node.left;
        node.left = leftNode.right;
        if (leftNode.right != null)
            leftNode.right.parent = node;
        leftNode.parent = node.parent;

        if (node.parent == null)
            root = leftNode;
        else if (node == node.parent.left)
            node.parent.left = leftNode;
        else
            node.parent.right = leftNode;

        leftNode.right = node;
        node.parent = leftNode;
    }

    private void doRotations(Node node) {
        Node parent = null, grandParent = null;
        while (node != root && node.color == RED && node.parent.color == RED) {
            parent = node.parent;
            grandParent = node.parent.parent;

            if (parent == grandParent.left) {
                Node uncle = grandParent.right;
                if (uncle != null && uncle.color == RED) {
                    grandParent.color = RED;
                    parent.color = BLACK;
                    uncle.color = BLACK;
                    node = grandParent;
                } else {
                    if (node == parent.right) {
                        rotateLeft(parent);
                        node = parent;
                        parent = node.parent;
                    }
                    rotateRight(grandParent);
                    swapColors(parent, grandParent);
                    node = parent;
                }
            } else {
                Node uncle = grandParent.left;
                if (uncle != null && uncle.color == RED) {
                    grandParent.color = RED;
                    parent.color = BLACK;
                    uncle.color = BLACK;
                    node = grandParent;
                } else {
                    if (node == parent.left) {
                        rotateRight(parent);
                        node = parent;
                        parent = node.parent;
                    }
                    rotateLeft(grandParent);
                    swapColors(parent, grandParent);
                    node = parent;
                }
            }
        }
        root.color = BLACK;
    }

    private void swapColors(Node a, Node b) {
        boolean temp = a.color;
        a.color = b.color;
        b.color = temp;
    }

    public int height() {
        return height(root);
    }

    private int height(Node n) {
        if (n == null)
            return -1;
        if (n.left == null && n.right == null) {
            return 0;
        }
        return Math.max(height(n.left), height(n.right) + 1);
    }
}
