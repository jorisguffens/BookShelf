package com.gufli.bookshelf.api.events;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

public class PlayerPostLoginEvent extends AbstractPlayerEvent {

    public PlayerPostLoginEvent(ShelfPlayer player) {
        super(player);
    }
    
}
