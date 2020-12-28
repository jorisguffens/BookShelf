package com.gufli.bookshelf.commands;

import com.gufli.bookshelf.entity.PlatformPlayer;
import com.gufli.bookshelf.entity.PlatformSender;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandHandler {

    private static final Set<Command<?>> commands = new HashSet<>();

    public static void register(Command<?> cmd) {
        commands.add(cmd);
    }

    public static void unregister(Command<?> cmd) {
        commands.remove(cmd);
    }

    public static boolean execute(PlatformSender sender, String cmd, String[] args) {
        for ( Command<?> command : commands ) {
            if ( command.getInfo().commands()[0].equalsIgnoreCase(cmd) ) {
                command.executeInternal(sender, args);
                return true;
            }
        }
        return false;
    }

    public static List<String> autocomplete(PlatformPlayer sender, String cmd, String[] args) {
        for ( Command<?> command : commands ) {
            if ( command.getInfo().commands()[0].equalsIgnoreCase(cmd) ) {
                String lastArg = args[args.length - 1].toLowerCase();
                return command.autocompleteInternal(sender, args).stream()
                        .filter(s -> s.toLowerCase().startsWith(lastArg))
                        .collect(Collectors.toList());
            }
        }
        return null;
    }

}
