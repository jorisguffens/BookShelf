package com.gufli.bookshelf.events.defaults;

import com.gufli.bookshelf.entity.ShelfPlayer;

public class PlayerQuitEvent extends AbstractPlayerEvent {

    public PlayerQuitEvent(ShelfPlayer player) {
        super(player);
    }
    
}
