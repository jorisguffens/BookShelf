package com.gufli.bookshelf.bukkit;

import com.gufli.bookshelf.bukkit.event.BukkitEventInjector;
import com.gufli.bookshelf.bukkit.gui.InventoryListener;
import com.gufli.bookshelf.bukkit.server.BukkitShelfServer;
import com.gufli.bookshelf.bukkit.server.ConnectionListener;
import com.gufli.bookshelf.event.EventInjector;
import com.gufli.bookshelf.server.Bookshelf;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitShelf extends JavaPlugin {

    public final BukkitShelfServer server;

    public BukkitShelf() {
        this.server = new BukkitShelfServer(new BukkitScheduler(this));
        Bookshelf.register(this.server);
    }

    @Override
    public void onEnable() {

        // inject bukkit events
        BukkitEventInjector injector = new BukkitEventInjector(this);
        EventInjector.register(injector);

        // Register default events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ConnectionListener(this), this);
        pm.registerEvents(new InventoryListener(), this);

        getLogger().info("Enabled " + getDescription().getName() + " v" + getDescription().getVersion() + ".");
    }

}
