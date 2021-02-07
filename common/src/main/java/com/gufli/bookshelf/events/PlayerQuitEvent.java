package com.gufli.bookshelf.events;

import com.gufli.bookshelf.entity.ShelfPlayer;

public class PlayerQuitEvent extends AbstractPlayerEvent {

    public PlayerQuitEvent(ShelfPlayer player) {
        super(player);
    }
    
}
