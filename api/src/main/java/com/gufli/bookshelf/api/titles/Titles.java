package com.gufli.bookshelf.api.titles;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

public class Titles {

    private Titles() {}

    private static TitleManager titleManager;

    public static void register(TitleManager manager) {
        if (titleManager != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton TitleManager.");
        }

        titleManager = manager;
    }

    public static void sendTitle(ShelfPlayer player, String text, TitleType type, int seconds) {
        titleManager.sendTitle(player, text, type, seconds);
    }

}
