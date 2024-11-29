/* *****************************************************************************
 *  Name:FastCollinearPoints.java
 *  Date: 2024-11-12
 *  Description: A fast algorithm to find collinear points.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private List<LineSegment> segments = new ArrayList<>();

    /** Finds all line segments containing 4 or more points. */
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException();
            }
        }
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
        if (points.length < 4) {
            return;
        }
        points = Arrays.copyOf(points, points.length);
        Arrays.sort(points);
        Point[] temp = Arrays.copyOf(points, points.length);
        for (Point p : points) {
            // sort by slope to points[i]
            Arrays.sort(temp, p.slopeOrder());
            for (int i = 1; i < points.length; ) {
                int j = i + 1;
                while (j < points.length && p.slopeTo(temp[i]) == p.slopeTo(temp[j])) {
                    j++;
                }
                // Draw only when p is the smallest point.
                if (j - i >= 3 && temp[0].compareTo(min(temp, i, j - 1)) < 0) {
                    segments.add(new LineSegment(temp[0], max(temp, i, j - 1)));
                }
                if (j == points.length) {
                    break;
                }
                i = j;
            }
        }
    }

    /** The number of line segments. */
    public int numberOfSegments() {
        return segments.size();
    }

    /** The line segments. */
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[numberOfSegments()]);
    }

    private Point min(Point[] a, int lo, int hi) {
        if (lo > hi || a == null) {
            throw new IllegalArgumentException();
        }
        Point minPoint = a[lo];
        for (int i = lo + 1; i <= hi; i++) {
            if (minPoint.compareTo(a[i]) > 0) {
                minPoint = a[i];
            }
        }
        return minPoint;
    }

    private Point max(Point[] a, int lo, int hi) {
        if (lo > hi || a == null) {
            throw new IllegalArgumentException();
        }
        Point maxPoint = a[lo];
        for (int i = lo + 1; i <= hi; i++) {
            if (maxPoint.compareTo(a[i]) < 0) {
                maxPoint = a[i];
            }
        }
        return maxPoint;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

    }
}
