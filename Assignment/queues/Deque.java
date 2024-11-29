import java.util.Iterator;

/* *****************************************************************************
 *  Name: Deque
 *  Date: 2024-11-06
 *  Description: A double-ended queue or deque (pronounced “deck”) is a generalization
 *  of a stack and a queue that supports adding and removing items from either the
 *  front or the back of the data structure.
 *  Implemented by Circular Linked List.
 **************************************************************************** */

public class Deque<Item> implements Iterable<Item> {

    /** helper class Node. */
    private class Node {
        private Item data;
        private Node next;
        private Node prev;

        private Node(Item item, Node n, Node p) {
            data = item;
            next = n;
            prev = p;
        }
    }

    private Node sentinel;
    private int size;

    // construct an empty deque
    public Deque() {
        sentinel = new Node(null, null, null);
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (size == 0) {
            Node first = new Node(item, sentinel, sentinel);
            sentinel.next = first;
            sentinel.prev = first;
            size += 1;
        }
        else {
            Node first = new Node(item, sentinel.next, sentinel);
            sentinel.next.prev = first;
            sentinel.next = first;
            size += 1;
        }
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (size == 0) {
            Node last = new Node(item, sentinel, sentinel);
            sentinel.next = last;
            sentinel.prev = last;
            size += 1;
        }
        else {
            Node last = new Node(item, sentinel, sentinel.prev);
            sentinel.prev.next = last;
            sentinel.prev = last;
            size += 1;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }
        else if (size == 1) {
            Item first = sentinel.next.data;
            sentinel.next.data = null;
            sentinel.next = null;
            sentinel.prev = null;
            size -= 1;
            return first;
        }
        else {
            Item first = sentinel.next.data;
            sentinel.next.data = null;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
            size -= 1;
            return first;
        }
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }
        else if (size == 1) {
            Item last = sentinel.prev.data;
            sentinel.prev.data = null;
            sentinel.next = null;
            sentinel.prev = null;
            size -= 1;
            return last;
        }
        else {
            Item last = sentinel.prev.data;
            sentinel.prev.data = null;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
            size -= 1;
            return last;
        }
    }

    public Iterator<Item> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<Item> {
        private Node current;

        LinkedListIterator() {
            this.current = sentinel.next;
        }

        @Override
        public boolean hasNext() {
            return this.current != sentinel && this.current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            Item ret = this.current.data;
            this.current = this.current.next;
            return ret;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // Unit test
    public static void main(String[] args) {
        Deque<Integer> testDeque = new Deque<>();
        testDeque.addFirst(1);
        testDeque.addLast(2);
        if (!testDeque.isEmpty()) System.out.println("Deque is not empty.");
        testDeque.removeFirst();
        testDeque.removeLast();
        System.out.println("The size is " + testDeque.size());
        testDeque.addLast(3);
        testDeque.addLast(3);
        testDeque.addLast(3);
        for (Integer integer : testDeque) {
            System.out.println(integer);
        }
    }
}
