package com.gufli.bookshelf.server;

import com.gufli.bookshelf.entity.PlatformPlayer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AbstractServer {

    protected final Set<PlatformPlayer> players = new HashSet<>();

    public PlatformPlayer getPlayer(UUID uuid) {
        return players.stream().filter(p -> p.getUniqueId().equals(uuid)).findFirst().orElse(null);
    }

    public PlatformPlayer getPlayer(String name) {
        return players.stream().filter(p -> p.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}
