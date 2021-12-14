package com.gufli.bookshelf.api.events;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

public abstract class AbstractPlayerEvent implements PlayerEvent {

    private final ShelfPlayer player;

    public AbstractPlayerEvent(ShelfPlayer player) {
        this.player = player;
    }

    public ShelfPlayer getPlayer() {
        return player;
    }

}
