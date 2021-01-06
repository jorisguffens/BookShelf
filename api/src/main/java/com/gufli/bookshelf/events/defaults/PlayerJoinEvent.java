package com.gufli.bookshelf.events.defaults;

import com.gufli.bookshelf.entity.ShelfPlayer;

public class PlayerJoinEvent extends AbstractPlayerEvent {

    public PlayerJoinEvent(ShelfPlayer player) {
        super(player);
    }

}
