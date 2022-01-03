package com.gufli.bookshelf.bukkit.commands;


import com.gufli.bookshelf.api.command.Command;
import com.gufli.bookshelf.api.command.CommandInfo;
import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.server.Bookshelf;
import com.gufli.bookshelf.bukkit.api.bossbar.Bossbar;
import com.gufli.bookshelf.bukkit.api.bossbar.Bossbars;
import com.gufli.bookshelf.bukkit.api.hologram.Hologram;
import com.gufli.bookshelf.bukkit.api.hologram.Holograms;
import com.gufli.bookshelf.messages.DefaultMessages;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@CommandInfo(
        commands = "hologram",
        argumentsHint = "<line1> <line2> ...",
        minArguments = 1,
        playerOnly = true,
        permissions = "bookshelf.hologram"
)
public class BookshelfHologramCommand extends Command<ShelfPlayer> {

    @Override
    protected void onExecute(ShelfPlayer player, String[] args) {
        List<String> lines = Arrays.asList(args);
        Hologram hologram = new Hologram(player.location(), lines);
        Holograms.show(hologram, player);

        Bookshelf.scheduler().syncLater(() -> Holograms.hide(hologram, player), 15, TimeUnit.SECONDS);
        DefaultMessages.send(player, "cmd.bookshelf.hologram");
    }

}
