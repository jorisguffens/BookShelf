package com.gufli.bookshelf.bukkit.events;

import com.gufli.bookshelf.entity.ShelfPlayer;
import com.gufli.bookshelf.event.Events;
import com.gufli.bookshelf.events.PlayerAttackByPlayerEvent;
import com.gufli.bookshelf.events.PlayerDeathEvent;
import com.gufli.bookshelf.server.Bookshelf;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityEvent;

public class DefaultEventListener {

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

        ShelfPlayer p = Bookshelf.getPlayer(entity.getUniqueId());
        if ( p == null ) {
            return;
        }

        ShelfPlayer d = null;
        if ( damager instanceof Player) {
            d = Bookshelf.getPlayer(damager.getUniqueId());
        }
        else if ( damager instanceof Projectile ) {
            Projectile projectile = (Projectile) damager;
            if ( projectile.getShooter() != null && projectile.getShooter() instanceof Player ) {
                Player shooter = (Player) projectile.getShooter();
                d = Bookshelf.getPlayer(shooter.getUniqueId());
            }
        }

        if ( d == null || p == d ) {
            return;
        }

        PlayerAttackByPlayerEvent attackEvent = Events.call(new PlayerAttackByPlayerEvent(p, d));
        event.setCancelled(attackEvent.isCancelled());
    }

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
