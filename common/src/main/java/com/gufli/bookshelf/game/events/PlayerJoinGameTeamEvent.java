package com.gufli.bookshelf.game.events;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.events.PlayerEvent;
import com.gufli.bookshelf.game.AbstractGame;
import com.gufli.bookshelf.game.GameTeam;

public class PlayerJoinGameTeamEvent extends GameTeamEvent implements PlayerEvent {

    private final ShelfPlayer player;

    public PlayerJoinGameTeamEvent(AbstractGame game, GameTeam team, ShelfPlayer player) {
        super(game, team);
        this.player = player;
    }

    public ShelfPlayer getPlayer() {
        return player;
    }
}
