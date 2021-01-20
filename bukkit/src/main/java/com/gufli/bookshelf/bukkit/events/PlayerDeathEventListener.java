package com.gufli.bookshelf.bukkit.events;

import com.gufli.bookshelf.entity.ShelfPlayer;
import com.gufli.bookshelf.event.Events;
import com.gufli.bookshelf.events.PlayerDeathEvent;
import com.gufli.bookshelf.server.Bookshelf;
import org.bukkit.event.EventHandler;

public class PlayerDeathEventListener {

    @EventHandler
    public void onDeath(org.bukkit.event.entity.PlayerDeathEvent event) {
        ShelfPlayer player = Bookshelf.getPlayer(event.getEntity().getUniqueId());
        if ( player == null ) {
            return;
        }

        if ( event.getEntity().getKiller() == null ) {
            PlayerDeathEvent e = Events.call(new PlayerDeathEvent(player, event.getDeathMessage()));
            event.setDeathMessage(e.getDeathMessage());
            return;
        }

        ShelfPlayer killer = Bookshelf.getPlayer(event.getEntity().getKiller().getUniqueId());
        if ( killer == null ) {
            PlayerDeathEvent e = Events.call(new PlayerDeathEvent(player, event.getDeathMessage()));
            event.setDeathMessage(e.getDeathMessage());
            return;
        }

        PlayerDeathEvent e = Events.call(new PlayerDeathEvent(player, killer, event.getDeathMessage()));
        event.setDeathMessage(e.getDeathMessage());
    }

}
