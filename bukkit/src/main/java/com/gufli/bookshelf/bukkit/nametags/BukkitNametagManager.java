package com.gufli.bookshelf.bukkit.nametags;

import com.comphenix.packetwrapper.AbstractPacket;
import com.comphenix.packetwrapper.WrapperPlayServerScoreboardTeam;
import com.comphenix.packetwrapper.WrapperPlayServerScoreboardTeam_v1_17;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.gufli.bookshelf.bukkit.entity.BukkitPlayer;
import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.bukkit.api.reflection.Reflection;
import com.gufli.bookshelf.api.event.Events;
import com.gufli.bookshelf.api.events.PlayerJoinEvent;
import com.gufli.bookshelf.api.events.PlayerQuitEvent;
import com.gufli.bookshelf.api.nametags.NametagManager;
import com.gufli.bookshelf.api.nametags.Nametags;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class BukkitNametagManager implements NametagManager {

    private final static String UNIQUEID = "BSNTGS";
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
        if ((prefix == null || prefix.equals("")) && (suffix == null || suffix.equals("")) ) {
            return;
        }

        FakeTeam joining = getFakeTeam(prefix, suffix);
        // Team already exists
        if (joining != null) {
            joining.addMember(player.getName());
            addPlayer(joining, player.getName());
        }
        // Team doesn't exist
        else {
            joining = new FakeTeam(UNIQUEID + (COUNTER++), prefix, suffix);
            showAll(joining);

            joining.addMember(player.getName());
            addPlayer(joining, player.getName());

            fakeTeams.add(joining);
        }
    }

    @Override
    public void setPrefix(ShelfPlayer player, String prefix) {
        FakeTeam previous = getFakeTeam(player);
        if ( previous != null ) {
            setNametag(player, prefix, previous.suffix());
            return;
        }
        setNametag(player, prefix, "");
    }

    @Override
    public void setSuffix(ShelfPlayer player, String suffix) {
        FakeTeam previous = getFakeTeam(player);
        if ( previous != null ) {
            setNametag(player, previous.prefix(), suffix);
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

        team.removeMember(player.getName());
        removePlayer(team, player.getName());

        // team is empty -> delete
        if ( team.members().size() == 0 ) {
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
        return fakeTeams.stream().filter(t -> t.members().contains(player.getName()))
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
        packet.setName(team.id());
        packet.broadcastPacket();
    }

    private void showFor(FakeTeam team, Player player) {
        showPacket(team).sendPacket(player);
        addPlayers(team, new ArrayList<>(team.members()));
    }

    private void showAll(FakeTeam team) {
        showPacket(team).broadcastPacket();
    }

    private void removePlayer(FakeTeam team, String playerName) {
        WrapperPlayServerScoreboardTeam packet = new WrapperPlayServerScoreboardTeam();
        packet.setMode(WrapperPlayServerScoreboardTeam.Mode.PLAYERS_REMOVED);
        packet.setName(team.id());
        packet.setPlayers(Collections.singletonList(playerName));
        packet.broadcastPacket();
    }

    private void addPlayer(FakeTeam team, String playerName) {
        addPlayers(team, Collections.singletonList(playerName));
    }

    private void addPlayers(FakeTeam team, List<String> players) {
        WrapperPlayServerScoreboardTeam packet = new WrapperPlayServerScoreboardTeam();
        packet.setMode(WrapperPlayServerScoreboardTeam.Mode.PLAYERS_ADDED);
        packet.setName(team.id());
        packet.setPlayers(players);
        packet.broadcastPacket();
    }

    private AbstractPacket showPacket(FakeTeam team) {
        WrapperPlayServerScoreboardTeam packet;

        if ( Reflection.isMcGreaterOrEqualTo("1.17") ) {
            packet = new WrapperPlayServerScoreboardTeam_v1_17(team.id());
        } else {
            packet = new WrapperPlayServerScoreboardTeam();
        }

        packet.setMode(WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED);
        packet.setName(team.id());

        // find last color
        String lastColors = ChatColor.getLastColors(team.prefix());
        ChatColor color = ChatColor.WHITE;
        while ( lastColors.length() >= 2 ) {
            ChatColor c = ChatColor.getByChar(lastColors.substring(lastColors.length() - 1));
            lastColors = lastColors.substring(0, lastColors.length() - 2);
            if ( c != null && c.isColor() ) {
                color = c;
                break;
            }
        }

        packet.setColor(color);
        packet.setDisplayName(WrappedChatComponent.fromText(team.id()));
        packet.setPrefix(WrappedChatComponent.fromText(team.prefix()));
        packet.setSuffix(WrappedChatComponent.fromText(color + team.suffix()));

        return packet;
    }
}
