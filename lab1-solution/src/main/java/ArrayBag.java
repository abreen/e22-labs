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

    /**
     * Constructor with no parameters - creates a new, empty ArrayBag with 
     * the default maximum size.
     */
    public ArrayBag() {
        this.items = new Object[DEFAULT_MAX_SIZE];
        this.numItems = 0;
    }

    /** 
     * A constructor that creates a new, empty ArrayBag with the specified
     * maximum size.
     */
    public ArrayBag(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize must be > 0");
        }
        this.items = new Object[maxSize];
        this.numItems = 0;
    }

    /**
     * A constructor that creates a new ArrayBag with the same items as the
     * specified ArrayBag.
     */
    public ArrayBag(ArrayBag other) {
        if (other == null) {
            throw new IllegalArgumentException("other bag cannot be null");
        }

        int maxSize = other.numItems * 5;
        this.items = new Object[maxSize];
        this.numItems = 0;

        for (int i = 0; i < other.numItems; i++) {
            this.add(other.items[i]);
        }
    }

    /** 
     * add - adds the specified item to this ArrayBag. Returns true if there 
     * is room to add it, and false otherwise.
     * Throws an IllegalArgumentException if the item is null.
     */
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

    /** 
     * remove - removes one occurrence of the specified item (if any)
     * from this ArrayBag.  Returns true on success and false if the
     * specified item (i.e., an object equal to item) is not in this ArrayBag.
     */
    public boolean remove(Object item) {
        for (int i = 0; i < this.numItems; i++) {
            if (this.items[i].equals(item)) {
                // Shift the remaining items left by one.
                for (int j = i; j < this.numItems - 1; j++) {
                    this.items[j] = this.items[j + 1];
                }
                this.items[this.numItems - 1] = null;

                this.numItems--;
                return true;
            }
        }

        return false; // item not found
    }

    /**
     * contains - returns true if the specified item is in the Bag, and
     * false otherwise.
     */
    public boolean contains(Object item) {
        for (int i = 0; i < this.numItems; i++) {
            if (this.items[i].equals(item)) {
                return true;
            }
        }

        return false;
    }

    /**
     * containsAll - does this ArrayBag contain all of the items in
     * otherBag?  Returns false if otherBag is null or empty. 
     */
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

    /**
     * grab - returns a reference to a randomly chosen item in this ArrayBag.
     */
    public Object grab() {
        if (this.numItems == 0) {
            throw new IllegalStateException("the bag is empty");
        }

        int whichOne = (int) (Math.random() * this.numItems);
        return this.items[whichOne];
    }

    /**
     * toArray - return an array containing the current contents of the bag
     */
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
