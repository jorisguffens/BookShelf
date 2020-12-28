package com.gufli.bookshelf.bukkit.events;

import com.gufli.bookshelf.entity.PlatformPlayer;
import com.gufli.bookshelf.events.Event;
import com.gufli.bookshelf.events.EventListener;
import com.gufli.bookshelf.events.EventManager;
import com.gufli.bookshelf.events.EventPriority;
import com.gufli.bookshelf.events.defaults.PlayerAttackByPlayerEvent;
import com.gufli.bookshelf.events.defaults.PlayerDeathEvent;
import com.gufli.bookshelf.server.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityEvent;

public class DefaultEventListener implements EventListener {

    @Event(priority = EventPriority.HIGHEST)
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        damageEvent(e, e.getEntity(), e.getDamager());
    }

    @Event(priority = EventPriority.HIGHEST)
    public void onEntityCombustByEntity(EntityCombustByEntityEvent e) {
        damageEvent(e, e.getEntity(), e.getCombuster());
    }

    private <T extends EntityEvent & Cancellable> void damageEvent(T event, Entity entity, Entity damager) {
        if ( !(entity instanceof Player) ) {
            return;
        }

        PlatformPlayer p = Server.getPlayer(entity.getUniqueId());
        if ( p == null ) {
            return;
        }

        PlatformPlayer d = null;
        if ( damager instanceof Player) {
            d = Server.getPlayer(damager.getUniqueId());
        }
        else if ( damager instanceof Projectile ) {
            Projectile projectile = (Projectile) damager;
            if ( projectile.getShooter() != null && projectile.getShooter() instanceof Player ) {
                Player shooter = (Player) projectile.getShooter();
                d = Server.getPlayer(shooter.getUniqueId());
            }
        }

        if ( d == null || p == d ) {
            return;
        }

        PlayerAttackByPlayerEvent attackEvent = EventManager.dispatch(new PlayerAttackByPlayerEvent(p, d));
        event.setCancelled(attackEvent.isCancelled());
    }

    @Event(priority = EventPriority.LOWEST)
    public void onDeath(org.bukkit.event.entity.PlayerDeathEvent event) {
        PlatformPlayer player = Server.getPlayer(event.getEntity().getUniqueId());
        if ( player == null ) {
            return;
        }

        if ( event.getEntity().getKiller() == null ) {
            PlayerDeathEvent e = EventManager.dispatch(new PlayerDeathEvent(player, event.getDeathMessage()));
            event.setDeathMessage(e.getDeathMessage());
            return;
        }

        PlatformPlayer killer = Server.getPlayer(event.getEntity().getKiller().getUniqueId());
        if ( killer == null ) {
            PlayerDeathEvent e = EventManager.dispatch(new PlayerDeathEvent(player, event.getDeathMessage()));
            event.setDeathMessage(e.getDeathMessage());
            return;
        }

        PlayerDeathEvent e = EventManager.dispatch(new PlayerDeathEvent(player, killer, event.getDeathMessage()));
        event.setDeathMessage(e.getDeathMessage());
    }

}
