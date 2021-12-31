package com.gufli.bookshelf.game.events;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.events.PlayerEvent;
import com.gufli.bookshelf.game.AbstractGame;

public class PlayerLeaveGameEvent extends GameEvent implements PlayerEvent {

    private final ShelfPlayer player;

    public PlayerLeaveGameEvent(AbstractGame game, ShelfPlayer player) {
        super(game);
        this.player = player;
    }

    public ShelfPlayer player() {
        return player;
    }
}
