package com.gufli.bookshelf.api.location.math;

import java.util.ArrayList;
import java.util.List;

public class Contour2 {

    private final List<Vector2> vectors = new ArrayList<>();

    public int size() {
        return vectors.size();
    }

    public List<Vector2> lines() {
        return vectors;
    }

    public boolean isClosed() {
        int size = vectors.size();
        Vector2 start = vectors.get(0);
        Vector2 end = vectors.get(size - 1);
        return start.start().isSimilar(end.end());
    }

    public boolean canAdd(Vector2 vector) {
        if (vectors.isEmpty()) {
            return true;
        }

        Vector2 end = vectors.get(vectors.size() - 1);
        return vector.start().isSimilar(end.end());
    }

    public void add(Vector2 vector) {
        if (!canAdd(vector)) {
            throw new IllegalArgumentException("Cannot add this vector to this contour.");
        }

        vectors.add(vector);
    }

    public boolean isSimilar(Contour2 other) {
        if (other.size() != this.size())
            return false;

        List<Vector2> available = new ArrayList<>(vectors);
        for (Vector2 ov : other.vectors) {
            available.removeIf(ov::isSimilar);
        }

        return available.isEmpty();
    }

}