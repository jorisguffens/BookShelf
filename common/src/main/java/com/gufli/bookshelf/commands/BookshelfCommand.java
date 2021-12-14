package com.gufli.bookshelf.commands;

import com.gufli.bookshelf.api.command.CommandInfo;
import com.gufli.bookshelf.command.DefaultRootCommandMessages;
import com.gufli.bookshelf.command.RootCommand;
import com.gufli.bookshelf.commands.debug.DebugCommand;

@CommandInfo(commands = "bookshelf")
public class BookshelfCommand extends RootCommand {

    public final static BookshelfCommand INSTANCE = new BookshelfCommand();

    private BookshelfCommand() {
        super(DefaultRootCommandMessages.INSTANCE);
        register(DebugCommand.INSTANCE);
    }
}
