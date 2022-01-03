package com.gufli.bookshelf.bukkit.commands;


import com.gufli.bookshelf.api.animation.AnimationBuilder;
import com.gufli.bookshelf.api.command.Command;
import com.gufli.bookshelf.api.command.CommandInfo;
import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.bukkit.api.bossbar.Bossbar;
import com.gufli.bookshelf.bukkit.api.bossbar.Bossbars;
import com.gufli.bookshelf.messages.DefaultMessages;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CommandInfo(
        commands = "bossbar animated",
        argumentsHint = "<color> <style> <text>",
        minArguments = 3,
        playerOnly = true,
        permissions = "bookshelf.bossbar.animated"
)
public class BookshelfBossbarAnimatedCommand extends Command<ShelfPlayer> {

    @Override
    protected List<String> onAutocomplete(ShelfPlayer sender, String[] args) {
        if (args.length == 1) {
            return Arrays.stream(BarColor.values()).map(Enum::name).collect(Collectors.toList());
        }
        if (args.length == 2) {
            return Arrays.stream(BarStyle.values()).map(Enum::name)
                    .collect(Collectors.toList());
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

        Bossbar bossbar = new Bossbar(args[2], color, style, 0);
        Bossbars.changeBossbar(player, bossbar);

        AnimationBuilder.get()
                .repeatUntil(() -> {
                    bossbar.changeProgress(Math.min(1, bossbar.progress() + .05f));
                    Bossbars.updateBossbar(player);
                    return bossbar.progress() >= 1;
                }, 50, ChronoUnit.MILLIS)
                .repeatTimes((i) ->
                        bossbar.changeColor(BarColor.values()[i]),
                        BarColor.values().length, 250, ChronoUnit.MILLIS)
                .repeatUntil(() -> {
                    bossbar.changeProgress(Math.max(0, bossbar.progress() - .05f));
                    Bossbars.updateBossbar(player);
                    return bossbar.progress() <= 0;
                }, 50, ChronoUnit.MILLIS)
                .execute(() -> Bossbars.removeBossbar(player))
                .build().start();

        DefaultMessages.send(player, "cmd.bookshelf.bossbar");
    }

}
