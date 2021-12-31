package com.gufli.bookshelf.api.events;

import com.gufli.bookshelf.api.chat.ChatChannel;
import com.gufli.bookshelf.api.entity.ShelfPlayer;

import java.util.Set;

public class PlayerChatEvent extends AbstractPlayerEvent implements Cancellable {

    private final ChatChannel chatChannel;
    private final String message;
    private final Set<ShelfPlayer> receivers;

    private String format;

    private boolean cancelled = false;

    public PlayerChatEvent(ShelfPlayer player, ChatChannel chatChannel, String message, Set<ShelfPlayer> receivers, String format) {
        super(player);
        this.chatChannel = chatChannel;
        this.message = message;
        this.receivers = receivers;
        this.format = format;
    }

    public ChatChannel chatChannel() {
        return chatChannel;
    }

    public String message() {
        return message;
    }

    public String format() {
        return format;
    }

    public Set<ShelfPlayer> receivers() {
        return receivers;
    }

    public void changeFormat(String format) {
        this.format = format;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
