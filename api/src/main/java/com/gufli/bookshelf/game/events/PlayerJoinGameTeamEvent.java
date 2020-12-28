package com.gufli.bookshelf.game.events;

import com.gufli.bookshelf.entity.PlatformPlayer;
import com.gufli.bookshelf.events.defaults.PlayerEvent;
import com.gufli.bookshelf.game.AbstractGame;
import com.gufli.bookshelf.game.GameTeam;

public class PlayerJoinGameTeamEvent extends GameTeamEvent implements PlayerEvent {

    private final PlatformPlayer player;

    public PlayerJoinGameTeamEvent(AbstractGame game, GameTeam team, PlatformPlayer player) {
        super(game, team);
        this.player = player;
    }

    public PlatformPlayer getPlayer() {
        return player;
    }
}
