package com.gufli.bookshelf.api.chat;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

public interface ChatChannel {

    String id();

    String format();

    String talkPrefix();

    boolean canRead(ShelfPlayer player);

    boolean canTalk(ShelfPlayer player);

}
