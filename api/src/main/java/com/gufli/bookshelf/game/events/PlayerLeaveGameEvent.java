package com.gufli.bookshelf.game.events;

import com.gufli.bookshelf.entity.PlatformPlayer;
import com.gufli.bookshelf.events.defaults.PlayerEvent;
import com.gufli.bookshelf.game.AbstractGame;

public class PlayerLeaveGameEvent extends GameEvent implements PlayerEvent {

    private final PlatformPlayer player;

    public PlayerLeaveGameEvent(AbstractGame game, PlatformPlayer player) {
        super(game);
        this.player = player;
    }

    public PlatformPlayer getPlayer() {
        return player;
    }
}
