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

@CommandInfo(commands = "bossbar", argumentsHint = "<color> <progress> <text>", minArguments = 3)
public class BookshelfBossbarCommand extends Command<ShelfPlayer> {

    @Override
    protected List<String> onAutocomplete(ShelfPlayer sender, String[] args) {
        if (args.length == 1) {
            return Arrays.stream(BarColor.values()).map(Enum::name).collect(Collectors.toList());
        }
        if (args.length == 2) {
            return Arrays.asList("0", "25", "50", "75", "100");
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

        if (!args[1].matches("[0-9]?[\\.][0-9]+")) {
            DefaultMessages.send(player, "cmd.error.args.number", args[1]);
            return;
        }

        float percent = Float.parseFloat(args[1]);
        if (percent < 0 || percent > 1) {
            DefaultMessages.send(player, "cmd.error.args.number", args[1]);
            return;
        }

        Bossbar bossbar = new Bossbar(args[2], color, percent);
        Bossbars.setBossbar(player, bossbar);

        DefaultMessages.send(player, "cmd.bookshelf.bossbar");
    }

}
