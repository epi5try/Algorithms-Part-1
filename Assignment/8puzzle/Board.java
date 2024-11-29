/* *****************************************************************************
 *  Name: Board.java
 *  Date: 2024-11-17
 *  Description: Immutable data type Board that models an n-by-n board with sliding tiles.
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int[][] board;
    private int size;

    private class Pair {
        private int row;
        private int col;

        public Pair(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public boolean equals(Object other) {
            if (this.getClass() != other.getClass()) return false;
            else {
                return row == ((Pair) other).row && col == ((Pair) other).col;
            }
        }
    }

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        size = tiles.length;
        board = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(size + "\n");
        for (int i = 0; i < size; i++) {
            str.append(" ");
            for (int j = 0; j < size; j++) {
                str.append(String.format("%2d ", board[i][j]));
            }
            str.append("\n");
        }
        return str.toString();
    }

    // board dimension n
    public int dimension() {
        return size;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int goal = i * size + j + 1;
                if (board[i][j] != 0 && board[i][j] != goal) {
                    hamming += 1;
                }
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int tile = board[i][j];
                if (tile == 0 || tile == i * size + j + 1) {
                    continue;
                }
                int goalRow = (tile - 1) / size;
                int goalCol = (tile - 1) % size;
                manhattan += Math.abs(goalRow - i) + Math.abs(goalCol - j);
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int goal = i * size + j + 1;
                if (board[i][j] != 0 && board[i][j] != goal) {
                    return false;
                }
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Board that = (Board) other;

        if (this.dimension() != that.size) return false;
        if (this.manhattan() != that.manhattan()) return false;
        if (that.toString().equals(this.toString())) return true;
        else return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> boards = new Queue<>();
        // find all the neighbor of empty tile.
        int zeroRow = 0;
        int zeroCol = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                    break;
                }
            }
        }
        List<Pair> neighbors = new ArrayList<>();
        neighbors.add(new Pair(zeroRow - 1, zeroCol));
        neighbors.add(new Pair(zeroRow, zeroCol - 1));
        neighbors.add(new Pair(zeroRow, zeroCol + 1));
        neighbors.add(new Pair(zeroRow + 1, zeroCol));
        for (Pair neighbor : neighbors) {
            if (inBound(neighbor.getRow(), neighbor.getCol())) {
                boards.enqueue(exch(neighbor, new Pair(zeroRow, zeroCol)));
            }
        }
        return boards;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (this.size == 1) return null;   // one-dimensional grid does not have a twin
        if (this.board == null) return null;

        boolean exchange = false;
        int[][] tmp = new int[this.size][this.size];
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (!exchange && j != this.size - 1 && this.board[i][j] != 0
                        && this.board[i][j + 1] != 0) {
                    // exchange this.grid[i][j] and this.grid[i][j+1]
                    tmp[i][j] = this.board[i][j + 1];
                    tmp[i][j + 1] = this.board[i][j];
                    j++;  // switch the next j because we have done the assignment
                    exchange = true;
                }
                else {
                    tmp[i][j] = this.board[i][j];
                }
            }
        }

        return new Board(tmp);
    }


    // exchange two tiles
    private Board exch(Pair p1, Pair p2) {
        Board copy = new Board(board);
        int row1 = p1.getRow();
        int col1 = p1.getCol();
        int row2 = p2.getRow();
        int col2 = p2.getCol();
        int temp = board[row1][col1];
        copy.board[row1][col1] = copy.board[row2][col2];
        copy.board[row2][col2] = temp;

        return copy;
    }

    // check index
    private boolean inBound(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] blocks = {
                { 1, 2, 3, 4 },
                { 5, 6, 0, 8 },
                { 9, 10, 11, 12 },
                { 13, 14, 15, 7 }
        };
        int[][] test = {
                { 1, 0 },
                { 2, 3 }
        };
        Board board = new Board(blocks);
        Board tes = new Board(test);
        for (Board board1 : board.neighbors()) {
            System.out.println(board1);
        }
        System.out.println(board.twin());
        System.out.println(board.manhattan());
        System.out.println(board.hamming());
        System.out.println(tes.hamming());
    }
}
