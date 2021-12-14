package com.gufli.bookshelf.bukkit.bossbar;

import com.comphenix.packetwrapper.WrapperPlayServerBoss;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.gufli.bookshelf.api.color.Color;
import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.bukkit.api.bossbar.Bossbar;
import com.gufli.bookshelf.bukkit.entity.BukkitPlayer;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitBossbar {

    private final UUID uuid = UUID.randomUUID();
    private final Bossbar bossbar;
    private final ShelfPlayer player;

    public BukkitBossbar(ShelfPlayer player, Bossbar bossbar) {
        this.player = player;
        this.bossbar = bossbar;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Bossbar getBossbar() {
        return bossbar;
    }

    public ShelfPlayer getPlayer() {
        return player;
    }

    void show() {
        WrapperPlayServerBoss packet = new WrapperPlayServerBoss();
        packet.setAction(WrapperPlayServerBoss.Action.ADD);
        packet.setUniqueId(uuid);
        packet.setTitle(WrappedChatComponent.fromText(bossbar.getText()));
        packet.setHealth(bossbar.getPercent());
        packet.setColor(fromColor(bossbar.getColor()));
        packet.sendPacket(((BukkitPlayer) player).getPlayer());
    }

    void destroy() {
        WrapperPlayServerBoss packet = new WrapperPlayServerBoss();
        packet.setAction(WrapperPlayServerBoss.Action.REMOVE);
        packet.setUniqueId(uuid);
        packet.sendPacket(((BukkitPlayer) player).getPlayer());
    }

    void update() {
        Player p = ((BukkitPlayer) player).getPlayer();

        WrapperPlayServerBoss packet = new WrapperPlayServerBoss();
        packet.setAction(WrapperPlayServerBoss.Action.UPDATE_NAME);
        packet.setUniqueId(uuid);
        packet.setTitle(WrappedChatComponent.fromText(bossbar.getText()));
        packet.sendPacket(p);

        packet = new WrapperPlayServerBoss();
        packet.setAction(WrapperPlayServerBoss.Action.UPDATE_PCT);
        packet.setUniqueId(uuid);
        packet.setHealth(bossbar.getPercent());
        packet.sendPacket(p);

        packet = new WrapperPlayServerBoss();
        packet.setAction(WrapperPlayServerBoss.Action.UPDATE_STYLE);
        packet.setUniqueId(uuid);
        packet.setColor(fromColor(bossbar.getColor()));
        packet.sendPacket(p);
    }

    private BarColor fromColor(Color color) {
        try {
            return BarColor.valueOf(color.getName());
        } catch (Exception ex) {
            return BarColor.PINK;
        }
    }

}
