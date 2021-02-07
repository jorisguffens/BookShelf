package com.gufli.bookshelf.events;

import com.gufli.bookshelf.entity.ShelfPlayer;

public class PlayerLoginEvent extends AbstractPlayerEvent {

    public PlayerLoginEvent(ShelfPlayer player) {
        super(player);
    }
    
}
