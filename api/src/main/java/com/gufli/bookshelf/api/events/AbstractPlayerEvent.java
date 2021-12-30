package com.gufli.bookshelf.api.events;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

public abstract class AbstractPlayerEvent extends AbstractEvent implements PlayerEvent {

    private final ShelfPlayer player;

    public AbstractPlayerEvent(boolean async, ShelfPlayer player) {
        super(async);
        this.player = player;
    }

    public AbstractPlayerEvent(ShelfPlayer player) {
        super();
        this.player = player;
    }

    public ShelfPlayer getPlayer() {
        return player;
    }

}
