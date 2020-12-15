package com.gufli.bookshelf.bukkit;

import com.gufli.bookshelf.bukkit.events.DefaultEventListener;
import com.gufli.bookshelf.bukkit.gui.InventoryListener;
import com.gufli.bookshelf.bukkit.server.BukkitServer;
import com.gufli.bookshelf.bukkit.server.ConnectionListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitShelf extends JavaPlugin {

    public final BukkitServer server;

    public BukkitShelf() {
        this.server = new BukkitServer();
    }

    @Override
    public void onEnable() {

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ConnectionListener(this), this);
        pm.registerEvents(new DefaultEventListener(), this);
        pm.registerEvents(new InventoryListener(), this);

        getLogger().info("Enabled " + getDescription().getName() + " v" + getDescription().getVersion() + ".");
    }

}
