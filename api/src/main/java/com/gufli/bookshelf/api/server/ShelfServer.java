package com.gufli.bookshelf.api.server;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.scheduler.Scheduler;

import java.util.Collection;
import java.util.UUID;

public interface ShelfServer {

    Scheduler scheduler();

    ShelfPlayer playerById(UUID uuid);

    ShelfPlayer playerByName(String name);

    Collection<ShelfPlayer> players();

}
