package com.gufli.bookshelf.commands;

import com.gufli.bookshelf.entity.PlatformPlayer;
import com.gufli.bookshelf.entity.PlatformSender;

import java.util.List;

public abstract class CommandExecutor {

    public abstract void execute(PlatformSender sender, String[] args);

    public List<String> autocomplete(PlatformPlayer sender, String[] args) {
        return null;
    }

}
