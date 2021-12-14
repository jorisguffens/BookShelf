package com.gufli.bookshelf.server;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.event.Events;
import com.gufli.bookshelf.api.events.PlayerLoginEvent;
import com.gufli.bookshelf.api.events.PlayerQuitEvent;
import com.gufli.bookshelf.api.scheduler.Scheduler;
import com.gufli.bookshelf.api.server.ShelfServer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AbstractShelfServer implements ShelfServer {

    private final Scheduler scheduler;

    protected final Set<ShelfPlayer> players = new HashSet<>();

    public AbstractShelfServer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public final Scheduler getScheduler() {
        return scheduler;
    }

    @Override
    public final ShelfPlayer getPlayer(UUID uuid) {
        return players.stream().filter(p -> p.getUniqueId().equals(uuid)).findFirst().orElse(null);
    }

    @Override
    public final ShelfPlayer getPlayer(String name) {
        return players.stream().filter(p -> p.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
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
