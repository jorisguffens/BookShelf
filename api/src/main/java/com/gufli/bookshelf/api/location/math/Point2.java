package com.gufli.bookshelf.api.location.math;

public class Point2 {

    private final double x, y;

    public Point2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public boolean isSimilar(Point2 other) {
        return this.x == other.x && this.y == other.y;
    }

}
