package com.gufli.bookshelf.bukkit.server;

import com.gufli.bookshelf.entity.PlatformPlayer;
import com.gufli.bookshelf.server.AbstractServer;

public class BukkitServer extends AbstractServer {

    void login(PlatformPlayer player) {
        players.add(player);
    }

    void quit(PlatformPlayer player) {
        players.remove(player);
    }

}
