package com.gufli.bookshelf.game;

import com.gufli.bookshelf.arenas.Arena;
import com.gufli.bookshelf.entity.ShelfPlayer;

import java.util.Set;

public interface Game {

    Set<ShelfPlayer> getPlayers();

    boolean contains(ShelfPlayer player);

    PlayerGameStatus getStatus(ShelfPlayer player);

    GameStatus getStatus();

    void broadcast(String msg);

    Arena getArena();

}
