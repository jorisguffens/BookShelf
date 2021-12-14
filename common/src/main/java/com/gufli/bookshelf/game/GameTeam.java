package com.gufli.bookshelf.game;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class GameTeam {

    private final Set<ShelfPlayer> players = new CopyOnWriteArraySet<>();

    private final String name;
    private final String color;

    public GameTeam(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Set<ShelfPlayer> getPlayers() {
        return Collections.unmodifiableSet(players);
    }

    public boolean contains(ShelfPlayer player) {
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

    void addPlayer(ShelfPlayer player) {
        players.add(player);
    }

    void removePlayer(ShelfPlayer player) {
        players.remove(player);
    }

}
