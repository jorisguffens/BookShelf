package com.gufli.bookshelf.bukkit.server;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.event.Events;
import com.gufli.bookshelf.api.events.PlayerPostLoginEvent;
import com.gufli.bookshelf.bukkit.BukkitShelf;
import com.gufli.bookshelf.bukkit.api.entity.BukkitPlayer;
import org.bukkit.entity.Player;
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

        for ( Player player : shelf.getServer().getOnlinePlayers() ) {
            shelf.server().onLogin(new BukkitPlayer(player));
        }

        Events.subscribe(com.gufli.bookshelf.api.events.PlayerLoginEvent.class)
                .handler(this::onLoginInternal);
    }

    // LOAD PLAYER
    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(PlayerLoginEvent e) {
        if (e.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }

        shelf.server().onLogin(new BukkitPlayer(e.getPlayer()));
    }

    // QUIT PLAYER
    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e) {
        ShelfPlayer player = shelf.server().playerById(e.getPlayer().getUniqueId());
        shelf.server().onQuit(player);
    }

    // JOIN PLAYER
    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent e) {
        ShelfPlayer player = shelf.server().playerById(e.getPlayer().getUniqueId());

        if (!player.has("LOGIN_SUCCESS")) {
            player.set("JOIN_SUCCESS", true);
            return;
        }

        Events.call(new PlayerPostLoginEvent(player));
    }

    // LOGIN FINISHED -> POST LOGIN (sync)
    public void onLoginInternal(com.gufli.bookshelf.api.events.PlayerLoginEvent e) {
        if (!e.player().has("JOIN_SUCCESS")) {
            e.player().set("LOGIN_SUCCESS", true);
            return;
        }

        Events.call(new PlayerPostLoginEvent(e.player()));
    }

}
