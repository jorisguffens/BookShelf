package com.gufli.bookshelf.api.server;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.scheduler.Scheduler;

import java.util.Collection;
import java.util.UUID;

public class Bookshelf {

    private static ShelfServer shelfServer;

    public static void register(ShelfServer server) {
        if (shelfServer != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton shelfServer.");
        }

        shelfServer = server;
    }

    public static Scheduler scheduler() {
        return shelfServer.scheduler();
    }

    public static ShelfPlayer playerById(UUID uuid) {
        return shelfServer.playerById(uuid);
    }

    public static ShelfPlayer playerByName(String name) {
        return shelfServer.playerByName(name);
    }

    public static Collection<ShelfPlayer> players() {
        return shelfServer.players();
    }
}
