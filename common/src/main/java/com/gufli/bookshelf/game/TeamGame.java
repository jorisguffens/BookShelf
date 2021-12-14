package com.gufli.bookshelf.game;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

import java.util.Set;

public interface TeamGame<T extends GameTeam> extends Game {

    Set<T> getTeams();

    T getTeam(ShelfPlayer player);

}
