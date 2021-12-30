package com.gufli.bookshelf.api.command;

import com.gufli.bookshelf.api.entity.ShelfCommandSender;
import com.gufli.bookshelf.api.entity.ShelfPlayer;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class CommandGroup extends Command<ShelfCommandSender> {

    private CommandGroup parent;

    private final Set<Command<?>> commands = new HashSet<>();
    private final CommandMessages messages;

    public CommandGroup(CommandMessages messages) {
        this.messages = messages;
    }

    public void add(Command<?> cmd) {
        commands.add(cmd);
        if (cmd instanceof CommandGroup) {
            ((CommandGroup) cmd).parent = this;
        }
    }

    public void remove(Command<?> cmd) {
        commands.remove(cmd);
        if (cmd instanceof CommandGroup) {
            ((CommandGroup) cmd).parent = null;
        }
    }

    //

    private String invalidUsageHint(Command<?> sub) {
        String hint = sub.info().commands()[0] + " " + sub.info().argumentsHint();
        CommandGroup parent = this.parent;
        while (parent != null) {
            hint = parent.info().commands()[0] + " " + hint;
            parent = parent.parent;
        }
        return "/" + hint;
    }

    //

    @Override
    public void onExecute(ShelfCommandSender sender, String[] args) {
        if (args.length == 0) {
            return;
        }

        String[] parsedArgs = parseArgs(args);
        String input = String.join(" ", parsedArgs).toLowerCase();

        int largestlength = 0;
        Command<?> bestcommand = null;
        for (Command<? extends ShelfCommandSender> subCmd : commands) {
            for (String alias : subCmd.info().commands()) {
                if (!input.startsWith(alias.toLowerCase())) {
                    continue;
                }

                int length = (int) alias.chars().filter(ch -> ch == ' ').count() + 1;
                if (length > largestlength) {
                    largestlength = length;
                    bestcommand = subCmd;
                }
            }
        }

        if (bestcommand == null) {
            suggestCommand(sender, parsedArgs);
            return;
        }

        String[] cmdArgs = Arrays.copyOfRange(parsedArgs, largestlength, parsedArgs.length);
        if (cmdArgs.length < bestcommand.info().minArguments()) {
            messages.sendInvalidUsage(sender, invalidUsageHint(bestcommand));
            return;
        }

        if (bestcommand.info().playerOnly() && !(sender instanceof ShelfPlayer)) {
            messages.sendPlayerOnly(sender);
            return;
        }

        if (!bestcommand.hasAnyPermission(sender)) {
            messages.sendNoPermission(sender);
            return;
        }

        bestcommand.execute(sender, cmdArgs);
    }

    @Override
    public List<String> onAutocomplete(ShelfCommandSender sender, String[] args) {

        Set<Command<?>> commands = this.commands.stream()
                .filter(c -> c.hasAnyPermission(sender))
                .collect(Collectors.toSet());

        // autocomplete command
        if (args.length <= 1) {
            String arg0 = args[0].toLowerCase();
            List<String> result = new ArrayList<>();
            for (Command<?> subCmd : commands) {
                result.addAll(Arrays.stream(subCmd.info().commands())
                        .map(c -> c.split(Pattern.quote(" "))[0])
                        .filter(s -> s.toLowerCase().startsWith(arg0))
                        .collect(Collectors.toList()));
            }

            return result.stream().distinct().collect(Collectors.toList());
        }


        String input = String.join(" ", args).toLowerCase();
        List<String> result = new ArrayList<>();

        // partial commands
        outer:
        for (Command<?> subCmd : commands) {
            for (String cmd : subCmd.info().commands()) {
                if (!cmd.toLowerCase().startsWith(input)) {
                    continue;
                }

                result.add(cmd.split(Pattern.quote(" "))[args.length - 1]);
                continue outer;
            }
        }

        // full commands
        int largestlength = 0;
        List<String> tempOptions = new ArrayList<>();
        outer:
        for (Command<?> subCmd : commands) {
            for (String cmd : subCmd.info().commands()) {
                if (!input.startsWith(cmd.toLowerCase())) {
                    continue;
                }

                int length = (int) cmd.chars().filter(ch -> ch == ' ').count() + 1;
                String[] cmdArgs = Arrays.copyOfRange(args, length, args.length);

                if (length > largestlength) {
                    largestlength = length;
                    tempOptions = subCmd.autocomplete(sender, cmdArgs);
                    continue outer;
                }
            }
        }

        result.addAll(tempOptions);
        return result.stream().distinct()
                .filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }

    private String[] parseArgs(String[] args) {
        char opened = 0;

        StringBuilder sb = new StringBuilder();
        List<String> result = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            char start = args[i].charAt(0);
            if (opened == 0 && (start == '\'' || start == '\"')) {
                opened = start;
                args[i] = args[i].substring(1);
            }

            if (opened == 0) {
                result.add(args[i]);
                continue;
            }

            if (sb.length() > 0) {
                sb.append(" ");
            }

            char end = args[i].charAt(args[i].length() - 1);
            if (end == opened) {
                opened = 0;
                sb.append(args[i], 0, args[i].length() - 1);
                result.add(sb.toString());
                sb = new StringBuilder();
            } else {
                sb.append(args[i]);
            }
        }

        // add remaining
        if (sb.length() > 0) {
            result.add(sb.toString());
        }

        return result.toArray(new String[0]);
    }

    private void suggestCommand(ShelfCommandSender sender, String[] args) {
        // suggest a command if the command doesn't exist
        LevenshteinDistance ld = new LevenshteinDistance(5);

        Map<Command<?>, String[]> candidates = new HashMap<>();
        for (Command<?> cb : commands) {
            candidates.put(cb, cb.info().commands()[0].split(Pattern.quote(" ")));
        }

        for (int i = 0; i < args.length; i++) {
            final int index = i;

            Map<Command<?>, String[]> filter = new HashMap<>();
            candidates.forEach((cb, cbArgs) -> {
                if (cbArgs.length > index) {
                    filter.put(cb, cbArgs);
                }
            });

            if (filter.size() == 0) {
                break;
            }

            List<Command<?>> equal = filter.keySet().stream()
                    .filter(cb -> filter.get(cb)[index].equalsIgnoreCase(args[index]))
                    .collect(Collectors.toList());

            if (!equal.isEmpty()) {
                candidates.entrySet().removeIf(e -> !equal.contains(e.getKey()));
                continue;
            }

            Command<?> closest = filter.keySet().stream()
                    .min(Comparator.comparingInt(cb -> {
                        int res = ld.apply(filter.get(cb)[index], args[index]);
                        return res == -1 ? Integer.MAX_VALUE : res;
                    }))
                    .orElse(null);

            if (closest == null) {
                break;
            }

            String arg = filter.get(closest)[index];

            List<Command<?>> keep = filter.keySet().stream()
                    .filter(cb -> filter.get(cb)[index].equalsIgnoreCase(arg))
                    .collect(Collectors.toList());

            candidates.entrySet().removeIf(e -> !keep.contains(e.getKey()));
        }

        if (candidates.isEmpty()) {
            messages.sendSuggestion(sender, null);
            return;
        }

        Command<?> bestCommand = candidates.keySet().stream()
                .min(Comparator.comparingInt(cb -> {
                    int baseLength = candidates.get(cb).length;
                    int argsLength = cb.info().argumentsHint().split(Pattern.quote(" ")).length;
                    return Math.abs((baseLength + argsLength) - args.length);
                })).orElse(null);

        messages.sendSuggestion(sender,
                "/" + info().commands()[0] + " " +
                        bestCommand.info().commands()[0] + " " +
                        bestCommand.info().argumentsHint());
    }

}
