package com.gufli.bookshelf.bukkit.bossbar;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.event.Events;
import com.gufli.bookshelf.api.events.PlayerQuitEvent;
import com.gufli.bookshelf.api.events.ShelfShutdownEvent;
import com.gufli.bookshelf.api.scheduler.SchedulerTask;
import com.gufli.bookshelf.api.server.Bookshelf;
import com.gufli.bookshelf.bukkit.api.bossbar.Bossbar;
import com.gufli.bookshelf.bukkit.api.bossbar.BossbarManager;
import com.gufli.bookshelf.bukkit.api.bossbar.Bossbars;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BukkitBossbarManager implements BossbarManager {

    private final Map<ShelfPlayer, BukkitBossbar> players = new HashMap<>();

    public BukkitBossbarManager() {
        Bossbars.register(this);

        Events.subscribe(PlayerQuitEvent.class)
                .handler(e -> removeBossbar(e.getPlayer()));

        SchedulerTask task = Bookshelf.scheduler()
                .asyncRepeating(this::update, 50, TimeUnit.MILLISECONDS);

        Events.subscribe(ShelfShutdownEvent.class)
                .handler(e -> {
                    task.cancel();
                    new HashSet<>(players.keySet()).forEach(this::removeBossbar);
                });
    }

    private void update() {
        players.keySet().forEach(this::updateBossbar);
    }

    @Override
    public void changeBossbar(ShelfPlayer player, Bossbar bossbar) {
        removeBossbar(player);
        BukkitBossbar bb = new BukkitBossbar(player, bossbar);
        players.put(player, bb);
    }

    @Override
    public void removeBossbar(ShelfPlayer player) {
        if (!players.containsKey(player)) {
            return;
        }

        BukkitBossbar bb = players.get(player);
        bb.destroy();
        players.remove(player);
    }

    @Override
    public void updateBossbar(ShelfPlayer player) {
        if (!players.containsKey(player)) {
            return;
        }

        players.get(player).update();
    }

}
