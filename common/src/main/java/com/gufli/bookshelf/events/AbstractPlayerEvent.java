package com.gufli.bookshelf.events;

import com.gufli.bookshelf.entity.ShelfPlayer;

public abstract class AbstractPlayerEvent implements PlayerEvent {

    private final ShelfPlayer player;

    public AbstractPlayerEvent(ShelfPlayer player) {
        this.player = player;
    }

    public ShelfPlayer getPlayer() {
        return player;
    }

}
