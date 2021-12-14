package com.gufli.bookshelf.api.server;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.scheduler.Scheduler;

import java.util.UUID;

public interface ShelfServer {

    Scheduler getScheduler();

    ShelfPlayer getPlayer(UUID uuid);

    ShelfPlayer getPlayer(String name);

}
