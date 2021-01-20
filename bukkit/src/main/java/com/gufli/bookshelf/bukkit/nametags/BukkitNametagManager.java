package com.gufli.bookshelf.bukkit.nametags;

import com.comphenix.packetwrapper.AbstractPacket;
import com.comphenix.packetwrapper.WrapperPlayServerScoreboardTeam;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.gufli.bookshelf.bukkit.entity.BukkitPlayer;
import com.gufli.bookshelf.entity.ShelfPlayer;
import com.gufli.bookshelf.event.Events;
import com.gufli.bookshelf.events.PlayerJoinEvent;
import com.gufli.bookshelf.events.PlayerQuitEvent;
import com.gufli.bookshelf.nametags.NametagManager;
import com.gufli.bookshelf.nametags.Nametags;
import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class BukkitNametagManager implements NametagManager {

    private final String UNIQUEID = RandomStringUtils.randomAlphanumeric(3);
    private long COUNTER = 0;

    private final Set<FakeTeam> fakeTeams = new CopyOnWriteArraySet<>();

    public BukkitNametagManager() {
        Nametags.register(this);

        Events.subscribe(PlayerJoinEvent.class)
                .handler(e -> showAll(e.getPlayer()));

        Events.subscribe(PlayerQuitEvent.class)
                .handler(e -> removeNametag(e.getPlayer()));
    }

    @Override
    public void setNametag(ShelfPlayer player, String prefix, String suffix) {
        // If player is already in the team -> ignore
        FakeTeam previous = getFakeTeam(player);
        if ( previous != null && previous.isSimilar(prefix, suffix) ) {
            return;
        }

        // Remove from old team
        removeNametag(player);

        FakeTeam joining = getFakeTeam(prefix, suffix);

        // Team already exists
        if (joining != null) {
            joining.addPlayer(player.getName());
            addPlayer(joining, player.getName());
        }
        // Team doesn't exist
        else {
            joining = new FakeTeam(UNIQUEID + (COUNTER++) + "", prefix, suffix);
            showAll(joining);

            joining.addPlayer(player.getName());
            addPlayer(joining, player.getName());

            fakeTeams.add(joining);
        }
    }

    @Override
    public void setPrefix(ShelfPlayer player, String prefix) {
        FakeTeam previous = getFakeTeam(player);
        if ( previous != null ) {
            setNametag(player, prefix, previous.getSuffix());
            return;
        }
        setNametag(player, prefix, "");
    }

    @Override
    public void setSuffix(ShelfPlayer player, String suffix) {
        FakeTeam previous = getFakeTeam(player);
        if ( previous != null ) {
            setNametag(player, previous.getPrefix(), suffix);
            return;
        }
        setNametag(player, "", suffix);
    }

    @Override
    public void removeNametag(ShelfPlayer player) {
        FakeTeam team = getFakeTeam(player);
        if ( team == null ) {
            return;
        }

        team.removePlayer(player.getName());
        removePlayer(team, player.getName());

        // team is empty -> delete
        if ( team.getPlayers().size() == 0 ) {
            hide(team);
            fakeTeams.remove(team);
        }
    }

    //

    private FakeTeam getFakeTeam(String prefix, String suffix) {
        return fakeTeams.stream().filter(t -> t.isSimilar(prefix, suffix))
                .findFirst().orElse(null);
    }

    private FakeTeam getFakeTeam(ShelfPlayer player) {
        return fakeTeams.stream().filter(t -> t.getPlayers().contains(player.getName()))
                .findFirst().orElse(null);
    }

    private void showAll(ShelfPlayer player) {
        Player p = ((BukkitPlayer) player).getPlayer();
        for ( FakeTeam team : fakeTeams ) {
            showFor(team, p);
        }
    }

    // packets

    private void hide(FakeTeam team) {
        WrapperPlayServerScoreboardTeam packet = new WrapperPlayServerScoreboardTeam();
        packet.setMode(WrapperPlayServerScoreboardTeam.Mode.TEAM_REMOVED);
        packet.setName(team.getId());
        packet.broadcastPacket();
    }

    private void showFor(FakeTeam team, Player player) {
        createShowPacket(team).sendPacket(player);
        addPlayers(team, new ArrayList<>(team.getPlayers()));
    }

    private void showAll(FakeTeam team) {
        createShowPacket(team).broadcastPacket();
    }

    private void removePlayer(FakeTeam team, String playerName) {
        WrapperPlayServerScoreboardTeam packet = new WrapperPlayServerScoreboardTeam();
        packet.setMode(WrapperPlayServerScoreboardTeam.Mode.PLAYERS_REMOVED);
        packet.setName(team.getId());
        packet.setPlayers(Collections.singletonList(playerName));
        packet.broadcastPacket();
    }

    private void addPlayer(FakeTeam team, String playerName) {
        addPlayers(team, Collections.singletonList(playerName));
    }

    private void addPlayers(FakeTeam team, List<String> players) {
        WrapperPlayServerScoreboardTeam packet = new WrapperPlayServerScoreboardTeam();
        packet.setMode(WrapperPlayServerScoreboardTeam.Mode.PLAYERS_ADDED);
        packet.setName(team.getId());
        packet.setPlayers(players);
        packet.broadcastPacket();
    }

    private AbstractPacket createShowPacket(FakeTeam team) {
        WrapperPlayServerScoreboardTeam packet = new WrapperPlayServerScoreboardTeam();
        packet.setMode(WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED);
        packet.setName(team.getId());

        String lastColors = ChatColor.getLastColors(team.getPrefix());
        ChatColor color = null;
        if ( !lastColors.isEmpty() ) {
            color = ChatColor.getByChar(lastColors.substring(lastColors.length() - 1));
        }

        if ( color != null ) {
            packet.setColor(color);
        } else {
            packet.setColor(ChatColor.RESET);
        }

        packet.setDisplayName(WrappedChatComponent.fromText(team.getId()));
        packet.setPrefix(WrappedChatComponent.fromText(team.getPrefix()));
        packet.setSuffix(WrappedChatComponent.fromText((color != null ? color : "") + team.getSuffix()));

        return packet;
    }
}
