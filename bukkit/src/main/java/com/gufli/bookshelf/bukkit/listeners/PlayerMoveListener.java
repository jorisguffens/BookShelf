package com.gufli.bookshelf.bukkit.listeners;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.event.Events;
import com.gufli.bookshelf.api.events.PlayerMoveEvent;
import com.gufli.bookshelf.api.server.Bookshelf;
import com.gufli.bookshelf.bukkit.api.entity.BukkitPlayer;
import com.gufli.bookshelf.bukkit.api.location.LocationConverter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;

import java.util.concurrent.TimeUnit;

public class PlayerMoveListener implements Listener {

    private final static String PREVIOUS_LOCATION_KEY = "PREVIOUS_LOCATION";

    public PlayerMoveListener() {
        Bookshelf.scheduler().asyncRepeating(() ->
                        Bookshelf.players().forEach(this::check),
                200, TimeUnit.MILLISECONDS);
    }

//    private boolean isChunkLoaded(Location loc) {
//        int chunkX = (int) Math.floor(loc.getBlockX() / 16.f);
//        int chunkZ = (int) Math.floor(loc.getBlockZ() / 16.f);
//        return loc.getWorld().isChunkLoaded(chunkX, chunkZ);
//    }

    private void check(ShelfPlayer player) {
        Player bp = ((BukkitPlayer) player).handle();

        if (!player.has(PREVIOUS_LOCATION_KEY)) {
            player.set(PREVIOUS_LOCATION_KEY, bp.getLocation());
            return;
        }

        Location from = player.get(PREVIOUS_LOCATION_KEY, Location.class);
        Location to = bp.getLocation();

        if (from.getBlockX() == to.getBlockX()
                && from.getBlockY() == to.getBlockY()
                && from.getBlockZ() == to.getBlockZ()) {
            return;
        }

        player.set(PREVIOUS_LOCATION_KEY, to);
        Bookshelf.scheduler().sync().execute(() ->
                handleMove(bp, from, to, () -> {
                    player.set(PREVIOUS_LOCATION_KEY, from);
                    if (bp.getVehicle() != null) {
                        bp.eject();
                    }
                    bounceBack(bp, from, to);
                }));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onTeleport(PlayerTeleportEvent event) {
        handleMove(event.getPlayer(), event.getFrom(), event.getTo(),
                () -> event.setCancelled(true));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onVehicleMove(VehicleMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX()
                && event.getFrom().getBlockY() == event.getTo().getBlockY()
                && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        for (Entity entity : event.getVehicle().getPassengers()) {
            if (!(entity instanceof Player bp)) continue;

            ShelfPlayer player = Bookshelf.playerById(bp.getUniqueId());
            player.set(PREVIOUS_LOCATION_KEY, event.getFrom());

            Location to = event.getTo();
            handleMove(bp, event.getFrom(), to, () -> {
                bp.eject();
                bounceBack(bp, event.getFrom(), event.getTo());
            });
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onVehicleExit(VehicleExitEvent event) {
        if (!(event.getExited() instanceof Player bp)) {
            return;
        }

        ShelfPlayer player = Bookshelf.playerById(bp.getUniqueId());
        if (player == null) {
            // event gets called when a player disconnects while in a vehicle, but after the playerquitevent
            return;
        }

        Location from;
        if (player.has(PREVIOUS_LOCATION_KEY)) {
            from = player.get(PREVIOUS_LOCATION_KEY, Location.class);
        } else {
            from = event.getVehicle().getLocation().add(0, 0.2, 0);
        }

        Bookshelf.scheduler().syncLater(() -> {
            Location to = event.getExited().getLocation();
            handleMove(bp, from, to, () -> bp.teleport(to));
        }, 20, TimeUnit.MILLISECONDS);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onVehicleClick(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Vehicle)) {
            return;
        }

        handleMove(event.getPlayer(), event.getPlayer().getLocation(),
                event.getRightClicked().getLocation(),
                () -> event.setCancelled(true));
    }

    private void bounceBack(Player player, Location from, Location to) {
        Vector direction = from.toVector().subtract(to.toVector()).normalize();
        Vector result = new Vector(direction.getX(), 0, direction.getZ());
        //Location tp = from.clone().add(result.multiply(0.3));

        double xDiff = Math.abs(from.getX() - to.getX());
        double zDiff = Math.abs(from.getZ() - to.getZ());
        if (xDiff < zDiff) {
            result.setX(0);
        } else {
            result.setZ(0);
        }
        Location tp = from.clone().add(result.multiply(0.3));

        tp.setYaw(player.getLocation().getYaw());
        tp.setPitch(player.getLocation().getPitch());

        while (tp.getBlock().getType().isSolid() || tp.clone().add(0, 1, 0).getBlock().getType().isSolid()) {
            tp.add(0, 1, 0);
        }

        player.teleport(tp);

        int air = 0;
        for (int i = 1; i <= 2; i++) {
            air += !tp.clone().add(0, -i, 0).getBlock().getType().isSolid() ? 1 : 0;
        }

        if (air == 2) {
            player.setVelocity(result.multiply(0.7));
        } else {
            player.setVelocity(result.add(new Vector(0, 0.1, 0)).multiply(0.3));
        }

        player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1F, 1F);
    }

    private void handleMove(Player p, Location from, Location to, Runnable cancel) {
        ShelfPlayer player = Bookshelf.playerById(p.getUniqueId());
        handleMove(player, from, to, cancel);
    }

    private void handleMove(ShelfPlayer player, Location from, Location to, Runnable cancel) {
        PlayerMoveEvent event = new PlayerMoveEvent(player, LocationConverter.convert(from), LocationConverter.convert(to));
        Events.call(event);

        if (event.isCancelled()) {
            cancel.run();
        }
    }

}
