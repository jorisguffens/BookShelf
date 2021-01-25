package com.gufli.bookshelf.titles;

import com.gufli.bookshelf.entity.ShelfPlayer;

public interface TitleManager {

    void showTitle(ShelfPlayer player, String title, int seconds);

    void showSubtitle(ShelfPlayer player, String subtitle, int seconds);

    void showActionbar(ShelfPlayer player, String text, int seconds);

    void setBossbar(ShelfPlayer player, Bossbar bossbar);

    Bossbar getBossbar(ShelfPlayer player);

    void removeBossbar(ShelfPlayer player);

    void updateBossbar(ShelfPlayer player);

}
