package com.gufli.bookshelf.events.defaults;

import com.gufli.bookshelf.entity.ShelfPlayer;

public abstract class AbstractPlayerEvent {

    private final ShelfPlayer player;

    public AbstractPlayerEvent(ShelfPlayer player) {
        this.player = player;
    }

    public ShelfPlayer getPlayer() {
        return player;
    }

}
