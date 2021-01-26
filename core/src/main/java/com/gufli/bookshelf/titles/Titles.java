package com.gufli.bookshelf.titles;

import com.gufli.bookshelf.entity.ShelfPlayer;

public class Titles {

    private static TitleManager titleManager;

    public static void register(TitleManager manager) {
        if (titleManager != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton TitleManager.");
        }

        titleManager = manager;
    }

    void showTitle(ShelfPlayer player, String title, int seconds) {
        titleManager.showTitle(player, title, seconds);
    }

    void showSubtitle(ShelfPlayer player, String subtitle, int seconds) {
        titleManager.showSubtitle(player, subtitle, seconds);
    }

    void showActionbar(ShelfPlayer player, String text, int seconds) {
        titleManager.showActionbar(player, text, seconds);
    }

    void setBossbar(ShelfPlayer player, Bossbar bossbar) {
        titleManager.setBossbar(player, bossbar);
    }

    Bossbar getBossbar(ShelfPlayer player) {
        return titleManager.getBossbar(player);
    }

    void removeBossbar(ShelfPlayer player) {
        titleManager.removeBossbar(player);
    }

    void updateBossbar(ShelfPlayer player) {
        titleManager.updateBossbar(player);
    }

}
