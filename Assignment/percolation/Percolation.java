import edu.princeton.cs.algs4.WeightedQuickUnionUF;
/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

public class Percolation {
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufForFull;
    private int gridWidth;
    private boolean[][] grid;
    private int virtualTop;
    private int virtualBottom;

    private int[] dx = { -1, 1, 0, 0 };
    private int[] dy = { 0, 0, -1, 1 };

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        grid = new boolean[n][n];
        gridWidth = n;
        virtualTop = n * n;
        virtualBottom = n * n + 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = false;
            }
        }

        // Two virtual sites: n * n for top, n * n + 1 for bottom.
        uf = new WeightedQuickUnionUF(n * n + 2);
        ufForFull = new WeightedQuickUnionUF(n * n + 1);
        for (int i = 0; i < n; i++) {
            uf.union(i, virtualTop);
            ufForFull.union(i, virtualTop);
            uf.union(n * (n - 1) + i, virtualBottom);
        }
    }

    // 2-D coordinates to 1-D uf index.
    private int xyTo1D(int i, int j) {
        return (i - 1) * gridWidth + j - 1;
    }

    // Help method for validation process.
    private boolean validIndex(int x, int y) {
        return x >= 1 && x <= gridWidth && y >= 1 && y <= gridWidth;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!validIndex(row, col)) {
            throw new IllegalArgumentException();
        }
        grid[row - 1][col - 1] = true;
        for (int i = 0; i < 4; ++i) {
            int nearCol = col + dx[i];
            int nearRow = row + dy[i];
            if (validIndex(nearCol, nearRow) && grid[nearRow - 1][nearCol - 1]) {
                int near = xyTo1D(nearRow, nearCol);
                int cur = xyTo1D(row, col);
                uf.union(near, cur);
                ufForFull.union(near, cur);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!validIndex(row, col)) {
            throw new IllegalArgumentException();
        }
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!validIndex(row, col)) {
            throw new IllegalArgumentException();
        }
        int x = xyTo1D(row, col);
        return (ufForFull.find(x) == ufForFull.find(virtualTop)) && isOpen(row, col);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int numberOfOpenSites = 0;
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridWidth; j++) {
                if (grid[i][j]) {
                    numberOfOpenSites += 1;
                }
            }
        }
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return (isOpen(1, 1) && gridWidth == 1) || (uf.find(virtualTop) == uf.find(virtualBottom)
                && gridWidth != 1);
    }

    // test client (optional)
    public static void main(String[] args) {

    }
}
