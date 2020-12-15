package com.gufli.bookshelf.server;

import com.gufli.bookshelf.entity.PlatformPlayer;

import java.util.UUID;

public interface PlatformServer {

    PlatformPlayer getPlayer(UUID uuid);

    PlatformPlayer getPlayer(String name);

}
