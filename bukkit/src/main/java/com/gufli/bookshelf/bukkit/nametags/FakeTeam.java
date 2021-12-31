package com.gufli.bookshelf.bukkit.nametags;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.gufli.bookshelf.bukkit.packets.AbstractPacket;
import com.gufli.bookshelf.bukkit.packets.WrapperPlayServerScoreboardTeam;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class FakeTeam {

    private final Set<String> members = new HashSet<>();

    private final String id;
    private final String prefix;
    private final String suffix;

    public FakeTeam(String id, String prefix, String suffix) {
        this.id = id;
        this.prefix = prefix == null ? "" : prefix;
        this.suffix = suffix == null ? "" : suffix;
    }

    public Set<String> members() {
        return Collections.unmodifiableSet(members);
    }

    public String id() { return id; }

    public String prefix() {
        return prefix;
    }

    public String suffix() {
        return suffix;
    }

    public boolean isSimilar(String prefix, String suffix) {
        return this.prefix.equals(prefix) && this.suffix.equals(suffix);
    }

    // packets

    public void hide() {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);
        packet.getModifier().writeDefaults();
        packet.getStrings().write(0, id());
        packet.getIntegers().write(0, 1); // 1 = TEAM REMOVED
        ProtocolLibrary.getProtocolManager().broadcastServerPacket(packet);
    }

    public void showFor(Player player) {
        showPacket().sendPacket(player);
        addPlayers(new ArrayList<>(members()));
    }

    public void showAll() {
        showPacket().broadcastPacket();
    }

    public void removePlayer(String playerName) {
        members.remove(playerName);
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);
        packet.getModifier().writeDefaults();
        packet.getStrings().write(0, id());
        packet.getIntegers().write(0, 4); // 1 = PLAYERS REMOVED
        packet.getSpecificModifier(Collection.class).write(0, Collections.singletonList(playerName));
        ProtocolLibrary.getProtocolManager().broadcastServerPacket(packet);
    }

    public void addPlayer(String playerName) {
        members.add(playerName);
        addPlayers(Collections.singletonList(playerName));
    }

    public void addPlayers(List<String> players) {
        members.addAll(players);
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);
        packet.getModifier().writeDefaults();
        packet.getStrings().write(0, id());
        packet.getIntegers().write(0, 3); // 1 = PLAYERS ADDED
        packet.getSpecificModifier(Collection.class).write(0, players);
        ProtocolLibrary.getProtocolManager().broadcastServerPacket(packet);
    }

    private AbstractPacket showPacket() {
        // find last color
        String lastColors = ChatColor.getLastColors(prefix());
        ChatColor color = ChatColor.WHITE;
        while ( lastColors.length() >= 2 ) {
            ChatColor c = ChatColor.getByChar(lastColors.substring(lastColors.length() - 1));
            lastColors = lastColors.substring(0, lastColors.length() - 2);
            if ( c != null && c.isColor() ) {
                color = c;
                break;
            }
        }

        WrapperPlayServerScoreboardTeam packet = new WrapperPlayServerScoreboardTeam(id());
        packet.setColor(color);
        packet.setDisplayName(WrappedChatComponent.fromText(id()));
        packet.setPrefix(WrappedChatComponent.fromText(prefix()));
        packet.setSuffix(WrappedChatComponent.fromText(color + suffix()));
        return packet;
    }

}