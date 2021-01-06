package com.gufli.bookshelf.events.defaults;

import com.gufli.bookshelf.entity.ShelfPlayer;

public class PlayerLoginEvent extends AbstractPlayerEvent {

    public PlayerLoginEvent(ShelfPlayer player) {
        super(player);
    }
    
}
