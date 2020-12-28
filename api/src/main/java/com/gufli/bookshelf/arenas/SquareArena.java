package com.gufli.bookshelf.arenas;

import com.gufli.bookshelf.entity.PlatformLocation;

public class SquareArena implements Arena {

    private final int x1, z1, x2, z2;

    public SquareArena(int x1, int z1, int x2, int z2) {
        this.x1 = x1;
        this.z1 = z1;
        this.x2 = x2;
        this.z2 = z2;
    }

    public SquareArena(PlatformLocation loc1, PlatformLocation loc2) {
        this.x1 = (int) Math.floor(Math.min(loc1.getX(), loc2.getX()));
        this.x2 = (int) Math.ceil(Math.max(loc1.getX(), loc2.getX()));
        this.z1 = (int) Math.floor(Math.min(loc1.getZ(), loc2.getZ()));
        this.z2 = (int) Math.ceil(Math.max(loc1.getZ(), loc2.getZ()));
    }

    @Override
    public boolean contains(PlatformLocation loc) {
        return x1 < loc.getX() && loc.getX() < x2 && z1 < loc.getZ() && loc.getZ() < z2;
    }
}
