package com.gufli.bookshelf.bukkit.api.hologram;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

public class Holograms {

    private static HologramManager hologramManager;

    public static void register(HologramManager manager) {
        if (hologramManager != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton hologramManager.");
        }

        hologramManager = manager;
    }

    public static void show(Hologram hologram, ShelfPlayer player) {
        hologramManager.show(hologram, player);
    }

    public static void hide(Hologram hologram, ShelfPlayer player) {
        hologramManager.hide(hologram, player);
    }

    public static void hideAll(Hologram hologram) {
        hologramManager.hideAll(hologram);
    }

}
