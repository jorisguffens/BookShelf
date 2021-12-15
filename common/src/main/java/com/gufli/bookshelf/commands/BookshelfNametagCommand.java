package com.gufli.bookshelf.commands;


import com.gufli.bookshelf.api.command.Command;
import com.gufli.bookshelf.api.command.CommandInfo;
import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.nametags.Nametags;
import com.gufli.bookshelf.messages.DefaultMessages;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CommandInfo(commands = "nametag", argumentsHint = "<prefix> <suffix>", minArguments = 2)
public class BookshelfNametagCommand extends Command<ShelfPlayer> {

    @Override
    protected void onExecute(ShelfPlayer player, String[] args) {
        Nametags.setNametag(player, args[0], args[1]);
        DefaultMessages.send(player, "cmd.bookshelf.nametag");
    }

}
