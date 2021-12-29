package com.gufli.bookshelf.bukkit.game;

import com.gufli.bookshelf.api.event.Events;
import com.gufli.bookshelf.api.events.PlayerDeathEvent;
import com.gufli.bookshelf.game.Game;
import com.gufli.bookshelf.game.GameTeam;
import com.gufli.bookshelf.game.TeamGame;
import com.gufli.bookshelf.game.events.PlayerJoinGameEvent;
import com.gufli.bookshelf.game.events.PlayerLeaveGameEvent;
import com.gufli.bookshelf.game.manager.GameManager;
import com.gufli.bookshelf.api.util.terminable.Terminable;
import com.gufli.bookshelf.api.util.terminable.composite.CompositeTerminable;
import org.bukkit.ChatColor;

public class DefaultGameEvents {

    public static Terminable register(Game game) {
        return CompositeTerminable.create().withAll(
                Events.subscribe(PlayerJoinGameEvent.class).filter(e -> e.getGame() == game).handler(DefaultGameEvents::onJoin),
                Events.subscribe(PlayerLeaveGameEvent.class).filter(e -> e.getGame() == game).handler(DefaultGameEvents::onLeave),
                Events.subscribe(PlayerDeathEvent.class).filter(e -> game.contains(e.getPlayer())).handler(DefaultGameEvents::onKill)
            );
    }

    private static void onJoin(PlayerJoinGameEvent event) {
        event.getGame().broadcast(ChatColor.GOLD + event.getPlayer().name() + ChatColor.YELLOW + " has joined the game!");
    }

    private static void onLeave(PlayerLeaveGameEvent event) {
        event.getGame().broadcast(ChatColor.GOLD + event.getPlayer().name() + ChatColor.YELLOW + " has left the game!");
    }

    private static void onKill(PlayerDeathEvent event) {
        Game game = GameManager.getGame(event.getPlayer());
        if ( game == null ) {
            return;
        }

        event.setDeathMessage(null);

        if ( game instanceof TeamGame) {
            GameTeam team = ((TeamGame<?>) game).getTeam(event.getPlayer());
            String color = team == null ? ChatColor.GOLD + "" : team.getColor();

            if ( event.getKiller() == null ) {
                game.broadcast(color + event.getPlayer().name() + ChatColor.YELLOW + " has died!");
                return;
            }

            GameTeam kteam = ((TeamGame<?>) game).getTeam(event.getKiller());
            String kcolor = kteam == null ? ChatColor.GOLD + "" : kteam.getColor();

            game.broadcast(color + event.getPlayer().name() + ChatColor.YELLOW + " was killed by "
                    + kcolor + event.getKiller().name() + ChatColor.YELLOW + "!");
        } else {
            if (event.getKiller() == null) {
                game.broadcast(ChatColor.GOLD + event.getPlayer().name() + ChatColor.YELLOW + " has died!");
                return;
            }

            game.broadcast(ChatColor.GOLD + event.getPlayer().name() + ChatColor.YELLOW + " was killed by "
                    + ChatColor.GOLD + event.getKiller().name() + ChatColor.YELLOW + "!");
        }
    }

}
