/* *****************************************************************************
 *  Name: PointSET.java
 *  Date: 2024-11-28
 *  Description: A brute force implementation of 2d range search.
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.Set;
import java.util.TreeSet;

public class PointSET {
    private Set<Point2D> points;
    private int size;

    // construct an empty set of points
    public PointSET() {
        points = new TreeSet<>();
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return this.size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (!points.contains(p)) {
            points.add(p);
            size = size + 1;
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : points) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        Set<Point2D> res = new TreeSet<>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                res.add(p);
            }
        }
        return res;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return null;
        }
        Point2D near = null;
        double minDist = Double.POSITIVE_INFINITY;
        for (Point2D point : points) {
            if (point.distanceTo(p) < minDist) {
                minDist = point.distanceTo(p);
                near = point;
            }
        }
        return near;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
