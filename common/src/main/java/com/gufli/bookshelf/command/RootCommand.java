package com.gufli.bookshelf.command;

import com.gufli.bookshelf.api.command.Command;
import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.entity.ShelfCommandSender;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RootCommand extends Command<ShelfCommandSender> {

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

    @Override
    public void onExecute(ShelfCommandSender sender, String[] args) {
        if ( args.length == 0 ) {
            return;
        }

        String[] parsedArgs = parseArgs(args);
        Command<?> invalidSubCommand = null;

        for ( Command<? extends ShelfCommandSender> subCmd : commands ) {
            for ( String alias : subCmd.info().commands() ) {
                if ( !(String.join(" ", parsedArgs).toLowerCase() + " ").startsWith(alias.toLowerCase() + " ") ) {
                    continue;
                }

                int cmdLength = alias.split(Pattern.quote(" ")).length;
                String[] cmdArgs = Arrays.copyOfRange(parsedArgs, cmdLength, parsedArgs.length);

                if ( subCmd.info().minArguments() != -1 && cmdArgs.length != subCmd.info().minArguments() ) {
                    invalidSubCommand = subCmd;
                    continue;
                }

                if ( subCmd.info().playerOnly() && !(sender instanceof ShelfPlayer) ) {
                    messages.sendPlayerOnly(sender);
                    return;
                }

                if ( !subCmd.hasAnyPermission(sender)) {
                    messages.sendNoPermission(sender);
                    return;
                }

                subCmd.execute(sender, cmdArgs);
                return;
            }
        }

        if ( invalidSubCommand != null ) {
            messages.sendInvalidUsage(sender, invalidSubCommand.info().commands()[0] + " "
                    + invalidSubCommand.info().argumentsHint());
            System.out.println(getClass().getSimpleName());
            return;
        }

        // suggest a command if the command doesn't exist
        LevenshteinDistance ld = new LevenshteinDistance(5);

        Map<Command<?>, String[]> candidates = new HashMap<>();
        for ( Command<?> cb : commands ) {
            candidates.put(cb, cb.info().commands()[0].split(Pattern.quote(" ")));
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
                    .filter(cb -> filter.get(cb)[index].equalsIgnoreCase(parsedArgs[index]))
                    .collect(Collectors.toList());

            if ( !equal.isEmpty() ) {
                candidates.entrySet().removeIf(e -> !equal.contains(e.getKey()));
                continue;
            }

            Command<?> closest = filter.keySet().stream()
                    .min(Comparator.comparingInt(cb -> {
                        int res = ld.apply(filter.get(cb)[index], parsedArgs[index]);
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
                    int argsLength = cb.info().argumentsHint().split(Pattern.quote(" ")).length;
                    return Math.abs((baseLength + argsLength) - parsedArgs.length);
                })).orElse(null);

        messages.sendSuggestion(sender, bestCommand.info().commands()[0] + ""
                + bestCommand.info().argumentsHint());
    }

    @Override
    public List<String> onAutocomplete(ShelfCommandSender sender, String[] args) {

        // autocomplete command
        if ( args.length <= 1 ) {
            String arg0 = args[0].toLowerCase();
            List<String> result = new ArrayList<>();
            for ( Command<?> subCmd : commands ) {
                if ( !subCmd.hasAnyPermission(sender) ) {
                    continue;
                }

                result.addAll(Arrays.stream(subCmd.info().commands())
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

            for ( String cmd : subCmd.info().commands() ) {

                // check full command match
                if ( (input + " ").startsWith(cmd.toLowerCase() + " ") ) {
                    String[] cmdArgs = Arrays.copyOfRange(args, cmd.split(Pattern.quote(" ")).length, args.length);
                    List<String> options = subCmd.autocomplete(sender, cmdArgs);
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

//    private String[] convertArgs(String[] args, int expectedArgs) {
//        if ( expectedArgs <= 0 ) {
//            return args;
//        }
//        if ( args.length < expectedArgs ) {
//            return args;
//        }
//
//        List<String> currentArgs = Arrays.asList(args);
//
//        String lastArgument = String.join(" ", currentArgs.subList(expectedArgs - 1, currentArgs.size()));
//        if ( (lastArgument.startsWith("\"") && lastArgument.endsWith("\"")) || (lastArgument.startsWith("'") && lastArgument.endsWith("'")) ) {
//            lastArgument = lastArgument.substring(1, lastArgument.length() - 1);
//
//            List<String> newArgs = new ArrayList<>(currentArgs.subList(0, expectedArgs - 1));
//            newArgs.add(lastArgument);
//            return newArgs.toArray(new String[0]);
//        }
//
//        return args;
//    }

    private String[] parseArgs(String[] args) {
        boolean opened = false;

        StringBuilder sb = new StringBuilder();
        List<String> result = new ArrayList<>();
        for(int i = 0; i < args.length; i++) {
            char start = args[i].charAt(0);
            if ( start == '\'' || start == '\"' ) {
                opened = true;
                args[i] = args[i].substring(1);
            }
            if ( !opened ) {
                result.add(args[i]);
                continue;
            }

            sb.append(" ").append(args[i]);

            char end = args[i].charAt(args.length - 1);
            if ( end == '\'' || end == '\"' ) {
                opened = false;
                result.add(sb.substring(0, sb.length() - 1));
                sb = new StringBuilder();
            }
        }

        return result.toArray(new String[0]);
    }

}
