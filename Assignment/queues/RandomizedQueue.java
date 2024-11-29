import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;
/* *****************************************************************************
 *  Name: RandomizedQueue
 *  Date: 2024-11-06
 *  Description: A randomized queue is similar to a stack or queue,
 *  except that the item removed is chosen uniformly at random among items in the data structure
 **************************************************************************** */

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int N;


    // construct an empty randomized queue
    public RandomizedQueue() {
        N = 0;
        items = (Item[]) new Object[10];
    }

    // swap two element in the array
    private void swap(Item[] s, int i, int j) {
        Item temp = s[i];
        s[i] = s[j];
        s[j] = temp;
    }

    // resize method
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            copy[i] = items[i];
        }
        items = copy;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return N;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (N == items.length) {
            resize(N * 2);
        }
        items[N++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int index = StdRandom.uniformInt(N);
        Item item = items[index];
        swap(items, index, N - 1);
        items[N - 1] = null;
        N = N - 1;
        if (N > 0 && N == items.length / 4) {
            resize(items.length / 2);
        }
        return item;
    }


    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        int index = StdRandom.uniformInt(N);
        return items[index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int i = 0;
        private int max = N;
        private Item[] copy = (Item[]) new Object[max];

        public RandomizedQueueIterator() {
            for (int i = 0; i < max; i++) {
                copy[i] = items[i];
            }
            StdRandom.shuffle(copy);
        }

        @Override
        public boolean hasNext() {
            return i < max;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = copy[i];
            i = i + 1;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        int n = 5;
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < n; i++)
            queue.enqueue(i);
        for (int a : queue) {
            for (int b : queue)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }
    }
}
