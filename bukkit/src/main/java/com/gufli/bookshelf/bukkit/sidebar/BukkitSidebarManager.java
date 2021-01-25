package com.gufli.bookshelf.bukkit.sidebar;

import com.gufli.bookshelf.entity.ShelfPlayer;
import com.gufli.bookshelf.event.Events;
import com.gufli.bookshelf.events.PlayerQuitEvent;
import com.gufli.bookshelf.sidebar.Sidebar;
import com.gufli.bookshelf.sidebar.SidebarManager;
import com.gufli.bookshelf.sidebar.Sidebars;

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
    public void setSidebar(ShelfPlayer player, Sidebar sidebar) {
        removeSidebar(player);
        BukkitSidebar bs = new BukkitSidebar(player, sidebar);
        players.put(player, bs);
        bs.show();
    }

    @Override
    public Sidebar getSidebar(ShelfPlayer player) {
        return players.containsKey(player) ? players.get(player).getSidebar() : null;
    }

    @Override
    public void removeSidebar(ShelfPlayer player) {
        if ( !players.containsKey(player) ) {
            return;
        }

        BukkitSidebar bs = players.get(player);
        bs.destroy();
        players.remove(player);
    }

    @Override
    public void updateSidebar(ShelfPlayer player) {
        if ( !players.containsKey(player) ) {
            return;
        }

        players.get(player).update();
    }
}
