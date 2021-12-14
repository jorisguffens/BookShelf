package com.gufli.bookshelf.game;

import com.gufli.bookshelf.api.location.arena.Arena;
import com.gufli.bookshelf.api.entity.ShelfPlayer;

import java.util.Set;

public interface Game {

    Set<ShelfPlayer> getPlayers();

    boolean contains(ShelfPlayer player);

    PlayerGameStatus getStatus(ShelfPlayer player);

    GameStatus getStatus();

    void broadcast(String msg);

    Arena getArena();

}
