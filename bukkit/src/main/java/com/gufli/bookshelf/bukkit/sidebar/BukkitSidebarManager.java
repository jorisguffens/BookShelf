package com.gufli.bookshelf.bukkit.sidebar;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.sidebar.SidebarManager;
import com.gufli.bookshelf.api.sidebar.SidebarTemplate;
import com.gufli.bookshelf.api.sidebar.Sidebars;
import com.gufli.bookshelf.bukkit.api.entity.BukkitPlayer;
import com.gufli.bookshelf.api.event.Events;
import com.gufli.bookshelf.api.events.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class BukkitSidebarManager implements SidebarManager {

    private final Map<ShelfPlayer, BukkitSidebar> players = new HashMap<>();

    public BukkitSidebarManager() {
        Sidebars.register(this);

        Events.subscribe(PlayerQuitEvent.class)
                .handler(e -> removeSidebar(e.getPlayer()));
    }

    @Override
    public void setSidebar(ShelfPlayer player, SidebarTemplate sidebar) {
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
