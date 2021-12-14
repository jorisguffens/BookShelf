package com.gufli.bookshelf.api.server;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.scheduler.Scheduler;

import java.util.UUID;

public class Bookshelf {

    private static ShelfServer shelfServer;

    public static void register(ShelfServer server) {
        if (shelfServer != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton shelfServer.");
        }

        shelfServer = server;
    }

    public static Scheduler getScheduler() {
        return shelfServer.getScheduler();
    }

    public static ShelfPlayer getPlayer(UUID uuid) {
        return shelfServer.getPlayer(uuid);
    }

    public static ShelfPlayer getPlayer(String name) {
        return shelfServer.getPlayer(name);
    }
}
