package com.gufli.bookshelf.api.location.region;

import com.gufli.bookshelf.api.location.ShelfLocation;

import java.util.UUID;

public class WorldRegion implements Region {

    private final UUID worldId;

    public WorldRegion(UUID worldId) {
        this.worldId = worldId;
    }

    @Override
    public boolean contains(ShelfLocation loc) {
        return loc.worldId().equals(worldId);
    }

    public UUID worldId() {
        return worldId;
    }
}
