package com.gufli.bookshelf.bukkit;

import com.gufli.bookshelf.api.event.Events;
import com.gufli.bookshelf.api.events.ShelfShutdownEvent;
import com.gufli.bookshelf.api.server.Bookshelf;
import com.gufli.bookshelf.bukkit.bossbar.BukkitBossbarManager;
import com.gufli.bookshelf.bukkit.chat.ChatListener;
import com.gufli.bookshelf.bukkit.color.BukkitTextColorMapper;
import com.gufli.bookshelf.bukkit.api.command.BukkitCommandExecutor;
import com.gufli.bookshelf.bukkit.commands.BookshelfBossbarAnimatedCommand;
import com.gufli.bookshelf.bukkit.commands.BookshelfBossbarCommand;
import com.gufli.bookshelf.bukkit.commands.BookshelfHologramCommand;
import com.gufli.bookshelf.bukkit.commands.BookshelfMenuCommand;
import com.gufli.bookshelf.bukkit.event.BukkitEventHook;
import com.gufli.bookshelf.bukkit.hologram.BukkitHologramManager;
import com.gufli.bookshelf.bukkit.item.BukkitItemSerializer;
import com.gufli.bookshelf.bukkit.listeners.InventoryMenuListener;
import com.gufli.bookshelf.bukkit.listeners.PlayerAttackEventListener;
import com.gufli.bookshelf.bukkit.listeners.PlayerDeathEventListener;
import com.gufli.bookshelf.bukkit.listeners.PlayerMoveListener;
import com.gufli.bookshelf.bukkit.nametags.BukkitNametagManager;
import com.gufli.bookshelf.bukkit.server.BukkitShelfServer;
import com.gufli.bookshelf.bukkit.server.ConnectionListener;
import com.gufli.bookshelf.bukkit.sidebar.BukkitSidebarManager;
import com.gufli.bookshelf.bukkit.titles.BukkitTitleManager;
import com.gufli.bookshelf.chat.SimpleChatManager;
import com.gufli.bookshelf.commands.BookshelfCommandGroup;
import com.gufli.bookshelf.event.SimpleEventManager;
import com.gufli.bookshelf.placeholders.SimplePlaceholderManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class BukkitShelf extends JavaPlugin {

    private BukkitScheduler scheduler;
    private BukkitShelfServer server;


    private static BukkitShelf instance;
    public static BukkitShelf plugin() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.scheduler = new BukkitScheduler(this);

        // Startup server
        this.server = new BukkitShelfServer(scheduler);
        Bookshelf.register(this.server);

        // Initialize and setup event system
        Events.register(new SimpleEventManager());
        BukkitEventHook injector = new BukkitEventHook(this);
        Events.registerEventHook(injector);

        // Common stuff
        new SimplePlaceholderManager();
        new SimpleChatManager();

        // Register default events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ConnectionListener(this), this);
        pm.registerEvents(new InventoryMenuListener(), this);
        pm.registerEvents(new PlayerAttackEventListener(), this);
        pm.registerEvents(new PlayerDeathEventListener(), this);
        pm.registerEvents(new ChatListener(), this);
        pm.registerEvents(new PlayerMoveListener(), this);

        // Bukkit manager imlementations
        new BukkitTextColorMapper();
        new BukkitItemSerializer();

        // Protocollib required
        if ( getServer().getPluginManager().isPluginEnabled("ProtocolLib") ) {
            new BukkitNametagManager();
            new BukkitSidebarManager();
            new BukkitTitleManager();
            new BukkitBossbarManager();
            new BukkitHologramManager();
        }

        // commands
        PluginCommand rootcmd = Objects.requireNonNull(getCommand("bookshelf"));
        BookshelfCommandGroup bcg = new BookshelfCommandGroup();
        rootcmd.setExecutor(new BukkitCommandExecutor(bcg));

        bcg.add(new BookshelfBossbarCommand());
        bcg.add(new BookshelfBossbarAnimatedCommand());
        bcg.add(new BookshelfMenuCommand());
        bcg.add(new BookshelfHologramCommand());

        getLogger().info("Enabled " + getDescription().getName() + " v" + getDescription().getVersion() + ".");
    }

    @EventHandler
    public void onDisable() {
        this.scheduler.shutdown();
        Events.call(new ShelfShutdownEvent());
    }

    public BukkitShelfServer server() {
        return server;
    }

}
