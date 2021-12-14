package com.gufli.bookshelf.commands.debug;


import com.gufli.bookshelf.api.command.Command;
import com.gufli.bookshelf.api.command.CommandInfo;
import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.sidebar.SidebarTemplate;
import com.gufli.bookshelf.api.sidebar.Sidebars;
import com.gufli.bookshelf.messages.DefaultMessages;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CommandInfo(commands = "sidebar", argumentsHint = "\"<title>\" \"<line1>\" \"<line2>\" ...", minArguments = 3)
public class DebugSidebarCommand extends Command<ShelfPlayer> {

    @Override
    protected void onExecute(ShelfPlayer player, String[] args) {
        List<String> contents = new ArrayList<>();

        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(String.join(" ", args));
        while (m.find()) {
            contents.add(m.group(1));
        }

        if ( contents.size() < 2 ) {
            DefaultMessages.send(player, "cmd.error.invald-usage", "/debug sidebar " + info().argumentsHint());
            return;
        }

        String title = contents.remove(0);

        /*
        String title = args[0];
        List<String> contents = Arrays.asList(Arrays.copyOfRange(args, 1, args.length));
        for ( int i = 0; i < contents.size(); i++ ) {
            contents.set(i, contents.get(i).replace("_", " "));
        }
         */

        SidebarTemplate template = new SidebarTemplate(title, contents);
        Sidebars.setSidebar(player, template);

        DefaultMessages.send(player, "cmd.debug.sidebar");
    }

}
