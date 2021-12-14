package com.gufli.bookshelf.api.events;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

public class PlayerJoinEvent extends AbstractPlayerEvent {

    public PlayerJoinEvent(ShelfPlayer player) {
        super(player);
    }

}
