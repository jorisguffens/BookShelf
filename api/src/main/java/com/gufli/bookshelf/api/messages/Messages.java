package com.gufli.bookshelf.api.messages;

import com.gufli.bookshelf.api.entity.ShelfCommandSender;

public interface Messages {

    String getPrefix();

    void setPrefix(String prefix);

    String getMessage(String name);

    String getMessage(String name, String... placeholders);

    void send(ShelfCommandSender sender, String name, String... placeholders);

}
