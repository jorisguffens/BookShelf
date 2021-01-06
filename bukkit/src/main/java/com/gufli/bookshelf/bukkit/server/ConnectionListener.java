package com.gufli.bookshelf.bukkit.server;

import com.gufli.bookshelf.bukkit.BukkitShelf;
import com.gufli.bookshelf.bukkit.entity.BukkitPlayer;
import com.gufli.bookshelf.entity.ShelfPlayer;
import com.gufli.bookshelf.events.Event;
import com.gufli.bookshelf.events.EventListener;
import com.gufli.bookshelf.events.EventManager;
import com.gufli.bookshelf.events.EventPriority;
import com.gufli.bookshelf.events.defaults.PlayerPostLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements EventListener {

    private final BukkitShelf shelf;

    public ConnectionListener(BukkitShelf shelf) {
        this.shelf = shelf;
    }

    // LOAD PLAYER
    @Event(priority = EventPriority.MONITOR)
    public void onLogin(PlayerLoginEvent e) {
        if ( e.getResult() != PlayerLoginEvent.Result.ALLOWED ) {
            return;
        }

        shelf.server.onLogin(new BukkitPlayer(e.getPlayer()));
    }

    // QUIT PLAYER
    @Event(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e) {
        ShelfPlayer player = shelf.server.getPlayer(e.getPlayer().getUniqueId());
        shelf.server.onQuit(player);
    }

    // JOIN PLAYER
    @Event(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent e) {
        ShelfPlayer player = shelf.server.getPlayer(e.getPlayer().getUniqueId());
        EventManager.dispatch(new com.gufli.bookshelf.events.defaults.PlayerJoinEvent(player));

        if ( !player.has("LOGIN_SUCCESS") ) {
            player.set("JOIN_SUCCESS", true);
            return;
        }

        EventManager.dispatch(new PlayerPostLoginEvent(player));
    }

    // LOGIN FINISHED -> POST LOGIN (sync)
    @Event(priority = EventPriority.MONITOR)
    public void onLoginInternal(com.gufli.bookshelf.events.defaults.PlayerLoginEvent e) {
        if ( !e.getPlayer().has("JOIN_SUCCESS") ) {
            e.getPlayer().set("LOGIN_SUCCESS", true);
            return;
        }

        shelf.server.getScheduler().sync().execute(() ->
                EventManager.dispatch(new PlayerPostLoginEvent(e.getPlayer())));
    }

}
