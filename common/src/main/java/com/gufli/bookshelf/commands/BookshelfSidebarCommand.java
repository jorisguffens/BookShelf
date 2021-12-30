package com.gufli.bookshelf.commands;


import com.gufli.bookshelf.api.command.Command;
import com.gufli.bookshelf.api.command.CommandInfo;
import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.sidebar.Sidebar;
import com.gufli.bookshelf.api.sidebar.Sidebars;
import com.gufli.bookshelf.messages.DefaultMessages;

import java.util.Arrays;

@CommandInfo(commands = "sidebar", argumentsHint = "<title> <line1> <line2> ...", minArguments = 2)
public class BookshelfSidebarCommand extends Command<ShelfPlayer> {

    @Override
    protected void onExecute(ShelfPlayer player, String[] args) {
        Sidebar template = new Sidebar(args[0], Arrays.asList(Arrays.copyOfRange(args, 1, args.length)));
        Sidebars.changeSidebar(player, template);

        DefaultMessages.send(player, "cmd.bookshelf.sidebar");
    }

}
