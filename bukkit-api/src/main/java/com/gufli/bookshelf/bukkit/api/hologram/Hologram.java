package com.gufli.bookshelf.bukkit.api.hologram;

import com.gufli.bookshelf.api.location.ShelfLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Hologram {

    private ShelfLocation location;
    private List<String> lines;

    public Hologram(ShelfLocation location, List<String> lines) {
        this.location = location;
        this.lines = lines;
    }

    public Hologram(ShelfLocation location) {
        this(location, new ArrayList<>());
    }

    public void changeLines(List<String> lines) {
        this.lines = lines;
    }

    public void addLines(String... lines) {
        this.lines.addAll(Arrays.asList(lines));
    }

    public void clearLines() {
        this.lines.clear();
    }

    public void changeLocation(ShelfLocation location) {
        this.location = location;
    }

    public List<String> lines() {
        return Collections.unmodifiableList(lines);
    }

    public ShelfLocation location() {
        return location;
    }

}
