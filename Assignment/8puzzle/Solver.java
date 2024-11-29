/* *****************************************************************************
 *  Name: Solver.java
 *  Date: 2024-11-18
 *  Description: Solve 8puzzle problem using A*.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private class Node implements Comparable<Node> {
        private int move;
        private Board current;
        private Node prev;
        // caching priorities.
        private int priority;

        public Node(Board current, Node prev, int move) {
            this.current = current;
            this.prev = prev;
            this.move = move;
            // Use manhattan
            priority = move + current.manhattan();
        }

        public int compareTo(Node that) {
            return this.priority - that.priority;
        }
    }

    // The final node.
    private Node goal;
    private boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        Board twin = initial.twin();

        MinPQ<Node> pq = new MinPQ<>();
        MinPQ<Node> twinPq = new MinPQ<>();

        Node initialNode = new Node(initial, null, 0);
        Node twinInitial = new Node(twin, null, 0);
        pq.insert(initialNode);
        twinPq.insert(twinInitial);
        while (!pq.min().current.isGoal() && !twinPq.min().current.isGoal()) {
            Node minNode = pq.delMin();
            Node twinMin = twinPq.delMin();
            for (Board neighbor : minNode.current.neighbors()) {
                if (minNode.prev != null && neighbor.equals(minNode.prev.current)) continue;
                Node node = new Node(neighbor, minNode, minNode.move + 1);
                pq.insert(node);
            }
            for (Board neighbor : twinMin.current.neighbors()) {
                if (twinMin.prev != null && neighbor.equals(twinMin.prev.current)) continue;
                Node node = new Node(neighbor, twinMin, twinMin.move + 1);
                twinPq.insert(node);
            }
        }
        if (pq.min().current.isGoal()) {
            solvable = true;
            goal = pq.min();
        }
        else {
            solvable = false;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) {
            return -1;
        }
        return goal.move;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        Stack<Board> res = new Stack<>();
        res.push(goal.current);
        Node prev = goal.prev;
        while (prev != null) {
            res.push(prev.current);
            prev = prev.prev;
        }
        return res;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
