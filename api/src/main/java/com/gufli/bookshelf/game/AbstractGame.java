package com.gufli.bookshelf.game;

import com.gufli.bookshelf.arenas.Arena;
import com.gufli.bookshelf.entity.ShelfPlayer;
import com.gufli.bookshelf.events.EventManager;
import com.gufli.bookshelf.game.events.PlayerJoinGameEvent;
import com.gufli.bookshelf.game.events.PlayerLeaveGameEvent;
import com.gufli.bookshelf.game.exceptions.InvalidPlayerGameStatusException;
import com.gufli.bookshelf.game.exceptions.PlayerNotInGameException;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractGame implements Game {

    protected final Map<ShelfPlayer, PlayerGameStatus> players = new ConcurrentHashMap<>();

    private Arena arena;
    private GameStatus gameStatus = GameStatus.WAITING;

    @Override
    public Set<ShelfPlayer> getPlayers() {
        return Collections.unmodifiableSet(players.keySet());
    }

    @Override
    public boolean contains(ShelfPlayer player) {
        return players.containsKey(player);
    }

    public void addPlayer(ShelfPlayer player) {
        players.put(player, PlayerGameStatus.WAITING);
        EventManager.dispatch(new PlayerJoinGameEvent(this, player));
    }

    public void removePlayer(ShelfPlayer player) {
        players.remove(player);
        EventManager.dispatch(new PlayerLeaveGameEvent(this, player));
    }

    @Override
    public PlayerGameStatus getStatus(ShelfPlayer player) {
        return players.get(player);
    }

    public void setStatus(ShelfPlayer player, PlayerGameStatus status) {
        if ( !contains(player) ) {
            throw new PlayerNotInGameException("That player is not registered with this game instance.");
        }
        if ( status == null ) {
            throw new InvalidPlayerGameStatusException("The status must not be null.");
        }
        players.put(player, status);
    }

    @Override
    public GameStatus getStatus() {
        return gameStatus;
    }

    public void setStatus(GameStatus status) {
        this.gameStatus = status;
    }

    @Override
    public void broadcast(String msg) {
        players.keySet().forEach(p -> p.sendMessage(msg));
    }

    @Override
    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    protected abstract void start();
}
