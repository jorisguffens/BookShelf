package com.gufli.bookshelf.server;

import com.gufli.bookshelf.entity.ShelfPlayer;
import com.gufli.bookshelf.events.EventManager;
import com.gufli.bookshelf.events.defaults.PlayerLoginEvent;
import com.gufli.bookshelf.events.defaults.PlayerQuitEvent;
import com.gufli.bookshelf.scheduler.Scheduler;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AbstractShelfServer {

    private final Scheduler scheduler;

    protected final Set<ShelfPlayer> players = new HashSet<>();

    public AbstractShelfServer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public final Scheduler getScheduler() {
        return scheduler;
    }

    public final ShelfPlayer getPlayer(UUID uuid) {
        return players.stream().filter(p -> p.getUniqueId().equals(uuid)).findFirst().orElse(null);
    }

    public final ShelfPlayer getPlayer(String name) {
        return players.stream().filter(p -> p.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    protected final void login(ShelfPlayer player) {
        players.add(player);
        scheduler.async().execute(() -> EventManager.dispatch(new PlayerLoginEvent(player)));
    }

    protected final void quit(ShelfPlayer player) {
        players.remove(player);
        EventManager.dispatch(new PlayerQuitEvent(player));
    }

}
