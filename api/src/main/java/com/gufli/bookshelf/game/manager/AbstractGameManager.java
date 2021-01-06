package com.gufli.bookshelf.game.manager;

import com.gufli.bookshelf.entity.ShelfPlayer;
import com.gufli.bookshelf.game.Game;
import com.gufli.bookshelf.scheduler.Scheduler;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class AbstractGameManager<T extends Game> {

    private final Set<T> games = new CopyOnWriteArraySet<>();
    private final Scheduler scheduler;

    public AbstractGameManager(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public final Scheduler getScheduler() {
        return scheduler;
    }

    public Set<T> getGames() {
        return Collections.unmodifiableSet(games);
    }

    public T getGame(ShelfPlayer player) {
        return games.stream().filter(game -> game.contains(player)).findFirst().orElse(null);
    }

    public void register(T game) {
        games.add(game);
    }

    public void unregister(T game) {
        games.remove(game);
    }


}
