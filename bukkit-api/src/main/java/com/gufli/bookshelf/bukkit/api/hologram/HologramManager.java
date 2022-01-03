package com.gufli.bookshelf.bukkit.api.hologram;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

public interface HologramManager {

    void show(Hologram hologram, ShelfPlayer player);

    void hide(Hologram hologram, ShelfPlayer player);

    void hideAll(Hologram hologram);

}
