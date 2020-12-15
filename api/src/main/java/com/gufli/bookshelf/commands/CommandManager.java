package com.gufli.bookshelf.commands;

import com.gufli.bookshelf.entity.PlatformPlayer;
import com.gufli.bookshelf.entity.PlatformSender;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommandManager {

    private final static CommandManager manager = new CommandManager();

    public static CommandManager get() {
        return manager;
    }

    private CommandManager() {}

    //

    private final Set<Command<?>> commands = new HashSet<>();

    public void register(Command<?> cmd) {
        commands.add(cmd);
    }

    public void unregister(Command<?> cmd) {
        commands.remove(cmd);
    }

    public boolean execute(PlatformSender sender, String cmd, String[] args) {
        for ( Command<?> command : commands ) {
            if ( command.getInfo().commands()[0].equalsIgnoreCase(cmd) ) {
                command.executeInternal(sender, args);
                return true;
            }
        }
        return false;
    }

    public List<String> autocomplete(PlatformPlayer sender, String cmd, String[] args) {
        for ( Command<?> command : commands ) {
            if ( command.getInfo().commands()[0].equalsIgnoreCase(cmd) ) {
                return command.autocompleteInternal(sender, args);
            }
        }
        return null;
    }

}
