package com.gufli.bookshelf.game.manager;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.game.Game;
import com.gufli.bookshelf.api.scheduler.Scheduler;

import java.util.Collections;
import java.util.Set;

public class GameManager {

    private static AbstractGameManager<?> gameServer;

    public static void register(AbstractGameManager<?> server) {
        if (gameServer != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton GameServer.");
        }

        gameServer = server;
    }

    public static Scheduler getScheduler() {
        return gameServer.getScheduler();
    }

    public static Set<Game> getGames() {
        return Collections.unmodifiableSet(gameServer.getGames());
    }

    public static Game getGame(ShelfPlayer player) {
        return gameServer.getGame(player);
    }

}
