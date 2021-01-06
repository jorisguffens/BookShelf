package com.gufli.bookshelf.bukkit.nametags;

import com.gufli.bookshelf.bukkit.BukkitShelf;
import com.gufli.bookshelf.server.Shelf;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    private final BukkitShelf plugin;
    private final BukkitNametagHandler nametagHandler;

    public ConnectionListener(BukkitShelf plugin, BukkitNametagHandler nametagHandler) {
        this.plugin = plugin;
        this.nametagHandler = nametagHandler;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            nametagHandler.showAll(event.getPlayer());
        }, 1L);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        nametagHandler.clear(Shelf.getPlayer(e.getPlayer().getUniqueId()));
    }

}
