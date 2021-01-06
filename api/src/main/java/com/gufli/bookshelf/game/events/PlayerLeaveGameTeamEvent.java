package com.gufli.bookshelf.game.events;

import com.gufli.bookshelf.entity.ShelfPlayer;
import com.gufli.bookshelf.events.defaults.PlayerEvent;
import com.gufli.bookshelf.game.AbstractGame;
import com.gufli.bookshelf.game.GameTeam;

public class PlayerLeaveGameTeamEvent extends GameTeamEvent implements PlayerEvent {

    private final ShelfPlayer player;

    public PlayerLeaveGameTeamEvent(AbstractGame game, GameTeam team, ShelfPlayer player) {
        super(game, team);
        this.player = player;
    }

    public ShelfPlayer getPlayer() {
        return player;
    }
}
