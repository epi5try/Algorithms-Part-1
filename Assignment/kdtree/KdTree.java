/* *****************************************************************************
 *  Name: KdTree.java
 *  Date: 2024-11-28
 *  Description: A mutable data type that uses a 2d-tree to implement the same API.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private Node root;
    private int size;
    // for nearest method
    private double minDist;
    private Point2D nearestPoint;

    private static final boolean X = true;
    private static final boolean Y = false;

    private static class Node {
        private Point2D p; // the point
        private RectHV rect; // the axis-aligned rectangle corresponding to this node.
        private Node lb; // the left/bottom subtree.
        private Node rt; // the right/top subtree.

        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }

    public KdTree() {
        this.root = null;
        size = 0;
    }

    // is the tree empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of roots in this tree.
    public int size() {
        return this.size;
    }

    // add a node to this tree.
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        root = insert(p, root, X, new RectHV(0, 0, 1, 1));
        size = size + 1;
    }

    private Node insert(Point2D p, Node x, boolean dim, RectHV rect) {
        if (x == null) {
            return new Node(p, rect);
        }
        if (dim) {
            int cmp = Double.compare(p.x(), x.p.x());
            if (cmp < 0) {
                x.lb = insert(p, x.lb, Y,
                              new RectHV(rect.xmin(), rect.ymin(), x.p.x(), rect.ymax()));
            }
            else if (cmp > 0) {
                x.rt = insert(p, x.rt, Y,
                              new RectHV(x.p.x(), rect.ymin(), rect.xmax(), rect.ymax()));
            }
            else {
                if (p.y() == x.p.y()) {
                    return x;
                }
                else {
                    x.rt = insert(p, x.rt, Y,
                                  new RectHV(x.p.x(), rect.ymin(), rect.xmax(), rect.ymax()));
                }
            }
        }
        else {
            int cmp = Double.compare(p.y(), x.p.y());
            if (cmp < 0) {
                x.lb = insert(p, x.lb, X,
                              new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), x.p.y()));
            }
            else if (cmp > 0) {
                x.rt = insert(p, x.rt, X,
                              new RectHV(rect.xmin(), x.p.y(), rect.xmax(), rect.ymax()));
            }
            else {
                if (p.x() == x.p.x()) {
                    return x;
                }
                else {
                    x.rt = insert(p, x.rt, X,
                                  new RectHV(rect.xmin(), x.p.y(), rect.xmax(), rect.ymax()));
                }
            }
        }
        return x;
    }

    // does the tree contains point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return false;
        }
        return contains(p, root, X);
    }

    private boolean contains(Point2D p, Node x, boolean dim) {
        if (x == null) {
            return false;
        }
        if (x.p.equals(p)) {
            return true;
        }
        if (dim) {
            int cmp = Double.compare(p.x(), x.p.x());
            if (cmp < 0) {
                return contains(p, x.lb, Y);
            }
            else {
                return contains(p, x.rt, Y);
            }
        }
        else {
            int cmp = Double.compare(p.y(), x.p.y());
            if (cmp < 0) {
                return contains(p, x.lb, X);
            }
            else {
                return contains(p, x.rt, X);
            }
        }
    }

    // draw all the points and corresponding partition to standard draw.
    public void draw() {
        draw(root, X);
    }

    private void draw(Node x, boolean dim) {
        if (x == null)
            return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.p.draw();
        if (dim) {
            // vertical
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.01);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(0.01);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }
        draw(x.lb, !dim);
        draw(x.rt, !dim);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        List<Point2D> points = new ArrayList<>();
        rangeHelper(rect, root, X, points);
        return points;
    }

    private void rangeHelper(RectHV rect, Node x, boolean dim, List<Point2D> points) {
        if (x == null) {
            return;
        }
        if (rect.contains(x.p)) {
            points.add(x.p);
        }
        if (dim) {
            if (rect.xmax() < x.p.x()) {
                rangeHelper(rect, x.lb, !dim, points);
            }
            else if (rect.xmin() > x.p.x()) {
                rangeHelper(rect, x.rt, !dim, points);
            }
            else {
                rangeHelper(rect, x.lb, !dim, points);
                rangeHelper(rect, x.rt, !dim, points);
            }
        }
        else {
            if (rect.ymax() < x.p.y()) {
                rangeHelper(rect, x.lb, !dim, points);
            }
            else if (rect.ymin() > x.p.y()) {
                rangeHelper(rect, x.rt, !dim, points);
            }
            else {
                rangeHelper(rect, x.lb, !dim, points);
                rangeHelper(rect, x.rt, !dim, points);
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        minDist = Double.MAX_VALUE;
        nearestPoint = null;
        nearestHelper(p, root, X);
        return nearestPoint;
    }

    // recursively search each subtree if there could exist a nearest point.
    private void nearestHelper(Point2D p, Node x, boolean dim) {
        if (x == null) {
            return;
        }
        if (x.p.distanceTo(p) < minDist) {
            minDist = x.p.distanceTo(p);
            nearestPoint = x.p;
        }
        if (dim) {
            if (p.x() < x.p.x()) {
                nearestHelper(p, x.lb, !dim);
                if (Math.abs(p.x() - x.p.x()) < minDist) {
                    nearestHelper(p, x.rt, !dim);
                }
            }
            else {
                nearestHelper(p, x.rt, !dim);
                if (Math.abs(p.x() - x.p.x()) < minDist) {
                    nearestHelper(p, x.lb, !dim);
                }
            }
        }
        else {
            if (p.y() < x.p.y()) {
                nearestHelper(p, x.lb, !dim);
                if (Math.abs(p.y() - x.p.y()) < minDist) {
                    nearestHelper(p, x.rt, !dim);
                }
            }
            else {
                nearestHelper(p, x.rt, !dim);
                if (Math.abs(p.y() - x.p.y()) < minDist) {
                    nearestHelper(p, x.lb, !dim);
                }
            }
        }
    }

    public static void main(String[] args) {
        // initialize the two data structures with point from file
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        Point2D query = new Point2D(0.53, 0.78);
        StdDraw.setPenColor(StdDraw.BLUE);
        kdtree.nearest(query).draw();
    }
}
