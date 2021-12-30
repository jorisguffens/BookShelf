package com.gufli.bookshelf.api.messages;

import com.gufli.bookshelf.api.entity.ShelfCommandSender;

public interface Messages {

    String prefix();

    void changePrefix(String prefix);

    String get(String key);

    String get(String key, String... placeholders);

    void send(ShelfCommandSender sender, String key, String... placeholders);

}
