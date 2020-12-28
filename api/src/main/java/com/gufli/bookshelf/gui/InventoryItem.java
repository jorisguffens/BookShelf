package com.gufli.bookshelf.gui;

import com.gufli.bookshelf.entity.PlatformPlayer;

public interface InventoryItem<T> {

    boolean dispatchClick(PlatformPlayer player, InventoryClickType type);

    T getHandle();

}
