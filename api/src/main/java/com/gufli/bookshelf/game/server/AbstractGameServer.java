package com.gufli.bookshelf.game.server;

import com.gufli.bookshelf.entity.PlatformPlayer;
import com.gufli.bookshelf.game.AbstractGame;
import com.gufli.bookshelf.scheduler.AbstractScheduler;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class AbstractGameServer {

    private final Set<AbstractGame> games = new CopyOnWriteArraySet<>();
    private final AbstractScheduler scheduler;

    public AbstractGameServer(AbstractScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public final AbstractScheduler getScheduler() {
        return scheduler;
    }

    public Set<AbstractGame> getGames() {
        return Collections.unmodifiableSet(games);
    }

    public AbstractGame getGame(PlatformPlayer player) {
        return games.stream().filter(game -> game.contains(player)).findFirst().orElse(null);
    }

    public void register(AbstractGame game) {
        games.add(game);
    }

    public void unregister(AbstractGame game) {
        games.remove(game);
    }


}
