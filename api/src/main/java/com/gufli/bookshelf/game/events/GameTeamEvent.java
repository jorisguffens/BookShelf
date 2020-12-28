package com.gufli.bookshelf.game.events;

import com.gufli.bookshelf.game.AbstractGame;
import com.gufli.bookshelf.game.GameTeam;

public class GameTeamEvent extends GameEvent {

    private final GameTeam team;

    public GameTeamEvent(AbstractGame game, GameTeam team) {
        super(game);
        this.team = team;
    }

    public GameTeam getTeam() {
        return team;
    }
}
