package com.gufli.bookshelf.game;

import com.gufli.bookshelf.api.location.region.Region;
import com.gufli.bookshelf.api.entity.ShelfPlayer;

import java.util.Set;

public interface Game {

    Set<ShelfPlayer> getPlayers();

    boolean contains(ShelfPlayer player);

    PlayerGameStatus getStatus(ShelfPlayer player);

    GameStatus getStatus();

    void broadcast(String msg);

    Region getArena();

}
