package com.gufli.bookshelf.events;

import com.gufli.bookshelf.entity.ShelfPlayer;

public interface PlayerEvent extends Event {

    ShelfPlayer getPlayer();

}
