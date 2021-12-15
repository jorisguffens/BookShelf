package com.gufli.bookshelf.bukkit.bossbar;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.bukkit.api.bossbar.Bossbar;
import com.gufli.bookshelf.bukkit.api.entity.BukkitPlayer;
import com.gufli.bookshelf.bukkit.packets.BossbarPacketBuilder;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitBossbar {

    private final UUID id = UUID.randomUUID();
    private final Bossbar bossbar;
    private final ShelfPlayer player;

    public BukkitBossbar(ShelfPlayer player, Bossbar bossbar) {
        this.player = player;
        this.bossbar = bossbar;
    }

    public UUID id() {
        return id;
    }

    public Bossbar bossbar() {
        return bossbar;
    }

    public ShelfPlayer player() {
        return player;
    }

    void show() {
        BossbarPacketBuilder.create(id, bossbar).sendPacket(((BukkitPlayer) player).getHandle());
    }

    void destroy() {
        BossbarPacketBuilder.remove(id).sendPacket(((BukkitPlayer) player).getHandle());
    }

    void update() {
        Player p = ((BukkitPlayer) player).getHandle();

        // TODO
//        BossbarPacketBuilder packet = new BossbarPacketBuilder();
//        packet.setAction(BossbarPacketBuilder.Action.UPDATE_NAME);
//        packet.setUniqueId(id);
//        packet.setTitle(WrappedChatComponent.fromText(bossbar.getText()));
//        packet.sendPacket(p);
//
//        packet = new BossbarPacketBuilder();
//        packet.setAction(BossbarPacketBuilder.Action.UPDATE_PCT);
//        packet.setUniqueId(id);
//        packet.setHealth(bossbar.getPercent());
//        packet.sendPacket(p);
//
//        packet = new BossbarPacketBuilder();
//        packet.setAction(BossbarPacketBuilder.Action.UPDATE_STYLE);
//        packet.setUniqueId(id);
//        packet.setColor(fromColor(bossbar.getColor()));
//        packet.sendPacket(p);
    }

}
