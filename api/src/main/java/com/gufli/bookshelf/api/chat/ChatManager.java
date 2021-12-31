package com.gufli.bookshelf.api.chat;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

import java.util.Collection;
import java.util.Set;
import java.util.function.BiConsumer;

public interface ChatManager {

    void registerChatChannel(ChatChannel channel);

    void unregisterChatChannel(ChatChannel channel);

    ChatChannel channelById(String id);

    Collection<ChatChannel> channels();

    void handle(ShelfPlayer player, String message);

    void handle(ShelfPlayer player, String message, Callback callback);

    void send(ChatChannel channel, String message);

    void send(ChatChannel channel, ShelfPlayer player, String message);

    @FunctionalInterface
    interface Callback {
        void accept(ChatChannel channel, String format, Set<ShelfPlayer> receivers);
    }
}
