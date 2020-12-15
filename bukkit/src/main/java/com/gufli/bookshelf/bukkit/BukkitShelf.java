package com.gufli.bookshelf.bukkit;

import com.gufli.bookshelf.bukkit.server.BukkitServer;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitShelf extends JavaPlugin {

    public final BukkitServer server;

    public BukkitShelf() {
        this.server = new BukkitServer();
    }

    @Override
    public void onEnable() {

    }

}
