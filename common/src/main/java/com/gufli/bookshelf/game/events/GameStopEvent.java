package com.gufli.bookshelf.game.events;

import com.gufli.bookshelf.game.AbstractGame;

public class GameStopEvent extends GameEvent {

    public GameStopEvent(AbstractGame game) {
        super(game);
    }
}
