/**
 * A simple collection class
 *
 * A bag is a collection with no guaranteed ordering, no restriction on
 * duplicate values, and no restriction on the types of values.
 */
public class ArrayBag {

    private Object[] items;

    private int numItems;

    public static final int DEFAULT_MAX_SIZE = 50;

    public ArrayBag() {
        this.items = new Object[DEFAULT_MAX_SIZE];
        this.numItems = 0;
    }

    public ArrayBag(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize must be > 0");
        }
        this.items = new Object[maxSize];
        this.numItems = 0;
    }

    public ArrayBag(ArrayBag other) {
        if (other == null) {
            throw new IllegalArgumentException("other cannot be null");
        }

        int newSize = other.numItems * 2;
        this.items = new Object[newSize];
        this.numItems = 0;

        for (int i = 0; i < other.numItems; i++) {
            this.add(other.items[i]);
        }
    }

    public boolean add(Object item) {
        if (item == null) {
            throw new IllegalArgumentException("item must be non-null");
        } else if (this.numItems == this.items.length) {
            return false; // no more room!
        } else {
            this.items[this.numItems] = item;
            this.numItems++;
            return true;
        }
    }

    public boolean contains(Object item) {
        for (int i = 0; i < this.numItems; i++) {
            if (this.items[i].equals(item)) {
                return true;
            }
        }

        return false;
    }

    public boolean remove(Object item) {
        for (int i = 0; i < this.numItems; i++) {
            if (this.items[i].equals(item)) {
                for (int j = i; j < this.numItems - 1; j++) {
                    items[j] = items[j + 1];
                }
                items[numItems - 1] = null;
                this.numItems--;
                return true;
            }
        }

        return false;
    }

    public boolean containsAll(ArrayBag otherBag) {
        if (otherBag == null || otherBag.numItems == 0) {
            return false;
        }

        for (int i = 0; i < otherBag.numItems; i++) {
            if (!this.contains(otherBag.items[i])) {
                return false;
            }
        }

        return true;
    }

    public Object grab() {
        if (this.numItems == 0) {
            throw new IllegalStateException("the bag is empty");
        }

        int whichOne = (int) (Math.random() * this.numItems);
        return this.items[whichOne];
    }

    public Object[] toArray() {
        Object[] copy = new Object[this.numItems];

        for (int i = 0; i < this.numItems; i++) {
            copy[i] = this.items[i];
        }

        return copy;
    }

    /**
     * toString - converts this ArrayBag into a string that can be printed.
     * Overrides the version of this method inherited from the Object class.
     */
    public String toString() {
        String str = "{";

        for (int i = 0; i < this.numItems; i++) {
            str = str + this.items[i];
            if (i != this.numItems - 1) {
                str += ", ";
            }
        }

        str = str + "}";
        return str;
    }
}
