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

    public static void setBossbar(ShelfPlayer player, Bossbar bossbar) {
        bossbarManager.setBossbar(player, bossbar);
    }

    public static Bossbar getBossbar(ShelfPlayer player) {
        return bossbarManager.getBossbar(player);
    }

}
