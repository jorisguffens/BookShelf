package com.gufli.bookshelf.bukkit.server;

import com.gufli.bookshelf.bukkit.BukkitShelf;
import com.gufli.bookshelf.bukkit.entity.BukkitPlayer;
import com.gufli.bookshelf.entity.PlatformPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    private final BukkitShelf shelf;

    public ConnectionListener(BukkitShelf shelf) {
        this.shelf = shelf;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(PlayerLoginEvent e) {
        if ( e.getResult() != PlayerLoginEvent.Result.ALLOWED ) {
            return;
        }

        shelf.server.login(new BukkitPlayer(e.getPlayer()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e) {
        PlatformPlayer player = shelf.server.getPlayer(e.getPlayer().getUniqueId());
        shelf.server.quit(player);
    }

}
