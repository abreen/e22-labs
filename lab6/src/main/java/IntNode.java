public class IntNode {
    public int datum;
    public IntNode next;

    public IntNode insertFront(IntNode first, int x) {
        first = new IntNode();
        first.datum = x;
        first.next = first;
        return first;
    }

    //public IntNode insertFront(IntNode first, int x) {
    //    IntNode n = new IntNode();
    //    n.datum = x;
    //    n.next = first;
    //    return n;
    //}

    //public IntNode insertFront(IntNode first, int x) {
    //    IntNode n = new IntNode();
    //    first = n;
    //    n.datum = x;
    //    n.next = first;
    //    return first;
    //}
}
