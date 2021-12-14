package com.gufli.bookshelf.bukkit.api.entity;

import com.gufli.bookshelf.api.entity.ShelfCommandSender;
import org.bukkit.command.CommandSender;

public class BukkitCommandSender implements ShelfCommandSender {

    protected final CommandSender commandSender;

    public BukkitCommandSender(CommandSender commandSender) {
        this.commandSender = commandSender;
    }

    @Override
    public boolean hasPermission(String permission) {
        return commandSender.hasPermission(permission);
    }

    @Override
    public void sendMessage(String msg) {
        commandSender.sendMessage(msg);
    }

}
