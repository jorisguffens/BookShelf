package com.gufli.bookshelf.bukkit.gui;

import com.gufli.bookshelf.gui.InventoryItemCallback;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class InventoryBuilder {

    public static InventoryBuilder create() {
        return new InventoryBuilder();
    }

    private String title;

    private ItemStack previousItem;
    private ItemStack nextItem;

    private final List<BukkitInventoryItem> items = new ArrayList<>();

    private final Map<Integer, BukkitInventoryItem> hotbar = new HashMap<>();

    private InventoryBuilder() {
        previousItem = ItemStackBuilder.of(Material.ARROW)
                .withName(ChatColor.GREEN + "Previous page")
                .build();

        nextItem = ItemStackBuilder.of(Material.ARROW)
                .withName(ChatColor.GREEN + "Next page")
                .build();
    }

    public InventoryBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public InventoryBuilder withItem(ItemStack item) {
        items.add(new BukkitInventoryItem(item));
        return this;
    }

    public InventoryBuilder withItems(ItemStack... items) {
        return withItems(Arrays.asList(items));
    }

    public InventoryBuilder withItems(Iterable<ItemStack> items) {
        for ( ItemStack item : items ) {
            this.items.add(new BukkitInventoryItem(item));
        }
        return this;
    }

    public InventoryBuilder withItem(ItemStack item, InventoryItemCallback cb) {
        items.add(new BukkitInventoryItem(item, cb));
        return this;
    }

    public InventoryBuilder withNextItem(ItemStack item) {
        this.nextItem = item;
        return this;
    }

    public InventoryBuilder withPreviousItem(ItemStack item) {
        this.previousItem = item;
        return this;
    }

    public InventoryBuilder withHotbarItem(int slot, ItemStack item) {
        hotbar.put(slot, new BukkitInventoryItem(item));
        return this;
    }

    public InventoryBuilder withHotbarItem(int slot, ItemStack item, InventoryItemCallback cb) {
        hotbar.put(slot, new BukkitInventoryItem(item, cb));
        return this;
    }

    public BukkitInventory build() {
        int size = (items.size() / 9) + (items.size() % 9 > 0 ? 1 : 0);

        // no need for pagination
        if ( size <= 6 && hotbar.isEmpty() ) {
            BukkitInventory inv = new BukkitInventory(size, title);
            for ( int i = 0; i < items.size(); i++ ) {
                inv.setItem(i, items.get(i));
            }
            return inv;
        }

        return page(0);
    }

    private BukkitInventory page(int page) {
        int size = (items.size() / 9) + (items.size() % 9 > 0 ? 1 : 0);

        int pages = (size / 4) + (size % 4 > 0 ? 1 : 0);
        BukkitInventory inv = new BukkitInventory(54, title);

        // fill with items
        for ( int i = 0; i < Math.max(items.size(), 36); i++ ) {
            inv.setItem(i, items.get(i));
        }

        // fill hotbar
        for ( int i : hotbar.keySet() ) {
            if ( i >= 9 ) {
                continue;
            }

            inv.setItem(45 + i, hotbar.get(i));
        }

        if ( page > 0 ) {
            inv.setItem(47, new BukkitInventoryItem(previousItem, (player, clickType) -> {
                player.openInventory(page(page + 1));
                return true;
            }));
        }

        if ( page < pages - 1 ) {
            inv.setItem(51, new BukkitInventoryItem(nextItem, (player, clickType) -> {
                player.openInventory(page(page - 1));
                return true;
            }));
        }

        return inv;
    }

}
