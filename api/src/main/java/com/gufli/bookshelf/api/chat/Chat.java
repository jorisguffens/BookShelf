package com.gufli.bookshelf.api.chat;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

import java.util.Collection;
import java.util.Set;
import java.util.function.BiConsumer;

public class Chat {

    private Chat() {}

    private static ChatManager chatManager;

    public static void register(ChatManager manager) {
        if (chatManager != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton chatManager.");
        }

        chatManager = manager;
    }

    //

    public static void registerChatChannel(ChatChannel channel) {
        chatManager.registerChatChannel(channel);
    }

    public static void unregisterChatChannel(ChatChannel channel) {
        chatManager.unregisterChatChannel(channel);
    }

    public static ChatChannel channelById(String id) {
        return chatManager.channelById(id);
    }

    public static Collection<ChatChannel> channels() {
        return chatManager.channels();
    }

    public static void handle(ShelfPlayer player, String message) {
        chatManager.handle(player, message);
    }

    public static void handle(ShelfPlayer player, String message, ChatManager.Callback callback) {
        chatManager.handle(player, message, callback);
    }

    public static void send(ChatChannel channel, String message) {
        chatManager.send(channel, message);
    }

    public void send(ChatChannel channel, ShelfPlayer player, String message) {
        chatManager.send(channel, player, message);
    }

}
