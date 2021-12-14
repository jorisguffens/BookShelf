package com.gufli.bookshelf.api.titles;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

public interface TitleManager {

    void sendTitle(ShelfPlayer player, String text, TitleType type, float seconds);

}
