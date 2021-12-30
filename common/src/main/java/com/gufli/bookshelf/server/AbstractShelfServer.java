package com.gufli.bookshelf.server;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.event.Events;
import com.gufli.bookshelf.api.events.PlayerLoginEvent;
import com.gufli.bookshelf.api.events.PlayerQuitEvent;
import com.gufli.bookshelf.api.scheduler.Scheduler;
import com.gufli.bookshelf.api.server.ShelfServer;

import java.util.*;

public class AbstractShelfServer implements ShelfServer {

    private final Scheduler scheduler;

    protected final Set<ShelfPlayer> players = new HashSet<>();

    public AbstractShelfServer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public final Scheduler scheduler() {
        return scheduler;
    }

    @Override
    public final ShelfPlayer playerById(UUID uuid) {
        return players.stream().filter(p -> p.id().equals(uuid)).findFirst().orElse(null);
    }

    @Override
    public final ShelfPlayer playerByName(String name) {
        return players.stream().filter(p -> p.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public Collection<ShelfPlayer> players() {
        return Collections.unmodifiableCollection(players);
    }

    protected final void login(ShelfPlayer player) {
        players.add(player);
        scheduler.async().execute(() -> Events.call(new PlayerLoginEvent(player)));
    }

    protected final void quit(ShelfPlayer player) {
        players.remove(player);
        Events.call(new PlayerQuitEvent(player));
    }

}
