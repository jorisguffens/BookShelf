package com.gufli.bookshelf.bukkit.game;

import com.gufli.bookshelf.arenas.WorldArena;
import com.gufli.bookshelf.bukkit.util.LocationConverter;
import com.gufli.bookshelf.entity.PlatformPlayer;
import com.gufli.bookshelf.events.EventListenerExecutor;
import com.gufli.bookshelf.events.defaults.PlayerEvent;
import com.gufli.bookshelf.game.AbstractGame;
import com.gufli.bookshelf.server.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.hanging.HangingEvent;
import org.bukkit.event.vehicle.VehicleEvent;
import org.bukkit.event.weather.WeatherEvent;
import org.bukkit.event.world.WorldEvent;

public class GameEvents {

    public static EventListenerExecutor setBoundary(EventListenerExecutor executor, AbstractGame game) {
        return executor
                // custom events
                .boundary(PlayerEvent.class, event -> on(game, event))

                // bukkit events
                .boundary(org.bukkit.event.player.PlayerEvent.class, event -> on(game, event))
                .boundary(BlockEvent.class, event -> on(game, event))
                .boundary(EntityEvent.class, event -> on(game, event))
                .boundary(WeatherEvent.class, event -> on(game, event))
                .boundary(VehicleEvent.class, event -> on(game, event))
                .boundary(WorldEvent.class, event -> on(game, event))
                .boundary(EnchantItemEvent.class, event -> on(game, event))
                .boundary(HangingEvent.class, event -> on(game, event));
    }

    private static boolean on(AbstractGame game, PlayerEvent event) {
        return game.contains(event.getPlayer());
    }

    private static boolean on(AbstractGame game, org.bukkit.event.player.PlayerEvent event) {
        PlatformPlayer player = Server.getPlayer(event.getPlayer().getUniqueId());
        return player != null && game.contains(player);
    }

    private static boolean on(AbstractGame game, BlockEvent event) {
        if ( game.getArena() == null ) {
            return false;
        }
        return game.getArena().contains(LocationConverter.convert(event.getBlock().getLocation()));
    }

    private static boolean on(AbstractGame game, EntityEvent event) {
        if ( event.getEntity() instanceof Player ) {
            PlatformPlayer player = Server.getPlayer(event.getEntity().getUniqueId());
            return player != null && game.contains(player);
        }
        if ( game.getArena() == null ) {
            return false;
        }
        return game.getArena().contains(LocationConverter.convert(event.getEntity().getLocation()));
    }

    private static boolean on(AbstractGame game, WeatherEvent event) {
        if ( game.getArena() == null ) {
            return false;
        }
        return game.getArena() instanceof WorldArena
                && ((WorldArena) game.getArena()).getWorldName().equals(event.getWorld().getName());
    }

    private static boolean on(AbstractGame game, VehicleEvent event) {
        if ( game.getArena() == null ) {
            return false;
        }
        return game.getArena().contains(LocationConverter.convert(event.getVehicle().getLocation()));
    }

    private static boolean on(AbstractGame game, WorldEvent event) {
        if ( game.getArena() == null ) {
            return false;
        }
        return game.getArena() instanceof WorldArena
                && ((WorldArena) game.getArena()).getWorldName().equals(event.getWorld().getName());
    }

    private static boolean on(AbstractGame game, EnchantItemEvent event) {
        PlatformPlayer player = Server.getPlayer(event.getEnchanter().getUniqueId());
        return player != null && game.contains(player);
    }

    private static boolean on(AbstractGame game, HangingEvent event) {
        if ( game.getArena() == null ) {
            return false;
        }
        return game.getArena().contains(LocationConverter.convert(event.getEntity().getLocation()));
    }
    
}
