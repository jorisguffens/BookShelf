package com.gufli.bookshelf.commands.debug;


import com.gufli.bookshelf.api.command.Command;
import com.gufli.bookshelf.api.command.CommandInfo;
import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.nametags.Nametags;
import com.gufli.bookshelf.messages.DefaultMessages;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CommandInfo(commands = "nametag", argumentsHint = "\"<prefix>\" \"<suffix>\"", minArguments = 2)
public class DebugNametagCommand extends Command<ShelfPlayer> {

    @Override
    protected void onExecute(ShelfPlayer player, String[] args) {

        List<String> result = new ArrayList<>();

        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(String.join(" ", args));
        while (m.find()) {
            result.add(m.group(1));
        }

        if (result.size() < 2) {
            DefaultMessages.send(player, "cmd.error.invald-usage", "/debug sidebar " + info().argumentsHint());
            return;
        }

        Nametags.setNametag(player, result.get(0), result.get(1));
        DefaultMessages.send(player, "cmd.debug.nametag");
    }

}
