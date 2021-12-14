package com.gufli.bookshelf.bukkit.titles;

import com.comphenix.packetwrapper.WrapperPlayServerTitle;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.titles.TitleManager;
import com.gufli.bookshelf.api.titles.TitleType;
import com.gufli.bookshelf.api.titles.Titles;
import com.gufli.bookshelf.bukkit.entity.BukkitPlayer;
import org.bukkit.entity.Player;

public class BukkitTitleManager implements TitleManager {

    public BukkitTitleManager() {
        Titles.register(this);
    }

    @Override
    public void showTitle(ShelfPlayer player, String text, TitleType type, float seconds) {
        Player p = ((BukkitPlayer) player).getPlayer();

        if (type == TitleType.BIG) {
            p.sendTitle(text, null, 10, (int) (seconds * 20), 10);
            return;
        }

        if (type == TitleType.MEDIUM) {
            p.sendTitle(null, text, 10, (int) (seconds * 20), 10);
            return;
        }

        if (type == TitleType.SMALL) {
            showActionbar(p, text, (int) (seconds * 20));
        }
    }

    private void showActionbar(Player player, String text, int ticks) {
        WrapperPlayServerTitle packet = new WrapperPlayServerTitle();
        packet.setFadeIn(10);
        packet.setFadeOut(10);
        packet.setStay(ticks);
        packet.sendPacket(player);

        packet = new WrapperPlayServerTitle();
        packet.setAction(EnumWrappers.TitleAction.ACTIONBAR);
        packet.setTitle(WrappedChatComponent.fromText(text));
        packet.sendPacket(player);
    }

}
