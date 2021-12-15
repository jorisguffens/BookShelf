package com.gufli.bookshelf.command;

import com.gufli.bookshelf.api.entity.ShelfCommandSender;

public interface CommandMessages {

    void sendPlayerOnly(ShelfCommandSender sender);

    void sendNoPermission(ShelfCommandSender sender);

    void sendSuggestion(ShelfCommandSender sender, String command);

    void sendInvalidUsage(ShelfCommandSender sender, String command);

}
