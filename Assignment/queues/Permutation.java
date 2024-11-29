/* *****************************************************************************
 *  Name: Permutation
 *  Date: 2024-11-06
 *  Description: This client program takes an integer k as a command-line
 *  argument; reads a sequence of strings from standard input; and prints exactly k
 *  of them, uniformly at random. Print each item from the sequence at most once.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Permutation k(a integer)");
        }
        else {
            // make sure that k <= n;
            int k = Integer.parseInt(args[0]);
            RandomizedQueue<String> rq = new RandomizedQueue<>();
            while (!StdIn.isEmpty()) {
                rq.enqueue(StdIn.readString());
            }
            for (int i = 0; i < k; i++) {
                System.out.println(rq.dequeue());
            }
        }
    }
}
