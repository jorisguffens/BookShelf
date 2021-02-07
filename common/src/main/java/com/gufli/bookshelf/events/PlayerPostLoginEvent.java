package com.gufli.bookshelf.events;

import com.gufli.bookshelf.entity.ShelfPlayer;

public class PlayerPostLoginEvent extends AbstractPlayerEvent {

    public PlayerPostLoginEvent(ShelfPlayer player) {
        super(player);
    }
    
}
