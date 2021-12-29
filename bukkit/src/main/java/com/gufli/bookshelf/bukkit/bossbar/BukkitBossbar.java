package com.gufli.bookshelf.bukkit.bossbar;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.bukkit.api.bossbar.Bossbar;
import com.gufli.bookshelf.bukkit.api.entity.BukkitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import java.util.UUID;

public class BukkitBossbar {

    private final UUID id = UUID.randomUUID();
    private final Bossbar bossbar;
    private final ShelfPlayer player;

    private final BossBar handle;

    public BukkitBossbar(ShelfPlayer player, Bossbar bossbar) {
        this.player = player;
        this.bossbar = bossbar;

        this.handle = Bukkit.createBossBar(bossbar.text(), bossbar.color(), BarStyle.SOLID);
        this.handle.setProgress(bossbar.progress());
        this.handle.addPlayer(((BukkitPlayer) player).getHandle());
    }

    public UUID id() {
        return id;
    }

    public Bossbar bossbar() {
        return bossbar;
    }

    public ShelfPlayer player() {
        return player;
    }

    void destroy() {
        handle.removeAll();
    }

}
