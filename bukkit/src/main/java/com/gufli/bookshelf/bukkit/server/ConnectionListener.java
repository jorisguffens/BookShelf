package com.gufli.bookshelf.bukkit.server;

import com.gufli.bookshelf.bukkit.BukkitShelf;
import com.gufli.bookshelf.bukkit.entity.BukkitPlayer;
import com.gufli.bookshelf.entity.PlatformPlayer;
import com.gufli.bookshelf.events.Event;
import com.gufli.bookshelf.events.EventListener;
import com.gufli.bookshelf.events.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements EventListener {

    private final BukkitShelf shelf;

    public ConnectionListener(BukkitShelf shelf) {
        this.shelf = shelf;
    }

    @Event(priority = EventPriority.MONITOR)
    public void onLogin(PlayerLoginEvent e) {
        if ( e.getResult() != PlayerLoginEvent.Result.ALLOWED ) {
            return;
        }

        shelf.server.login(new BukkitPlayer(e.getPlayer()));
    }

    @Event(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e) {
        PlatformPlayer player = shelf.server.getPlayer(e.getPlayer().getUniqueId());
        shelf.server.quit(player);
    }

}
