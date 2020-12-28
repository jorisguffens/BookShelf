package com.gufli.bookshelf.events.defaults;

import com.gufli.bookshelf.entity.PlatformPlayer;

public abstract class AbstractPlayerEvent {

    private final PlatformPlayer player;

    public AbstractPlayerEvent(PlatformPlayer player) {
        this.player = player;
    }

    public PlatformPlayer getPlayer() {
        return player;
    }

}
