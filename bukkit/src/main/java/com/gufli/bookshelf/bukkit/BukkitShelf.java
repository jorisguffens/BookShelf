package com.gufli.bookshelf.bukkit;

import com.gufli.bookshelf.animation.AnimationTest;
import com.gufli.bookshelf.api.event.Events;
import com.gufli.bookshelf.api.server.Bookshelf;
import com.gufli.bookshelf.bukkit.bossbar.BukkitBossbarManager;
import com.gufli.bookshelf.bukkit.color.TextColorMapper;
import com.gufli.bookshelf.bukkit.command.BukkitCommandExecutor;
import com.gufli.bookshelf.bukkit.commands.BookshelfBossbarCommand;
import com.gufli.bookshelf.bukkit.commands.BookshelfMenuCommand;
import com.gufli.bookshelf.bukkit.event.BukkitEventHook;
import com.gufli.bookshelf.bukkit.events.MenuListener;
import com.gufli.bookshelf.bukkit.events.PlayerAttackEventListener;
import com.gufli.bookshelf.bukkit.events.PlayerDeathEventListener;
import com.gufli.bookshelf.bukkit.nametags.BukkitNametagManager;
import com.gufli.bookshelf.bukkit.server.BukkitShelfServer;
import com.gufli.bookshelf.bukkit.server.ConnectionListener;
import com.gufli.bookshelf.bukkit.sidebar.BukkitSidebarManager;
import com.gufli.bookshelf.bukkit.titles.BukkitTitleManager;
import com.gufli.bookshelf.commands.BookshelfCommandGroup;
import com.gufli.bookshelf.event.SimpleEventManager;
import org.bukkit.command.PluginCommand;
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
        // register COMMON stuff
        Events.register(new SimpleEventManager());

        // register bukkit events
        BukkitEventHook injector = new BukkitEventHook(this);
        Events.registerEventHook(injector);

        // Register default events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ConnectionListener(this), this);
        pm.registerEvents(new MenuListener(), this);
        pm.registerEvents(new PlayerAttackEventListener(), this);
        pm.registerEvents(new PlayerDeathEventListener(), this);

        // Load manager imlementations
        if ( getServer().getPluginManager().isPluginEnabled("ProtocolLib") ) {
            new BukkitNametagManager();
            new BukkitSidebarManager();
            new BukkitTitleManager();
            new BukkitBossbarManager();
        }

        // other implementations
        new TextColorMapper();

        // commands
        PluginCommand rootcmd = getCommand("bookshelf");
        rootcmd.setExecutor(new BukkitCommandExecutor(BookshelfCommandGroup.INSTANCE));
        BookshelfCommandGroup.INSTANCE.add(new BookshelfBossbarCommand());
        BookshelfCommandGroup.INSTANCE.add(new BookshelfMenuCommand());

        AnimationTest.test();

        getLogger().info("Enabled " + getDescription().getName() + " v" + getDescription().getVersion() + ".");
    }

}
