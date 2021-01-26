package com.gufli.bookshelf.bukkit.titles;

import com.comphenix.packetwrapper.WrapperPlayServerTitle;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.gufli.bookshelf.bukkit.entity.BukkitPlayer;
import com.gufli.bookshelf.entity.ShelfPlayer;
import com.gufli.bookshelf.event.Events;
import com.gufli.bookshelf.events.PlayerQuitEvent;
import com.gufli.bookshelf.titles.Bossbar;
import com.gufli.bookshelf.titles.TitleManager;
import com.gufli.bookshelf.titles.Titles;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BukkitTitleManager implements TitleManager {

    private final Map<ShelfPlayer, BukkitBossbar> players = new HashMap<>();

    public BukkitTitleManager() {
        Titles.register(this);

        Events.subscribe(PlayerQuitEvent.class)
                .handler(e -> removeBossbar(e.getPlayer()));
    }

    @Override
    public void showTitle(ShelfPlayer player, String title, int seconds) {
        Player p = ((BukkitPlayer) player).getPlayer();
        p.sendTitle(title, null, 10, seconds * 20, 10);
    }

    @Override
    public void showSubtitle(ShelfPlayer player, String subtitle, int seconds) {
        Player p = ((BukkitPlayer) player).getPlayer();
        p.sendTitle(null, subtitle, 10, seconds * 20, 10);
    }

    @Override
    public void showActionbar(ShelfPlayer player, String text, int seconds) {
        Player p = ((BukkitPlayer) player).getPlayer();

        WrapperPlayServerTitle packet = new WrapperPlayServerTitle();
        packet.setFadeIn(10);
        packet.setFadeOut(10);
        packet.setStay(seconds * 20);
        packet.sendPacket(p);

        packet = new WrapperPlayServerTitle();
        packet.setAction(EnumWrappers.TitleAction.ACTIONBAR);
        packet.setTitle(WrappedChatComponent.fromText(text));
        packet.sendPacket(p);
    }

    @Override
    public void setBossbar(ShelfPlayer player, Bossbar bossbar) {
        removeBossbar(player);
        BukkitBossbar bb = new BukkitBossbar(player, bossbar);
        players.put(player, bb);
        bb.show();
    }

    @Override
    public Bossbar getBossbar(ShelfPlayer player) {
        return players.containsKey(player) ? players.get(player).getBossbar() : null;
    }

    @Override
    public void removeBossbar(ShelfPlayer player) {
        if ( !players.containsKey(player) ) {
            return;
        }

        BukkitBossbar bb = players.get(player);
        bb.destroy();
        players.remove(player);
    }

    @Override
    public void updateBossbar(ShelfPlayer player) {
        if ( !players.containsKey(player) ) {
            return;
        }

        players.get(player).update();
    }

}
