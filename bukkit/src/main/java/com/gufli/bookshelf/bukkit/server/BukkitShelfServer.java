package com.gufli.bookshelf.bukkit.server;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.scheduler.Scheduler;
import com.gufli.bookshelf.server.AbstractShelfServer;

public class BukkitShelfServer extends AbstractShelfServer {

    public BukkitShelfServer(Scheduler scheduler) {
        super(scheduler);
    }

    void onLogin(ShelfPlayer player) {
        this.login(player);
    }

    void onQuit(ShelfPlayer player) {
        this.quit(player);
    }

}
