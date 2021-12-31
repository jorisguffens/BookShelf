package com.gufli.bookshelf.api.chat;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

public class SimpleChatChannel implements ChatChannel {

    private final String id;
    private final String talkPrefix;
    private final String format;

    private RestrictedAction action;
    private String permission;

    public SimpleChatChannel(String id, String talkPrefix, String format) {
        this.id = id;
        this.talkPrefix = talkPrefix;
        this.format = format;
    }

    @Override
    public final String id() {
        return id;
    }

    @Override
    public final String format() {
        return format;
    }

    @Override
    public final String talkPrefix() {
        return talkPrefix;
    }

    public final void protect(RestrictedAction action, String permission) {
        this.action = action;
        this.permission = permission;
    }

    @Override
    public boolean canRead(ShelfPlayer player) {
        if ( action != null ) {
            return player.hasPermission(permission);
        }
        return true;
    }

    @Override
    public boolean canTalk(ShelfPlayer player) {
        if ( action == RestrictedAction.READ_AND_TALK ) {
            return player.hasPermission(permission);
        }
        return canRead(player);
    }

    enum RestrictedAction {
        READ, READ_AND_TALK;
    }
}
