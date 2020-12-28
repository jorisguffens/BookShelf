package com.gufli.bookshelf.game.events;

import com.gufli.bookshelf.game.AbstractGame;

public class GameStartEvent extends GameEvent {

    public GameStartEvent(AbstractGame game) {
        super(game);
    }
}
