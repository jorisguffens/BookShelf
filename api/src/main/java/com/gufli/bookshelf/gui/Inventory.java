package com.gufli.bookshelf.gui;

import com.gufli.bookshelf.entity.PlatformPlayer;

public interface Inventory<T, U> {

    boolean dispatchClick(PlatformPlayer player, InventoryClickType clickType, int slot);

    void dispatchClose(PlatformPlayer player);

    void dispatchOpen(PlatformPlayer player);

    T getHandle();

    InventoryItem<U> getItem(int slot);

    <V extends InventoryItem<U>> void setItem(int slot, V item);

    void removeItem(int slot);

}
