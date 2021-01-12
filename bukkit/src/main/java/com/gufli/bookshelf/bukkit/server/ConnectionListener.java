package com.gufli.bookshelf.bukkit.server;

import com.gufli.bookshelf.bukkit.BukkitShelf;
import com.gufli.bookshelf.bukkit.entity.BukkitPlayer;
import com.gufli.bookshelf.entity.ShelfPlayer;
import com.gufli.bookshelf.events.PlayerPostLoginEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    private final BukkitShelf shelf;

    public ConnectionListener(BukkitShelf shelf) {
        this.shelf = shelf;
    }

    // LOAD PLAYER
    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(PlayerLoginEvent e) {
        if ( e.getResult() != PlayerLoginEvent.Result.ALLOWED ) {
            return;
        }

        shelf.server.onLogin(new BukkitPlayer(e.getPlayer()));
    }

    // QUIT PLAYER
    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e) {
        ShelfPlayer player = shelf.server.getPlayer(e.getPlayer().getUniqueId());
        shelf.server.onQuit(player);
    }

    // JOIN PLAYER
    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent e) {
        ShelfPlayer player = shelf.server.getPlayer(e.getPlayer().getUniqueId());
        //EventManager.dispatch(new com.gufli.bookshelf.events.PlayerJoinEvent(player));

        if ( !player.has("LOGIN_SUCCESS") ) {
            player.set("JOIN_SUCCESS", true);
            return;
        }

        //EventManager.dispatch(new PlayerPostLoginEvent(player));
    }

    // LOGIN FINISHED -> POST LOGIN (sync)
    public void onLoginInternal(com.gufli.bookshelf.events.PlayerLoginEvent e) {
        if ( !e.getPlayer().has("JOIN_SUCCESS") ) {
            e.getPlayer().set("LOGIN_SUCCESS", true);
            return;
        }

        //shelf.server.getScheduler().sync().execute(() -> EventManager.dispatch(new PlayerPostLoginEvent(e.getPlayer())));
    }

}
