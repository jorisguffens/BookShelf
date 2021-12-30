package com.gufli.bookshelf.bukkit.api.menu;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.bukkit.api.entity.BukkitPlayer;
import org.bukkit.event.inventory.ClickType;

public interface InventoryMenuCallback {

    void onOpen(BukkitPlayer player);

    void onClose(BukkitPlayer player);

    void onClick(BukkitPlayer player, ClickType clickType, int slot, InventoryMenuItem item);

}
