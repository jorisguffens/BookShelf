package com.gufli.bookshelf.commands.debug;


import com.gufli.bookshelf.api.command.Command;
import com.gufli.bookshelf.api.command.CommandInfo;
import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.titles.TitleType;
import com.gufli.bookshelf.api.titles.Titles;
import com.gufli.bookshelf.messages.DefaultMessages;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CommandInfo(commands = "title", argumentsHint = "<type> <seconds> \"<title>\"", minArguments = 3)
public class DebugTitleCommand extends Command<ShelfPlayer> {

    @Override
    protected void onExecute(ShelfPlayer player, String[] args) {

        TitleType type;
        try {
            type = TitleType.valueOf(args[0]);
        } catch (IllegalArgumentException ex) {
            DefaultMessages.send(player, "cmd.error.args", args[0]);
            return;
        }

        if ( !args[1].matches("[0-9]") ) {
            DefaultMessages.send(player, "cmd.error.args.number", args[1]);
            return;
        }
        int seconds = Integer.parseInt(args[1]);

        String text = "";
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(String.join(" ", Arrays.copyOfRange(args, 2, args.length)));
        while (m.find()) {
            text = m.group(1);
        }

        Titles.sendTitle(player, text, type, seconds);

        DefaultMessages.send(player, "cmd.debug.title");
    }

}
