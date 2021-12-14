package com.gufli.bookshelf.api.events;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

public class PlayerQuitEvent extends AbstractPlayerEvent {

    public PlayerQuitEvent(ShelfPlayer player) {
        super(player);
    }
    
}
