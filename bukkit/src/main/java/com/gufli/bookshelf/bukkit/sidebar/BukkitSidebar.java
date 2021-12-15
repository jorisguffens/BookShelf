package com.gufli.bookshelf.bukkit.sidebar;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.sidebar.SidebarTemplate;
import com.gufli.bookshelf.bukkit.api.entity.BukkitPlayer;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

public class BukkitSidebar {

    private static final int SIDEBAR_SLOT = 1;

    //
    private final BukkitPlayer player;

    private final String objectiveId;

    private final String title;
    private final List<String> template;
    private final BiConsumer<ShelfPlayer, List<String>> updater;

    // these things change
    private final List<String> contents = new CopyOnWriteArrayList<>();
    private final Map<String, Integer> scores = new HashMap<>();

    public BukkitSidebar(BukkitPlayer player, SidebarTemplate template) {
        this.player = player;
        this.objectiveId = RandomStringUtils.randomAlphabetic(15);

        this.title = ChatColor.translateAlternateColorCodes('&', template.title());
        this.template = new ArrayList<>(template.contents());
        this.updater = template.updater();

        show();
        updateLines();
    }

    private void updateLines() {
        for ( int i = 0; i < template.size(); i++ ) {
            String line = template.get(i);
            line = ChatColor.translateAlternateColorCodes('&', line);
            line = StringEscapeUtils.unescapeJava(line);
            line = String.format("%-10s", line); // min width of sidebar = 10
            template.set(i, line);
        }
    }

    public void update() {
        List<String> contents = new ArrayList<>(template);

        // update lines (with variables)
        if ( updater != null ) {
            updater.accept(player, contents);
        }

        // also show duplicate lines
        List<String> checked = new ArrayList<>();
        for ( String line : contents ) {
            if ( line.length() > 40 ) {
                line = line.substring(0, 40);
            }
            while ( checked.contains(line) ) {
                line += " ";
            }
            checked.add(line);
        }
        contents = checked;

        // remove lines that no longer exist
        for ( String line : this.contents ) {
            if ( !contents.contains(line) ) {
                removeScore(line);
            }
        }

        // set & update lines with the new score order
        this.contents.clear();
        int i = contents.size() - 1;
        for ( String s : contents) {
            this.contents.add(s);
            setScore(s, i);
            i--;
        }
    }

    // OBJECTIVE

    public void setScore(String name, int value) {
        Integer oldVal = scores.put(name, value);
        if (oldVal != null && oldVal == value) {
            return;
        }

        sendScorePacket(name, value, EnumWrappers.ScoreboardAction.CHANGE);
    }

    public void removeScore(String name) {
        scores.remove(name);
        sendScorePacket(name, 0, EnumWrappers.ScoreboardAction.REMOVE);
    }

    public void clear() {
        scores.keySet().forEach(this::removeScore);
    }

    public void destroy() {
        this.contents.forEach(this::removeScore);
        sendObjectivePacket(PacketState.REMOVE);
    }

    public void show() {
        // remove scores before re-adding them
        this.contents.forEach(this::removeScore);

        sendObjectivePacket(PacketState.CREATE);
        sendShowPacket();
        update();
    }

    // PACKETS

    private void sendScorePacket(String name, int score, EnumWrappers.ScoreboardAction action) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SCOREBOARD_SCORE);
        packet.getStrings().write(0, name);
        packet.getStrings().write(1, objectiveId);

        packet.getIntegers().write(0, score);

        packet.getScoreboardActions().write(0, action);

        sendPacket(packet);
    }

    private void sendObjectivePacket(PacketState state) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SCOREBOARD_OBJECTIVE);
        packet.getStrings().write(0, objectiveId);
        packet.getIntegers().write(0, state.ordinal());
        packet.getEnumModifier(EnumScoreboardHealthDisplay.class, 2).write(0, EnumScoreboardHealthDisplay.INTEGER);

        if (state != PacketState.REMOVE) {
            packet.getChatComponents().write(0, WrappedChatComponent.fromText(title));
        }

        sendPacket(packet);
    }

    private void sendShowPacket() {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SCOREBOARD_DISPLAY_OBJECTIVE);
        packet.getStrings().write(0, objectiveId);
        packet.getIntegers().write(0, SIDEBAR_SLOT);
        sendPacket(packet);
    }

    private void sendPacket(PacketContainer packet) {
        if ( !player.getHandle().isOnline() ) {
            return;
        }
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player.getHandle(), packet);
        } catch (InvocationTargetException ignore) {}
    }

    private enum EnumScoreboardHealthDisplay {
        INTEGER,
        HEARTS
    }


    public enum PacketState {
        CREATE,
        REMOVE,
        UPDATE_DISPLAY_NAME;
    }

}
