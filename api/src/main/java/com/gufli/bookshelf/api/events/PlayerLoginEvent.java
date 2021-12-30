package com.gufli.bookshelf.api.events;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

public class PlayerLoginEvent extends AbstractPlayerEvent {

    public PlayerLoginEvent(ShelfPlayer player) {
        super(true, player);
    }
    
}
