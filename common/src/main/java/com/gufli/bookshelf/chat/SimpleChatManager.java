package com.gufli.bookshelf.chat;

import com.gufli.bookshelf.api.chat.Chat;
import com.gufli.bookshelf.api.chat.ChatChannel;
import com.gufli.bookshelf.api.chat.ChatManager;
import com.gufli.bookshelf.api.chat.SimpleChatChannel;
import com.gufli.bookshelf.api.color.TextColor;
import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.events.PlayerChatEvent;
import com.gufli.bookshelf.api.placeholders.Placeholders;
import com.gufli.bookshelf.api.server.Bookshelf;
import org.apache.commons.text.StringEscapeUtils;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SimpleChatManager implements ChatManager {

    private final static String DEFAULT_CHANNEL_KEY = "DEFAULT_CHAT_CHANNEL";

    private final Set<ChatChannel> chatChannels = new CopyOnWriteArraySet<>();

    public SimpleChatManager() {
        Chat.register(this);
        chatChannels.add(new SimpleChatChannel("default", "", "&f%s &7> &f%s"));
    }

    @Override
    public void registerChatChannel(ChatChannel channel) {
        if (channelById(channel.id()) != null) {
            throw new IllegalArgumentException("A channel with that id already exists.");
        }
        chatChannels.add(channel);
    }

    @Override
    public void unregisterChatChannel(ChatChannel channel) {
        chatChannels.remove(channel);
    }

    @Override
    public ChatChannel channelById(String id) {
        return chatChannels.stream().filter(c -> c.id().equals(id))
                .findFirst().orElse(null);
    }

    @Override
    public Collection<ChatChannel> channels() {
        return Collections.unmodifiableCollection(chatChannels);
    }

    @Override
    public void handle(ShelfPlayer player, String message, Callback callback) {
        message = message.trim();

        if (TextColor.strip(message).equals("")) {
            return;
        }

        List<ChatChannel> channels = chatChannels.stream()
                .filter(c -> c.canTalk(player))
                .sorted(Comparator.comparingInt(ch -> ch.talkPrefix() == null ? 0 : -ch.talkPrefix().length()))
                .toList();

        // initialize default channel
        ChatChannel channel = null;
        if (player.has(DEFAULT_CHANNEL_KEY)) {
            String id = player.get(DEFAULT_CHANNEL_KEY, String.class);
            channel = channelById(id);
        }

        // unset default chat channel by just typing the prefix
        if (channel != null && channel.talkPrefix() != null && channel.talkPrefix().equals(message)) {
            player.remove(DEFAULT_CHANNEL_KEY);
            // TODO msg
            return;
        }

        // set default chat channel by just typing the prefix
        for (ChatChannel ch : channels) {
            if (ch.talkPrefix() != null && !ch.talkPrefix().equals("") && ch.talkPrefix().equals(message)) {
                player.set(DEFAULT_CHANNEL_KEY, ch.id());
                // TODO msg
                return;
            }
        }

        // find channel for typed prefix
        for (ChatChannel ch : channels) {
            if (ch == channel) {
                continue;
            }

            if (ch.talkPrefix() != null && !message.startsWith(ch.talkPrefix())) {
                continue;
            }

            // If the player is using a default channel, use that prefix instead to talk in the channel without prefix
            if (channel != null && channel.talkPrefix() != null && ch.talkPrefix() != null && ch.talkPrefix().equals("")) {
                if (message.startsWith(channel.talkPrefix())) {
                    message = message.replaceFirst(Pattern.quote(channel.talkPrefix()), "");
                    channel = ch;
                    break;
                }
                continue;
            }

            if (ch.talkPrefix() != null) {
                message = message.replaceFirst(Pattern.quote(ch.talkPrefix()), "");
            }
            channel = ch;
            break;
        }

        if (channel == null) {
            // TODO msg
            return;
        }

        ChatChannel finalChannel = channel;
        dispatch(channel, player, message, (format, receivers) ->
                callback.accept(finalChannel, format, receivers));
    }

    @Override
    public void handle(ShelfPlayer player, String message) {
        handle(player, message, (channel, format, receivers) ->
                send(channel, player, format, message, receivers));
    }

    @Override
    public void send(ChatChannel channel, ShelfPlayer player, String message) {
        dispatch(channel, player, message, (format, receivers) ->
                send(channel, player, format, message, receivers));
    }

    @Override
    public void send(ChatChannel channel, String message) {
        Bookshelf.players().stream()
                .filter(channel::canRead)
                .forEach(p -> p.sendMessage(message));
    }

    private void send(ChatChannel channel, ShelfPlayer player, String format, String message, Set<ShelfPlayer> receivers) {
        if ( channel.talkPrefix() != null && message.startsWith(channel.talkPrefix()) ) {
            message = message.substring(channel.talkPrefix().length());
        }
        String result = String.format(format, player.displayName(), message);
        receivers.forEach(p -> p.sendMessage(result));
    }

    private void dispatch(ChatChannel channel, ShelfPlayer player, String message, BiConsumer<String, Set<ShelfPlayer>> callback) {
        Set<ShelfPlayer> receivers = Bookshelf.players().stream()
                .filter(channel::canRead).collect(Collectors.toSet());

        PlayerChatEvent event = new PlayerChatEvent(player, channel, message, receivers, channel.format());
        if (event.isCancelled() || event.format() == null || event.receivers().isEmpty()) {
            return;
        }

        String format = event.format();
        format = StringEscapeUtils.unescapeJava(format);
        format = Placeholders.replace(player, format);
        format = TextColor.translate(format);

        receivers.add(player);
        callback.accept(format, receivers);
    }
}
