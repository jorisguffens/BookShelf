package com.gufli.bookshelf.bukkit.server;

import com.gufli.bookshelf.entity.PlatformPlayer;
import com.gufli.bookshelf.server.PlatformServer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BukkitServer implements PlatformServer {

    private final Set<PlatformPlayer> players = new HashSet<>();

    @Override
    public PlatformPlayer getPlayer(UUID uuid) {
        return null;
    }

    @Override
    public PlatformPlayer getPlayer(String name) {
        return null;
    }


    //

    void login(PlatformPlayer player) {
        players.add(player);
    }

    void quit(PlatformPlayer player) {
        players.remove(player);
    }
}
