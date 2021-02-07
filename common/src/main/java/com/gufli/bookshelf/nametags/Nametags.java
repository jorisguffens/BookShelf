package com.gufli.bookshelf.nametags;

import com.gufli.bookshelf.entity.ShelfPlayer;

public class Nametags {

    private static NametagManager nametagManager;

    public static void register(NametagManager manager) {
        if (nametagManager != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton nametagManager.");
        }

        nametagManager = manager;
    }

    public static void setNametag(ShelfPlayer player, String prefix, String suffix) {
        nametagManager.setNametag(player, prefix, suffix);
    }

    public static void setPrefix(ShelfPlayer player, String prefix) {
        nametagManager.setPrefix(player, prefix);
    }

    public static void setSuffix(ShelfPlayer player, String suffix) {
        nametagManager.setSuffix(player, suffix);
    }

    public static void clear(ShelfPlayer player) {
        nametagManager.removeNametag(player);
    }
}
