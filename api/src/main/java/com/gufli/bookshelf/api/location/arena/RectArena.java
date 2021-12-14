package com.gufli.bookshelf.api.location.arena;

import com.gufli.bookshelf.api.location.ShelfLocation;
import com.gufli.bookshelf.api.location.math.Point2;

public class RectArena implements Arena {

    private final Point2 lower, upper;

    public RectArena(Point2 lower, Point2 upper) {
        if ( lower.x() > upper.x() || lower.y() > upper.y() ) {
            throw new IllegalArgumentException("Invalid lower and upper points.");
        }
        this.lower = lower;
        this.upper = upper;
    }

    public RectArena(double x1, double z1, double x2, double z2) {
        this(new Point2(x1, z1), new Point2(x2, z2));
    }

    public RectArena(ShelfLocation loc1, ShelfLocation loc2) {
        this(loc1.x(), loc1.z(), loc2.x(), loc2.z());
    }

    @Override
    public boolean contains(ShelfLocation loc) {
        return lower.x() < loc.x() && loc.x() < upper.x()
                && lower.y() < loc.z() && loc.z() < upper.y();
    }
}
