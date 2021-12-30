package com.gufli.bookshelf.bukkit.api.bossbar;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

public class Bossbars {

    private static BossbarManager bossbarManager;

    public static void register(BossbarManager manager) {
        if (bossbarManager != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton bossbarManager.");
        }

        bossbarManager = manager;
    }

    public static void changeBossbar(ShelfPlayer player, Bossbar bossbar) {
        bossbarManager.changeBossbar(player, bossbar);
    }

    public static void removeBossbar(ShelfPlayer player) {
        bossbarManager.removeBossbar(player);
    }

    public static void updateBossbar(ShelfPlayer player) {
        bossbarManager.updateBossbar(player);
    }

}
