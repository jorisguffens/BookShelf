package com.gufli.bookshelf.commands;

import com.gufli.bookshelf.api.command.CommandInfo;
import com.gufli.bookshelf.command.DefaultCommandMessages;
import com.gufli.bookshelf.command.CommandGroup;

@CommandInfo(commands = "bookshelf")
public class BookshelfCommandGroup extends CommandGroup {

    public BookshelfCommandGroup() {
        super(DefaultCommandMessages.INSTANCE);

        add(new BookshelfNametagCommand());
        add(new BookshelfSidebarCommand());
        add(new BookshelfTitleCommand());
    }
}
