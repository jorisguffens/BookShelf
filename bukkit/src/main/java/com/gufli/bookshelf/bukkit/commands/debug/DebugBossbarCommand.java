package com.gufli.bookshelf.bukkit.commands.debug;


import com.gufli.bookshelf.api.color.Color;
import com.gufli.bookshelf.api.command.Command;
import com.gufli.bookshelf.api.command.CommandInfo;
import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.bukkit.api.bossbar.Bossbar;
import com.gufli.bookshelf.bukkit.api.bossbar.Bossbars;
import com.gufli.bookshelf.messages.DefaultMessages;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CommandInfo(commands = "bossbar", argumentsHint = "<color> <percent> \"<text>\"")
public class DebugBossbarCommand extends Command<ShelfPlayer> {

    @Override
    protected void onExecute(ShelfPlayer player, String[] args) {

        Color color = Color.findColor(args[0]);
        if ( color == null ) {
            DefaultMessages.send(player, "cmd.error.args.color", args[0]);
            return;
        }

        if ( !args[1].matches("[0-9]") ) {
            DefaultMessages.send(player, "cmd.error.args.number", args[1]);
            return;
        }
        float percent = Integer.parseInt(args[1]) / 100.f;
        if ( percent < 0 || percent > 1 ) {
            DefaultMessages.send(player, "cmd.error.args.number", args[1]);
            return;
        }

        String text = "";
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(String.join(" ", Arrays.copyOfRange(args, 2, args.length)));
        while (m.find()) {
            text = m.group(1);
        }

        Bossbar bossbar = new Bossbar(text, color, percent);
        Bossbars.setBossbar(player, bossbar);

        DefaultMessages.send(player, "cmd.debug.bossbar");
    }

}
