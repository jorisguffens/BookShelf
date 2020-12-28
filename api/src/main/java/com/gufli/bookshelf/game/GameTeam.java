package com.gufli.bookshelf.game;

import com.gufli.bookshelf.entity.PlatformPlayer;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class GameTeam {

    private final Set<PlatformPlayer> players = new CopyOnWriteArraySet<>();

    private final String name;
    private final String color;

    public GameTeam(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Set<PlatformPlayer> getPlayers() {
        return Collections.unmodifiableSet(players);
    }

    public boolean contains(PlatformPlayer player) {
        return players.contains(player);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public void broadcast(String msg) {
        players.forEach(p -> p.sendMessage(msg));
    }

    //

    void addPlayer(PlatformPlayer player) {
        players.add(player);
    }

    void removePlayer(PlatformPlayer player) {
        players.remove(player);
    }

}
