package com.gufli.bookshelf.nametags;

import com.gufli.bookshelf.entity.ShelfPlayer;

public class NametagManager {

    private static NametagHandler nametagHandler;

    public static void register(NametagHandler handler) {
        if (nametagHandler != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton NametagHandler.");
        }

        nametagHandler = handler;
    }

    public static void setNametag(ShelfPlayer player, String prefix, String suffix) {
        nametagHandler.setNametag(player, prefix, suffix);
    }

    public static void setPrefix(ShelfPlayer player, String prefix) {
        nametagHandler.setPrefix(player, prefix);
    }

    public static void setSuffix(ShelfPlayer player, String suffix) {
        nametagHandler.setSuffix(player, suffix);
    }

    public static void clear(ShelfPlayer player) {
        nametagHandler.clear(player);
    }
}
