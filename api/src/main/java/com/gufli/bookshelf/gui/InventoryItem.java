package com.gufli.bookshelf.gui;

import com.gufli.bookshelf.entity.ShelfPlayer;

public interface InventoryItem<T> {

    boolean dispatchClick(ShelfPlayer player, InventoryClickType type);

    T getHandle();

}
