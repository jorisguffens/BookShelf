package com.gufli.bookshelf.commands;

import com.gufli.bookshelf.entity.PlatformSender;

public interface RootCommandMessages {

    void sendPlayerOnly(PlatformSender sender);

    void sendNoPermission(PlatformSender sender);

    void sendSuggestion(PlatformSender sender, String command);

    void sendInvalidUsage(PlatformSender sender, String command);

}
