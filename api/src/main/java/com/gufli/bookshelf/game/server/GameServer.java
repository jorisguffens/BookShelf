package com.gufli.bookshelf.game.server;

import com.gufli.bookshelf.entity.PlatformPlayer;
import com.gufli.bookshelf.game.AbstractGame;
import com.gufli.bookshelf.scheduler.AbstractScheduler;

import java.util.Set;

public class GameServer {

    private static AbstractGameServer gameServer;

    public static void register(AbstractGameServer server) {
        if (gameServer != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton GameServer.");
        }

        gameServer = server;
    }

    public static AbstractScheduler getScheduler() {
        return gameServer.getScheduler();
    }

    public static Set<AbstractGame> getGames() {
        return gameServer.getGames();
    }

    public static AbstractGame getGame(PlatformPlayer player) {
        return gameServer.getGame(player);
    }

    public static void register(AbstractGame game) {
        gameServer.register(game);
    }

    public static void unregister(AbstractGame game) {
        gameServer.unregister(game);
    }

}
