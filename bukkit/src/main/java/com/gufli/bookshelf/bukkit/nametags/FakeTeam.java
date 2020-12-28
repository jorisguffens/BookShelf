package com.gufli.bookshelf.bukkit.nametags;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FakeTeam {

    private final Set<String> players = new HashSet<>();

    private final String id;
    private final String prefix;
    private final String suffix;

    public FakeTeam(String id, String prefix, String suffix) {
        this.id = id;
        this.prefix = prefix == null ? "" : prefix;
        this.suffix = suffix == null ? "" : suffix;
    }

    public void addPlayer(String player) {
        players.add(player);
    }

    public void removePlayer(String player) {
        players.remove(player);
    }

    public Set<String> getPlayers() {
        return Collections.unmodifiableSet(players);
    }

    public String getId() { return id; }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public boolean isSimilar(String prefix, String suffix) {
        return this.prefix.equals(prefix) && this.suffix.equals(suffix);
    }

}