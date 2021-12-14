package com.gufli.bookshelf.command;

import com.gufli.bookshelf.api.entity.ShelfCommandSender;
import com.gufli.bookshelf.messages.DefaultMessages;

public class DefaultRootCommandMessages implements RootCommandMessages {

    public final static DefaultRootCommandMessages INSTANCE = new DefaultRootCommandMessages();

    private DefaultRootCommandMessages() {}

    @Override
    public void sendPlayerOnly(ShelfCommandSender sender) {
        DefaultMessages.send(sender, "cmd.error.player-only");
    }

    @Override
    public void sendNoPermission(ShelfCommandSender sender) {
        DefaultMessages.send(sender, "cmd.error.no-permission");
    }

    @Override
    public void sendSuggestion(ShelfCommandSender sender, String command) {
        DefaultMessages.send(sender, "cmd.error.suggestion", command);
    }

    @Override
    public void sendInvalidUsage(ShelfCommandSender sender, String command) {
        DefaultMessages.send(sender, "cmd.error.invalid-usage", command);
    }
}
