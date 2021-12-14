package com.gufli.bookshelf.api.location.arena;

import com.gufli.bookshelf.api.location.ShelfLocation;

public class WorldArena implements Arena {

    private final String worldId;

    public WorldArena(String worldId) {
        this.worldId = worldId;
    }

    @Override
    public boolean contains(ShelfLocation loc) {
        return loc.worldId().equals(worldId);
    }

    public String worldId() {
        return worldId;
    }
}
