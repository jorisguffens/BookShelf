package com.gufli.bookshelf.bukkit.sidebar;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.event.Events;
import com.gufli.bookshelf.api.events.PlayerQuitEvent;
import com.gufli.bookshelf.api.events.ShelfShutdownEvent;
import com.gufli.bookshelf.api.scheduler.SchedulerTask;
import com.gufli.bookshelf.api.server.Bookshelf;
import com.gufli.bookshelf.api.sidebar.Sidebar;
import com.gufli.bookshelf.api.sidebar.SidebarManager;
import com.gufli.bookshelf.api.sidebar.Sidebars;
import com.gufli.bookshelf.bukkit.api.entity.BukkitPlayer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BukkitSidebarManager implements SidebarManager {

    private final Map<ShelfPlayer, BukkitSidebar> players = new HashMap<>();

    public BukkitSidebarManager() {
        Sidebars.register(this);

        Events.subscribe(PlayerQuitEvent.class)
                .handler(e -> removeSidebar(e.getPlayer()));

        SchedulerTask task = Bookshelf.scheduler()
                .asyncRepeating(this::update, 50, TimeUnit.MILLISECONDS);

        Events.subscribe(ShelfShutdownEvent.class)
                .handler(e -> {
                    task.cancel();
                    new HashSet<>(players.keySet()).forEach(this::removeSidebar);
                });
    }

    private void update() {
        players.keySet().forEach(this::updateSidebar);
    }

    @Override
    public void changeSidebar(ShelfPlayer player, Sidebar sidebar) {
        BukkitPlayer bp = (BukkitPlayer) player;

        removeSidebar(bp);
        BukkitSidebar bs = new BukkitSidebar(bp, sidebar);
        bs.show();

        players.put(bp, bs);
    }

    @Override
    public void removeSidebar(ShelfPlayer player) {
        if (!players.containsKey(player)) {
            return;
        }

        BukkitSidebar bs = players.remove(player);
        bs.destroy();
    }

    @Override
    public void updateSidebar(ShelfPlayer player) {
        if (!players.containsKey(player)) {
            return;
        }

        players.get(player).update();
    }

    @Override
    public void refresh(ShelfPlayer player) {
        if (!players.containsKey(player)) {
            return;
        }

        BukkitSidebar bs = players.get(player);
        bs.destroy();
        bs.show();
    }

    @Override
    public void refresh() {
        players.keySet().forEach(this::refresh);
    }
}
