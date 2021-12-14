package com.gufli.bookshelf.api.titles;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

public class Titles {

    private static TitleManager titleManager;

    private Titles() {}

    public static void register(TitleManager manager) {
        if (titleManager != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton TitleManager.");
        }

        titleManager = manager;
    }

    public static void showTitle(ShelfPlayer player, String text, TitleType type, int seconds) {
        titleManager.showTitle(player, text, type, seconds);
    }

}
