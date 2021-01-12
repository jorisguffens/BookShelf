package com.gufli.bookshelf.events;

import com.gufli.bookshelf.entity.ShelfPlayer;

public class PlayerJoinEvent extends AbstractPlayerEvent {

    public PlayerJoinEvent(ShelfPlayer player) {
        super(player);
    }

}
