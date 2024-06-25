/**
 * A binary tree
 *
 * A simplified version of LinkedTree that stores a single Object as data.
 */
public class BinaryTree implements Tree {

    private class Node {
        private int key;
        private Object data;
        private Node left;
        private Node right;

        private Node(int key, Object data) {
            this.key = key;
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    private Node root;

    public Object search(int key) {
        Node n = search(root, key);
        if (n == null) {
            return null;
        } else {
            return n.data;
        }
    }

    private static Node search(Node root, int key) {
        if (root == null) {
            return null;
        } else if (key == root.key) {
            return root;
        } else if (key < root.key) {
            return search(root.left, key);
        } else {
            return search(root.right, key);
        }
    }

    public void insert(int key, Object data) {
        // find the parent of the new node
        Node parent = null;
        Node trav = root;
        while (trav != null) {
            if (trav.key == key) {
                trav.data = data;
                return;
            }
            parent = trav;
            if (key < trav.key) {
                trav = trav.left;
            } else {
                trav = trav.right;
            }
        }

        Node newNode = new Node(key, data);
        if (parent == null) {
            root = newNode;
        } else if (key < parent.key) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
    }

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
}
