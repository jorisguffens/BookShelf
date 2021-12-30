package com.gufli.bookshelf.bukkit.bossbar;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.placeholders.Placeholders;
import com.gufli.bookshelf.bukkit.api.bossbar.Bossbar;
import com.gufli.bookshelf.bukkit.api.entity.BukkitPlayer;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BossBar;

public class BukkitBossbar {

    private final Bossbar template;

    private final ShelfPlayer player;
    private final BossBar handle;

    public BukkitBossbar(ShelfPlayer player, Bossbar template) {
        this.player = player;
        this.template = template;

        this.handle = Bukkit.createBossBar(
                template.text(),
                template.color(),
                template.style()
        );
        update();
        this.handle.addPlayer(((BukkitPlayer) player).handle());
        this.handle.setVisible(true);
    }

    public void destroy() {
        handle.removeAll();
    }

    public void update() {
        String title = ChatColor.translateAlternateColorCodes('&', template.text());
        title = StringEscapeUtils.unescapeJava(title);
        title = Placeholders.replace(player, title);

        handle.setTitle(title);
        handle.setColor(template.color());
        handle.setStyle(template.style());
        handle.setProgress(template.progress());
    }

}
