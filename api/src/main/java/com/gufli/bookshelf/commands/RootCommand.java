package com.gufli.bookshelf.commands;

import com.gufli.bookshelf.entity.PlatformPlayer;
import com.gufli.bookshelf.entity.PlatformSender;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RootCommand extends CommandExecutor {

    private final Set<Command<?>> commands = new HashSet<>();
    private final RootCommandMessages messages;

    public RootCommand(RootCommandMessages messages) {
        this.messages = messages;
    }

    public void register(Command<?> cmd) {
        commands.add(cmd);
    }

    public void unregister(Command<?> cmd) {
        commands.remove(cmd);
    }

    public void execute(PlatformSender sender, String[] args) {
        if ( args.length == 0 ) {
            return;
        }

        Command<?> invalidSubCommand = null;

        for ( Command<? extends PlatformSender> subCmd : commands ) {
            for ( String alias : subCmd.getInfo().commands() ) {
                if ( !(String.join(" ", args).toLowerCase() + " ").startsWith(alias.toLowerCase() + " ") ) {
                    continue;
                }

                int cmdLength = alias.split(Pattern.quote(" ")).length;
                String[] cmdArgs = convertArgs(Arrays.copyOfRange(args, cmdLength, args.length),
                        subCmd.getInfo().argumentsLength());

                if ( subCmd.getInfo().argumentsLength() != -1 && cmdArgs.length != subCmd.getInfo().argumentsLength() ) {
                    invalidSubCommand = subCmd;
                    continue;
                }

                if ( subCmd.getInfo().playerOnly() && !(sender instanceof PlatformPlayer) ) {
                    messages.sendPlayerOnly(sender);
                    return;
                }

                if ( !subCmd.hasAnyPermission(sender)) {
                    messages.sendNoPermission(sender);
                    return;
                }

                subCmd.executeInternal(sender, cmdArgs);
                return;
            }
        }

        if ( invalidSubCommand != null ) {
            messages.sendInvalidUsage(sender, invalidSubCommand.getInfo().commands()[0] + " "
                    + invalidSubCommand.getInfo().argumentsHint());
            return;
        }

        // suggest a command if the command doesn't exist
        LevenshteinDistance ld = new LevenshteinDistance(5);

        Map<Command<?>, String[]> candidates = new HashMap<>();
        for ( Command<?> cb : commands ) {
            candidates.put(cb, cb.getInfo().commands()[0].split(Pattern.quote(" ")));
        }

        for ( int i = 0; i < args.length; i++ ) {
            final int index = i;

            Map<Command<?>, String[]> filter = new HashMap<>();
            candidates.forEach((cb, cbArgs) -> {
                if ( cbArgs.length > index ) {
                    filter.put(cb, cbArgs);
                }
            });

            if ( filter.size() == 0 ) {
                break;
            }

            List<Command<?>> equal = filter.keySet().stream()
                    .filter(cb -> filter.get(cb)[index].equalsIgnoreCase(args[index]))
                    .collect(Collectors.toList());

            if ( !equal.isEmpty() ) {
                candidates.entrySet().removeIf(e -> !equal.contains(e.getKey()));
                continue;
            }

            Command<?> closest = filter.keySet().stream()
                    .min(Comparator.comparingInt(cb -> {
                        int res = ld.apply(filter.get(cb)[index], args[index]);
                        return res == -1 ? Integer.MAX_VALUE : res;
                    }))
                    .orElse(null);

            if ( closest == null ) {
                break;
            }

            String arg = filter.get(closest)[index];

            List<Command<?>> keep = filter.keySet().stream()
                    .filter(cb -> filter.get(cb)[index].equalsIgnoreCase(arg))
                    .collect(Collectors.toList());

            candidates.entrySet().removeIf(e -> !keep.contains(e.getKey()));
        }

        if ( candidates.isEmpty() ) {
            messages.sendSuggestion(sender, null);
            return;
        }

        Command<?> bestCommand = candidates.keySet().stream()
                .min(Comparator.comparingInt(cb -> {
                    int baseLength = candidates.get(cb).length;
                    int argsLength = cb.getInfo().argumentsHint().split(Pattern.quote(" ")).length;
                    return Math.abs((baseLength + argsLength) - args.length);
                })).orElse(null);

        messages.sendSuggestion(sender, bestCommand.getInfo().commands()[0] + ""
                + bestCommand.getInfo().argumentsHint());

    }

    public List<String> autocomplete(PlatformPlayer sender, String[] args) {

        // autocomplete command
        if ( args.length <= 1 ) {
            String arg0 = args[0].toLowerCase();
            List<String> result = new ArrayList<>();
            for ( Command<?> subCmd : commands ) {
                if ( !subCmd.hasAnyPermission(sender) ) {
                    continue;
                }

                result.addAll(Arrays.stream(subCmd.getInfo().commands())
                        .map(c -> c.split(Pattern.quote(" "))[0])
                        .filter(s -> s.toLowerCase().startsWith(arg0))
                        .collect(Collectors.toList()));
            }

            return result.stream().distinct().collect(Collectors.toList());
        }

        // autocomplete arguments
        String input = String.join(" ", args).toLowerCase();
        List<String> result = new ArrayList<>();
        outer: for ( Command<?> subCmd : commands ) {
            if ( !subCmd.hasAnyPermission(sender) ) {
                continue;
            }

            for ( String cmd : subCmd.getInfo().commands() ) {

                // check full command match
                if ( (input + " ").startsWith(cmd.toLowerCase() + " ") ) {
                    String[] cmdArgs = Arrays.copyOfRange(args, cmd.split(Pattern.quote(" ")).length, args.length);
                    List<String> options = subCmd.autocompleteInternal(sender, cmdArgs);
                    if ( options != null ) {
                        result.addAll(options);
                    }
                    continue outer;
                }

                // check partial command match
                if ( cmd.toLowerCase().startsWith(input) ) {
                    result.add(cmd.split(Pattern.quote(" "))[args.length - 1]);
                    continue outer;
                }
            }
        }

        return result.stream().distinct().filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase())).collect(Collectors.toList());
    }

    private String[] convertArgs(String[] args, int expectedArgs) {
        if ( expectedArgs <= 0 ) {
            return args;
        }
        if ( args.length < expectedArgs ) {
            return args;
        }

        List<String> currentArgs = Arrays.asList(args);

        String lastArgument = String.join(" ", currentArgs.subList(expectedArgs - 1, currentArgs.size()));
        if ( (lastArgument.startsWith("\"") && lastArgument.endsWith("\"")) || (lastArgument.startsWith("'") && lastArgument.endsWith("'")) ) {
            lastArgument = lastArgument.substring(1, lastArgument.length() - 1);

            List<String> newArgs = new ArrayList<>(currentArgs.subList(0, expectedArgs - 1));
            newArgs.add(lastArgument);
            return newArgs.toArray(new String[0]);
        }

        return args;
    }


}
