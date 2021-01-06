package com.gufli.bookshelf.commands;

import com.gufli.bookshelf.entity.ShelfCommandSender;

public interface RootCommandMessages {

    void sendPlayerOnly(ShelfCommandSender sender);

    void sendNoPermission(ShelfCommandSender sender);

    void sendSuggestion(ShelfCommandSender sender, String command);

    void sendInvalidUsage(ShelfCommandSender sender, String command);

}
