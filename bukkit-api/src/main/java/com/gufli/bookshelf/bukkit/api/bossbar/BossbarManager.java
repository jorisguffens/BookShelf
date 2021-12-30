package com.gufli.bookshelf.bukkit.api.bossbar;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

public interface BossbarManager {

    void changeBossbar(ShelfPlayer player, Bossbar bossbar);

    void removeBossbar(ShelfPlayer player);

    void updateBossbar(ShelfPlayer player);

}
