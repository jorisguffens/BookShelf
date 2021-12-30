package com.gufli.bookshelf.bukkit.nametags;

import com.gufli.bookshelf.api.events.ShelfShutdownEvent;
import com.gufli.bookshelf.api.server.Bookshelf;
import com.gufli.bookshelf.bukkit.packets.AbstractPacket;
import com.gufli.bookshelf.bukkit.packets.WrapperPlayServerScoreboardTeam;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.gufli.bookshelf.bukkit.api.entity.BukkitPlayer;
import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.event.Events;
import com.gufli.bookshelf.api.events.PlayerJoinEvent;
import com.gufli.bookshelf.api.events.PlayerQuitEvent;
import com.gufli.bookshelf.api.nametags.NametagManager;
import com.gufli.bookshelf.api.nametags.Nametags;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
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

        Events.subscribe(ShelfShutdownEvent.class)
                .handler(e -> Bookshelf.players().forEach(this::removeNametag));
    }

    @Override
    public void changeNametag(ShelfPlayer player, String prefix, String suffix) {
        if ( prefix != null ) {
            prefix = ChatColor.translateAlternateColorCodes('&', prefix);
        }
        if ( suffix != null ) {
            suffix = ChatColor.translateAlternateColorCodes('&', suffix);
        }

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
            joining.addMember(player.name());
            addPlayer(joining, player.name());
        }
        // Team doesn't exist
        else {
            joining = new FakeTeam(UNIQUEID + (COUNTER++), prefix, suffix);
            showAll(joining);

            joining.addMember(player.name());
            addPlayer(joining, player.name());

            fakeTeams.add(joining);
        }
    }

    @Override
    public void changePrefix(ShelfPlayer player, String prefix) {
        FakeTeam previous = getFakeTeam(player);
        if ( previous != null ) {
            changeNametag(player, prefix, previous.suffix());
            return;
        }
        changeNametag(player, prefix, "");
    }

    @Override
    public void changeSuffix(ShelfPlayer player, String suffix) {
        FakeTeam previous = getFakeTeam(player);
        if ( previous != null ) {
            changeNametag(player, previous.prefix(), suffix);
            return;
        }
        changeNametag(player, "", suffix);
    }

    @Override
    public void removeNametag(ShelfPlayer player) {
        FakeTeam team = getFakeTeam(player);
        if ( team == null ) {
            return;
        }

        team.removeMember(player.name());
        removePlayer(team, player.name());

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
        return fakeTeams.stream().filter(t -> t.members().contains(player.name()))
                .findFirst().orElse(null);
    }

    private void showAll(ShelfPlayer player) {
        Player p = ((BukkitPlayer) player).handle();
        for ( FakeTeam team : fakeTeams ) {
            showFor(team, p);
        }
    }

    // packets

    private void hide(FakeTeam team) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);
        packet.getModifier().writeDefaults();
        packet.getStrings().write(0, team.id());
        packet.getIntegers().write(0, 1); // 1 = TEAM REMOVED
        ProtocolLibrary.getProtocolManager().broadcastServerPacket(packet);
    }

    private void showFor(FakeTeam team, Player player) {
        showPacket(team).sendPacket(player);
        addPlayers(team, new ArrayList<>(team.members()));
    }

    private void showAll(FakeTeam team) {
        showPacket(team).broadcastPacket();
    }

    private void removePlayer(FakeTeam team, String playerName) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);
        packet.getModifier().writeDefaults();
        packet.getStrings().write(0, team.id());
        packet.getIntegers().write(0, 4); // 1 = PLAYERS REMOVED
        packet.getSpecificModifier(Collection.class).write(0, Collections.singletonList(playerName));
        ProtocolLibrary.getProtocolManager().broadcastServerPacket(packet);
    }

    private void addPlayer(FakeTeam team, String playerName) {
        addPlayers(team, Collections.singletonList(playerName));
    }

    private void addPlayers(FakeTeam team, List<String> players) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);
        packet.getModifier().writeDefaults();
        packet.getStrings().write(0, team.id());
        packet.getIntegers().write(0, 3); // 1 = PLAYERS ADDED
        packet.getSpecificModifier(Collection.class).write(0, players);
        ProtocolLibrary.getProtocolManager().broadcastServerPacket(packet);
    }

    private AbstractPacket showPacket(FakeTeam team) {
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

        WrapperPlayServerScoreboardTeam packet = new WrapperPlayServerScoreboardTeam(team.id());
        packet.setColor(color);
        packet.setDisplayName(WrappedChatComponent.fromText(team.id()));
        packet.setPrefix(WrappedChatComponent.fromText(team.prefix()));
        packet.setSuffix(WrappedChatComponent.fromText(color + team.suffix()));
        return packet;
    }
}
