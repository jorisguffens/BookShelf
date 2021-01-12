package com.gufli.bookshelf.server;

import com.gufli.bookshelf.entity.ShelfPlayer;
import com.gufli.bookshelf.scheduler.Scheduler;

import java.util.UUID;

public class Bookshelf {

    private static AbstractShelfServer platformServer;

    public static void register(AbstractShelfServer server) {
        if (platformServer != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton PlatformServer.");
        }

        platformServer = server;
    }

    public static Scheduler getScheduler() {
        return platformServer.getScheduler();
    }

    public static ShelfPlayer getPlayer(UUID uuid) {
        return platformServer.getPlayer(uuid);
    }

    public static ShelfPlayer getPlayer(String name) {
        return platformServer.getPlayer(name);
    }
}
