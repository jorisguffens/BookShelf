package com.gufli.bookshelf.bukkit.item;

import com.gufli.bookshelf.api.item.Item;
import org.bukkit.inventory.ItemStack;

public class BukkitItem implements Item {

    private final ItemStack handle;

    public BukkitItem(ItemStack handle) {
        this.handle = handle;
    }

    @Override
    public Object handle() {
        if (handle == null) {
            return null;
        }
        return handle.clone();
    }
}