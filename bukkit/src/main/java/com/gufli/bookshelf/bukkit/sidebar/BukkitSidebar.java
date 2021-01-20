package com.gufli.bookshelf.bukkit.sidebar;

import com.gufli.bookshelf.bukkit.entity.BukkitPlayer;
import com.gufli.bookshelf.bukkit.sidebar.packets.PacketObjective;
import com.gufli.bookshelf.entity.ShelfPlayer;
import com.gufli.bookshelf.sidebar.Sidebar;
import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BukkitSidebar {

    private final Sidebar sidebar;
    private final ShelfPlayer player;
    private final List<String> lines = new CopyOnWriteArrayList<>();

    private final PacketObjective objective;

    public BukkitSidebar(ShelfPlayer player, Sidebar sidebar) {
        this.sidebar = sidebar;
        this.player = player;

        objective = new PacketObjective(
                ((BukkitPlayer) player).getPlayer(),
                RandomStringUtils.randomAlphabetic(15),
                sidebar.getTitle() != null ? sidebar.getTitle() : ""
        );
    }

    Sidebar getSidebar() {
        return sidebar;
    }

    void destroy() {
        objective.destroy();
    }

    void show() {
        objective.show();
    }

    void update() {
        if ( sidebar.getTitle() != null ) {
            objective.setDisplayName(sidebar.getTitle());
        }

        List<String> contents = sidebar.getLines(player);

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
        for ( String line : this.lines ) {
            if ( !contents.contains(line) ) {
                objective.removeScore(line);
            }
        }

        // set & update lines with the new score order
        this.lines.clear();
        int i = contents.size() - 1;
        for ( String s : contents) {
            this.lines.add(s);
            objective.setScore(s, i);
            i--;
        }
    }

}
