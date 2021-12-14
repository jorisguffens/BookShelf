package com.gufli.bookshelf.api.menu;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

import java.util.function.BiFunction;

public abstract class MenuItem<T> {

    protected T handle;
    protected BiFunction<ShelfPlayer, MenuClickType, Boolean> callback;

    public MenuItem(T handle, BiFunction<ShelfPlayer, MenuClickType, Boolean> callback) {
        this.handle = handle;
        this.callback = callback;
    }

    public MenuItem(T handle) {
        this(handle, null);
    }

    public boolean dispatchClick(ShelfPlayer player, MenuClickType type) {
        if ( callback == null ) {
            return false;
        }
        return callback.apply(player, type);
    }

    public T getHandle() {
        return handle;
    }

}
