public interface Tree {
    void insert(int key, Object data);

    Object search(int key);

    int height();
}
