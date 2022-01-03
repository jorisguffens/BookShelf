package com.gufli.bookshelf.bukkit.commands;


import com.gufli.bookshelf.api.command.Command;
import com.gufli.bookshelf.api.command.CommandInfo;
import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.bukkit.api.bossbar.Bossbar;
import com.gufli.bookshelf.bukkit.api.bossbar.Bossbars;
import com.gufli.bookshelf.messages.DefaultMessages;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CommandInfo(
        commands = "bossbar",
        argumentsHint = "<color> <style> <progress> <text>",
        minArguments = 4,
        playerOnly = true,
        permissions = "bookshelf.bossbar"
)
public class BookshelfBossbarCommand extends Command<ShelfPlayer> {

    @Override
    protected List<String> onAutocomplete(ShelfPlayer sender, String[] args) {
        if (args.length == 1) {
            return Arrays.stream(BarColor.values()).map(Enum::name).collect(Collectors.toList());
        }
        if (args.length == 2) {
            return Arrays.stream(BarStyle.values()).map(Enum::name)
                    .collect(Collectors.toList());
        }
        if (args.length == 3) {
            return Arrays.asList("0", ".25", ".5", ".75", "1");
        }
        return null;
    }

    @Override
    protected void onExecute(ShelfPlayer player, String[] args) {

        BarColor color;
        try {
            color = BarColor.valueOf(args[0]);
        } catch (IllegalArgumentException ex) {
            DefaultMessages.send(player, "cmd.error.args.color", args[0]);
            return;
        }

        BarStyle style;
        try {
            style = BarStyle.valueOf(args[1]);
        } catch (IllegalArgumentException ex) {
            DefaultMessages.send(player, "cmd.error.args", args[1]);
            return;
        }

        if (!args[2].matches("[0-9]?[.]?[0-9]+")) {
            DefaultMessages.send(player, "cmd.error.args.number", args[2]);
            return;
        }

        float progress = Float.parseFloat(args[2]);
        if (progress < 0 || progress > 1) {
            DefaultMessages.send(player, "cmd.error.args.number", args[2]);
            return;
        }

        Bossbar bossbar = new Bossbar(args[3], color, style, progress);
        Bossbars.changeBossbar(player, bossbar);

        DefaultMessages.send(player, "cmd.bookshelf.bossbar");
    }

}
