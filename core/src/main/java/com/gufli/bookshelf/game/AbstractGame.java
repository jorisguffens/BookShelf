package com.gufli.bookshelf.game;

import com.gufli.bookshelf.arenas.Arena;
import com.gufli.bookshelf.entity.ShelfPlayer;
import com.gufli.bookshelf.event.Events;
import com.gufli.bookshelf.game.events.GameStartEvent;
import com.gufli.bookshelf.game.events.GameStopEvent;
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

    public final void addPlayer(ShelfPlayer player) {
        players.put(player, PlayerGameStatus.WAITING);
        onJoin(player);
        Events.call(new PlayerJoinGameEvent(this, player));
    }

    public final void removePlayer(ShelfPlayer player) {
        players.remove(player);
        onLeave(player);
        Events.call(new PlayerLeaveGameEvent(this, player));
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

    public final void start() {
        if ( getStatus() == GameStatus.STARTED ) {
            return;
        }
        setStatus(GameStatus.STARTED);

        onStart();
        Events.call(new GameStartEvent(this));
    }

    public final void stop() {
        if ( getStatus() == GameStatus.FINISHED ) {
            return;
        }
        setStatus(GameStatus.FINISHED);

        onStop();
        Events.call(new GameStopEvent(this));
    }

    protected void onStart() {}

    protected void onStop() {}

    protected void onJoin(ShelfPlayer player) {}

    protected void onLeave(ShelfPlayer player) {}

}
