package com.gufli.bookshelf.api.menu;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

import java.util.HashMap;
import java.util.Map;

public abstract class Menu<T, U> {

    protected MenuCallback<U> callback;
    protected Map<Integer, MenuItem<U>> items = new HashMap<>();

    protected T handle;

    public Menu(T handle, MenuCallback<U> callback) {
        this.handle = handle;
        this.callback = callback;
    }

    public Menu(T handle) {
        this(handle, null);
    }

    public boolean dispatchClick(ShelfPlayer player, MenuClickType clickType, int slot) {
        MenuItem<U> item = items.get(slot);

        if ( callback != null ) {
            callback.onClick(player, clickType, slot, item);
        }

        return item.dispatchClick(player, clickType);
    }

    public void dispatchClose(ShelfPlayer player) {
        if ( callback != null ) {
            callback.onClose(player);
        }
    }

    public void dispatchOpen(ShelfPlayer player) {
        if ( callback != null ) {
            callback.onOpen(player);
        }
    }

    public T getHandle() {
        return handle;
    }

    public MenuItem<U> getItem(int slot) {
        return items.get(slot);
    }

    public <V extends MenuItem<U>> void setItem(int slot, V item) {
        items.put(slot, item);
    }

    public void removeItem(int slot) {
        items.remove(slot);
    }

    public Map<Integer, MenuItem<U>> getItems() {
        return items;
    }

}
