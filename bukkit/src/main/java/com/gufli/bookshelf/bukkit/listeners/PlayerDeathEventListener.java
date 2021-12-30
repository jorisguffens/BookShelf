package com.gufli.bookshelf.bukkit.listeners;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.event.Events;
import com.gufli.bookshelf.api.events.PlayerDeathEvent;
import com.gufli.bookshelf.api.server.Bookshelf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerDeathEventListener implements Listener {

    @EventHandler
    public void onDeath(org.bukkit.event.entity.PlayerDeathEvent event) {
        ShelfPlayer player = Bookshelf.playerById(event.getEntity().getUniqueId());
        if ( player == null ) {
            return;
        }

        if ( event.getEntity().getKiller() == null ) {
            PlayerDeathEvent e = Events.call(new PlayerDeathEvent(player, event.getDeathMessage()));
            event.setDeathMessage(e.getDeathMessage());
            return;
        }

        ShelfPlayer killer = Bookshelf.playerById(event.getEntity().getKiller().getUniqueId());
        if ( killer == null ) {
            PlayerDeathEvent e = Events.call(new PlayerDeathEvent(player, event.getDeathMessage()));
            event.setDeathMessage(e.getDeathMessage());
            return;
        }

        PlayerDeathEvent e = Events.call(new PlayerDeathEvent(player, killer, event.getDeathMessage()));
        event.setDeathMessage(e.getDeathMessage());
    }

}
