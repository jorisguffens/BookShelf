package com.gufli.bookshelf.server;

import com.gufli.bookshelf.entity.PlatformPlayer;

import java.util.UUID;

public class Server {

    private static AbstractServer platformServer;

    public static void register(AbstractServer server) {
        if (platformServer != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton PlatformServer.");
        }

        platformServer = server;
    }

    public static PlatformPlayer getPlayer(UUID uuid) {
        return platformServer.getPlayer(uuid);
    }

    public static PlatformPlayer getPlayer(String name) {
        return platformServer.getPlayer(name);
    }
}
