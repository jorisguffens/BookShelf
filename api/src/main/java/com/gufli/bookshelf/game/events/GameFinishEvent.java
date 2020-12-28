package com.gufli.bookshelf.game.events;

import com.gufli.bookshelf.game.AbstractGame;

public class GameFinishEvent extends GameEvent {

    public GameFinishEvent(AbstractGame game) {
        super(game);
    }
}
