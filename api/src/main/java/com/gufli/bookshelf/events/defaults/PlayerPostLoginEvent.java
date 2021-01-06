package com.gufli.bookshelf.events.defaults;

import com.gufli.bookshelf.entity.ShelfPlayer;

public class PlayerPostLoginEvent extends AbstractPlayerEvent {

    public PlayerPostLoginEvent(ShelfPlayer player) {
        super(player);
    }
    
}
