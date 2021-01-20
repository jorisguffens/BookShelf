package com.gufli.bookshelf.bukkit.sidebar.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PacketObjective {

    private static final int SIDEBAR_SLOT = 1;

    private final String objectiveId;
    private final Player player;

    private final Map<String, Integer> scores = new HashMap<>();
    private String displayName;

    public PacketObjective(Player player, String objectiveId, String displayName) {
        this.player = player;
        this.objectiveId = objectiveId;
        this.displayName = displayName;

        show();
    }

    public PacketObjective(Player player, String objectiveId) {
        this(player, objectiveId, objectiveId);
    }

    public String getId() {
        return objectiveId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        if (this.displayName.equals(displayName)) {
            return;
        }

        this.displayName = displayName;
        sendObjectivePacket(PacketState.UPDATE_DISPLAY_NAME);
    }

    public int getScore(String name, int def) {
        Integer score = scores.get(name);
        if (score == null) {
            setScore(name, def);
            return def;
        }

        return score;
    }

    public boolean hasScore(String name) {
        return scores.containsKey(name);
    }

    public void setScore(String name, int value) {
        Integer oldVal = scores.put(name, value);
        if (oldVal != null && oldVal == value) {
            return;
        }

        sendScorePacket(name, value, EnumWrappers.ScoreboardAction.CHANGE);
    }

    public Map<String, Integer> getScores() {
        return new HashMap<>(scores);
    }

    public void removeScore(String name) {
        scores.remove(name);
        sendScorePacket(name, 0, EnumWrappers.ScoreboardAction.REMOVE);
    }

    public void clear() {
        scores.keySet().forEach(this::removeScore);
    }

    public void destroy() {
        sendObjectivePacket(PacketState.REMOVE);
    }

    public void show() {
        sendObjectivePacket(PacketState.CREATE);
        sendShowPacket();
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectiveId);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PacketObjective && Objects.equals(objectiveId, ((PacketObjective) obj).objectiveId);
    }

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

        Class<?> criteria;

        try {
            criteria = MinecraftReflection.getMinecraftClass("IScoreboardCriteria$EnumScoreboardHealthDisplay");
        } catch (Exception ex) {
            try {
                criteria = MinecraftReflection.getMinecraftClass("EnumScoreboardHealthDisplay");
            } catch (Exception ex0) {
                return;
            }
        }

        packet.getModifier().write(2, criteria.getEnumConstants()[0]);

        if (state != PacketState.REMOVE) {
            packet.getChatComponents().write(0, WrappedChatComponent.fromText(displayName));
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
        if ( !player.isOnline() ) {
            return;
        }
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }
}