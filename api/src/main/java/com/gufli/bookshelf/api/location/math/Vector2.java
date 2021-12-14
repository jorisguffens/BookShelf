package com.gufli.bookshelf.api.location.math;

public class Vector2 {

    private final Point2 start, end;

    public Vector2(Point2 start, Point2 end) {
        this.start = start;
        this.end = end;
    }

    public final Point2 start() {
        return start;
    }

    public final Point2 end() {
        return end;
    }

    public boolean isSimilar(Vector2 other) {
        return start.isSimilar(other.start) && end.isSimilar(other.end);
    }

}