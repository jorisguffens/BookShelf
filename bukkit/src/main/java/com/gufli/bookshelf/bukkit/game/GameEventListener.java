package com.gufli.bookshelf.bukkit.game;

import com.gufli.bookshelf.events.Event;
import com.gufli.bookshelf.events.defaults.PlayerDeathEvent;
import com.gufli.bookshelf.game.*;
import com.gufli.bookshelf.game.events.PlayerJoinGameEvent;
import com.gufli.bookshelf.game.events.PlayerLeaveGameEvent;
import com.gufli.bookshelf.game.manager.GameManager;
import org.bukkit.ChatColor;

public class GameEventListener implements com.gufli.bookshelf.events.EventListener {

    @Event
    public void onJoin(PlayerJoinGameEvent event) {
        event.getGame().broadcast(ChatColor.GOLD + event.getPlayer().getName() + ChatColor.YELLOW + " has joined the game!");
    }

    @Event
    public void onLeave(PlayerLeaveGameEvent event) {
        event.getGame().broadcast(ChatColor.GOLD + event.getPlayer().getName() + ChatColor.YELLOW + " has left the game!");
    }

    @Event
    public void onKill(PlayerDeathEvent event) {
        Game game = GameManager.getGame(event.getPlayer());
        if ( game == null ) {
            return;
        }

        event.setDeathMessage(null);

        if ( game instanceof TeamGame) {
            GameTeam team = ((TeamGame<?>) game).getTeam(event.getPlayer());
            String color = team == null ? ChatColor.GOLD + "" : team.getColor();

            if ( event.getKiller() == null ) {
                game.broadcast(color + event.getPlayer().getName() + ChatColor.YELLOW + " has died!");
                return;
            }

            GameTeam kteam = ((TeamGame<?>) game).getTeam(event.getKiller());
            String kcolor = kteam == null ? ChatColor.GOLD + "" : kteam.getColor();

            game.broadcast(color + event.getPlayer().getName() + ChatColor.YELLOW + " was killed by "
                    + kcolor + event.getKiller().getName() + ChatColor.YELLOW + "!");
        } else {
            if (event.getKiller() == null) {
                game.broadcast(ChatColor.GOLD + event.getPlayer().getName() + ChatColor.YELLOW + " has died!");
                return;
            }

            game.broadcast(ChatColor.GOLD + event.getPlayer().getName() + ChatColor.YELLOW + " was killed by "
                    + ChatColor.GOLD + event.getKiller().getName() + ChatColor.YELLOW + "!");
        }
    }

}
