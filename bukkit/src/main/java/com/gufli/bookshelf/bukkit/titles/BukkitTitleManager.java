package com.gufli.bookshelf.bukkit.titles;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.titles.TitleManager;
import com.gufli.bookshelf.api.titles.TitleType;
import com.gufli.bookshelf.api.titles.Titles;
import com.gufli.bookshelf.bukkit.api.entity.BukkitPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class BukkitTitleManager implements TitleManager {

    public BukkitTitleManager() {
        Titles.register(this);
    }

    @Override
    public void sendTitle(ShelfPlayer player, String text, TitleType type, float seconds) {
        Player p = ((BukkitPlayer) player).getHandle();
        text = ChatColor.translateAlternateColorCodes('&', text);

        if (type == TitleType.BIG) {
            sendTitle(p, text, (int) (seconds * 20), PacketType.Play.Server.SET_TITLE_TEXT);
            return;
        }

        if (type == TitleType.MEDIUM) {
            sendTitle(p, text, (int) (seconds * 20), PacketType.Play.Server.SET_SUBTITLE_TEXT);
            return;
        }

        if (type == TitleType.SMALL) {
            sendTitle(p, text, (int) (seconds * 20), PacketType.Play.Server.SET_ACTION_BAR_TEXT);
        }
    }

    private void sendTitle(Player player, String text, int ticks, PacketType type) {
        System.out.println("sending packets " + ticks + " '" + text + "'");
        PacketContainer animationPacket = new PacketContainer(PacketType.Play.Server.SET_TITLES_ANIMATION);
        animationPacket.getModifier().writeDefaults();
        animationPacket.getIntegers().write(0, 10);
        animationPacket.getIntegers().write(1, ticks);
        animationPacket.getIntegers().write(2, 10);
        sendPacket(player, animationPacket);

        PacketContainer textPacket = new PacketContainer(type);
        textPacket.getModifier().writeDefaults();
        textPacket.getChatComponents().write(0, WrappedChatComponent.fromText(text));
        sendPacket(player, textPacket);
    }

    private void sendPacket(Player player, PacketContainer packet) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Cannot send packet.", e);
        }
    }

}
