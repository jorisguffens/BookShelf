package com.gufli.bookshelf.commands;


import com.gufli.bookshelf.api.command.Command;
import com.gufli.bookshelf.api.command.CommandInfo;
import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.titles.TitleType;
import com.gufli.bookshelf.api.titles.Titles;
import com.gufli.bookshelf.messages.DefaultMessages;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@CommandInfo(commands = "title", argumentsHint = "<type> <seconds> <title>", minArguments = 3)
public class BookshelfTitleCommand extends Command<ShelfPlayer> {

    @Override
    protected List<String> onAutocomplete(ShelfPlayer sender, String[] args) {
        if ( args.length == 1 ) {
            return Arrays.stream(TitleType.values()).map(Enum::name).collect(Collectors.toList());
        }
        if ( args.length == 2 ) {
            return Arrays.asList("1", "2", "3", "4", "5");
        }
        return null;
    }

    @Override
    protected void onExecute(ShelfPlayer player, String[] args) {

        TitleType type;
        try {
            type = TitleType.valueOf(args[0]);
        } catch (IllegalArgumentException ex) {
            DefaultMessages.send(player, "cmd.error.args", args[0]);
            return;
        }

        if ( !args[1].matches("[0-9]+") ) {
            DefaultMessages.send(player, "cmd.error.args.number", args[1]);
            return;
        }
        int seconds = Integer.parseInt(args[1]);

        Titles.sendTitle(player, args[2], type, seconds);
        DefaultMessages.send(player, "cmd.bookshelf.title");
    }

}
