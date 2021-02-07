package com.gufli.bookshelf.game.events;

import com.gufli.bookshelf.events.Event;
import com.gufli.bookshelf.game.AbstractGame;

public class GameEvent implements Event {

    private final AbstractGame game;

    public GameEvent(AbstractGame game) {
        this.game = game;
    }

    public AbstractGame getGame() {
        return game;
    }
}
