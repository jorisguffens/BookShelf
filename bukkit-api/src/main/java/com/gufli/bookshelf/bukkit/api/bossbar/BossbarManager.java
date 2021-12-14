package com.gufli.bookshelf.bukkit.api.bossbar;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

public interface BossbarManager {

    void setBossbar(ShelfPlayer player, Bossbar bossbar);

    Bossbar getBossbar(ShelfPlayer player);

    void removeBossbar(ShelfPlayer player);

    void updateBossbar(ShelfPlayer player);

}
