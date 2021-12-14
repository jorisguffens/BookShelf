package com.gufli.bookshelf.api.menu;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

public interface MenuCallback<T> {

    void onOpen(ShelfPlayer player);

    void onClose(ShelfPlayer player);

    void onClick(ShelfPlayer player, MenuClickType clickType, int slot, MenuItem<T> item);

}
