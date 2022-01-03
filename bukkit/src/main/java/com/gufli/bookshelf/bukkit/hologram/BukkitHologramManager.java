package com.gufli.bookshelf.bukkit.hologram;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.event.Events;
import com.gufli.bookshelf.api.events.ShelfShutdownEvent;
import com.gufli.bookshelf.api.scheduler.SchedulerTask;
import com.gufli.bookshelf.api.server.Bookshelf;
import com.gufli.bookshelf.bukkit.api.entity.BukkitPlayer;
import com.gufli.bookshelf.bukkit.api.hologram.Hologram;
import com.gufli.bookshelf.bukkit.api.hologram.HologramManager;
import com.gufli.bookshelf.bukkit.api.hologram.Holograms;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class BukkitHologramManager implements HologramManager {

    private final Map<Hologram, Map<ShelfPlayer, BukkitHologram>> holograms = new ConcurrentHashMap<>();

    public BukkitHologramManager() {
        Holograms.register(this);

//        SchedulerTask task = Bookshelf.scheduler()
//                .asyncRepeating(this::update, 50, TimeUnit.MILLISECONDS);

        Events.subscribe(ShelfShutdownEvent.class)
                .handler(e -> {
//                    task.cancel();
                    new HashSet<>(holograms.keySet()).forEach(this::hideAll);
                });
    }

    private void update() {
        holograms.values().stream()
                .flatMap(key -> key.values().stream())
                .forEach(BukkitHologram::update);
    }

    @Override
    public void show(Hologram hologram, ShelfPlayer player) {
        if ( !holograms.containsKey(hologram) ) {
            holograms.put(hologram, new ConcurrentHashMap<>());
        }

        BukkitPlayer bp = (BukkitPlayer) player;
        BukkitHologram bh = new BukkitHologram(bp, hologram);
        holograms.get(hologram).put(bp, bh);
        bh.update();
    }

    @Override
    public void hide(Hologram hologram, ShelfPlayer player) {
        if ( !holograms.containsKey(hologram) ) {
            return;
        }

        Map<ShelfPlayer, BukkitHologram> hk = holograms.get(hologram);
        if ( !hk.containsKey(player) ) {
            return;
        }

        BukkitHologram bh = hk.remove(player);
        bh.destroy();

        if ( hk.isEmpty() ) {
            holograms.remove(hologram);
        }
    }

    @Override
    public void hideAll(Hologram hologram) {
        if ( !holograms.containsKey(hologram) ) {
            return;
        }

        Map<ShelfPlayer, BukkitHologram> hk = holograms.get(hologram);
        hk.values().forEach(BukkitHologram::destroy);
        holograms.remove(hologram);
    }
}
