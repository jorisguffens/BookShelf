package com.gufli.bookshelf.commands.debug;

import com.gufli.bookshelf.api.command.CommandInfo;
import com.gufli.bookshelf.command.DefaultRootCommandMessages;
import com.gufli.bookshelf.command.RootCommand;

@CommandInfo(commands = "debug")
public class DebugCommand extends RootCommand {

    public final static DebugCommand INSTANCE = new DebugCommand();

    private DebugCommand() {
        super(DefaultRootCommandMessages.INSTANCE);

        register(new DebugSidebarCommand());
        register(new DebugNametagCommand());
        register(new DebugTitleCommand());
    }
}
