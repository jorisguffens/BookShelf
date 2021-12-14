package com.gufli.bookshelf.api.titles;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

public interface TitleManager {

    void showTitle(ShelfPlayer player, String text, TitleType type, float seconds);

}
