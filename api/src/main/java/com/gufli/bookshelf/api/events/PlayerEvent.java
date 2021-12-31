package com.gufli.bookshelf.api.events;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

public interface PlayerEvent extends Event {

    ShelfPlayer player();

}
