package com.gufli.bookshelf.gui;

import com.gufli.bookshelf.entity.ShelfPlayer;

public interface Inventory<T, U> {

    boolean dispatchClick(ShelfPlayer player, InventoryClickType clickType, int slot);

    void dispatchClose(ShelfPlayer player);

    void dispatchOpen(ShelfPlayer player);

    T getHandle();

    InventoryItem<U> getItem(int slot);

    <V extends InventoryItem<U>> void setItem(int slot, V item);

    void removeItem(int slot);

}
