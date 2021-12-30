package com.gufli.bookshelf.bukkit.listeners;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.event.Events;
import com.gufli.bookshelf.api.events.PlayerAttackedByPlayerEvent;
import com.gufli.bookshelf.api.server.Bookshelf;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityEvent;

public class PlayerAttackEventListener implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        damageEvent(e, e.getEntity(), e.getDamager());
    }

    @EventHandler
    public void onEntityCombustByEntity(EntityCombustByEntityEvent e) {
        damageEvent(e, e.getEntity(), e.getCombuster());
    }

    private <T extends EntityEvent & Cancellable> void damageEvent(T event, Entity entity, Entity damager) {
        if ( !(entity instanceof Player) ) {
            return;
        }

        ShelfPlayer p = Bookshelf.playerById(entity.getUniqueId());
        if ( p == null ) {
            return;
        }

        ShelfPlayer d = null;
        if ( damager instanceof Player) {
            d = Bookshelf.playerById(damager.getUniqueId());
        }
        else if ( damager instanceof Projectile ) {
            Projectile projectile = (Projectile) damager;
            if ( projectile.getShooter() != null && projectile.getShooter() instanceof Player ) {
                Player shooter = (Player) projectile.getShooter();
                d = Bookshelf.playerById(shooter.getUniqueId());
            }
        }

        if ( d == null || p == d ) {
            return;
        }

        PlayerAttackedByPlayerEvent attackEvent = Events.call(new PlayerAttackedByPlayerEvent(p, d));
        event.setCancelled(attackEvent.isCancelled());
    }

}
