package com.gufli.bookshelf.bukkit.bossbar;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.event.Events;
import com.gufli.bookshelf.api.events.PlayerQuitEvent;
import com.gufli.bookshelf.bukkit.api.bossbar.Bossbar;
import com.gufli.bookshelf.bukkit.api.bossbar.BossbarManager;
import com.gufli.bookshelf.bukkit.api.bossbar.Bossbars;

import java.util.HashMap;
import java.util.Map;

public class BukkitBossbarManager implements BossbarManager {

    private final Map<ShelfPlayer, BukkitBossbar> players = new HashMap<>();

    public BukkitBossbarManager() {
        Bossbars.register(this);

        Events.subscribe(PlayerQuitEvent.class)
                .handler(e -> removeBossbar(e.getPlayer()));
    }

    @Override
    public void setBossbar(ShelfPlayer player, Bossbar bossbar) {
        removeBossbar(player);
        BukkitBossbar bb = new BukkitBossbar(player, bossbar);
        players.put(player, bb);
        bb.show();
    }

    @Override
    public Bossbar getBossbar(ShelfPlayer player) {
        return players.containsKey(player) ? players.get(player).getBossbar() : null;
    }

    @Override
    public void removeBossbar(ShelfPlayer player) {
        if ( !players.containsKey(player) ) {
            return;
        }

        BukkitBossbar bb = players.get(player);
        bb.destroy();
        players.remove(player);
    }

    @Override
    public void updateBossbar(ShelfPlayer player) {
        if ( !players.containsKey(player) ) {
            return;
        }

        players.get(player).update();
    }

}
