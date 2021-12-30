package com.gufli.bookshelf.bukkit.api.menu;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.bukkit.api.entity.BukkitPlayer;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class InventoryMenuBuilder {

    private final static InventoryMenuScheme[] SCHEMES = new InventoryMenuScheme[] {
            InventoryMenuScheme.of("000000000", "000010000"), // 1 item
            InventoryMenuScheme.of("000000000", "000101000"), // 2 items
            InventoryMenuScheme.of("000000000", "001010100"), // 3 items
            InventoryMenuScheme.of("000000000", "010101010"), // 4 items
            InventoryMenuScheme.of("000000000", "001000100", "000010000", "001000100"), // 5 items
            InventoryMenuScheme.of("000000000", "001010100", "001010100"), // 6 items
            InventoryMenuScheme.of("000000000", "010101010", "001010100"), // 7 items
            InventoryMenuScheme.of("000000000", "010101010", "010101010"), // 8 items
            InventoryMenuScheme.of("000000000", "010101010", "001010100", "010101010"), // 9 items
            InventoryMenuScheme.of("000000000", "010101010", "000101000", "010101010") // 10 items
    };

    //

    protected InventoryMenuBuilder() {}

    public static InventoryMenuBuilder create() {
        return new InventoryMenuBuilder();
    }

    public static InventoryMenuBuilder create(String title) {
        return new InventoryMenuBuilder().withTitle(title);
    }

    //

    protected String title;

    protected final List<InventoryMenuItem> items = new ArrayList<>();
    protected final Map<Integer, InventoryMenuItem> hotbar = new HashMap<>();

    //

    public final InventoryMenuBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public final InventoryMenuBuilder withItem(ItemStack item) {
        items.add(new InventoryMenuItem(item));
        return this;
    }

    public final InventoryMenuBuilder withItems(ItemStack... items) {
        return withItems(Arrays.asList(items));
    }

    public final InventoryMenuBuilder withItems(Iterable<ItemStack> items) {
        for ( ItemStack item : items ) {
            this.items.add(new InventoryMenuItem(item));
        }
        return this;
    }

    public final InventoryMenuBuilder withItem(ItemStack item, BiFunction<BukkitPlayer, ClickType, Boolean> cb) {
        items.add(new InventoryMenuItem(item, cb));
        return this;
    }

    public final InventoryMenuBuilder withItem(ItemStack item, BiConsumer<BukkitPlayer, ClickType> cb) {
        items.add(new InventoryMenuItem(item, (player, clickType) -> {
            cb.accept(player, clickType);
            return true;
        }));
        return this;
    }

    public final InventoryMenuBuilder withHotbarItem(int slot, ItemStack item) {
        hotbar.put(slot, new InventoryMenuItem(item));
        return this;
    }

    public final InventoryMenuBuilder withHotbarItem(int slot, ItemStack item, BiFunction<BukkitPlayer, ClickType, Boolean> cb) {
        hotbar.put(slot, new InventoryMenuItem(item, cb));
        return this;
    }

    public InventoryMenu build() {
        if ( items.size() > 10 ) {
            throw new UnsupportedOperationException("More than 10 items is not supported.");
        }

        InventoryMenuScheme scheme = SCHEMES[items.size() - 1];
        int size = scheme.getRows() + 1;

        if ( !hotbar.isEmpty() ) {
            size += 1;
        }

        InventoryMenu menu = new InventoryMenu(size, title);

        // fill items
        int index = 0;
        for ( int slot : scheme.getSlots() ) {
            menu.setItem(slot, items.get(index));
            index++;
        }

        // fill hotbar
        for ( int slot : hotbar.keySet() ) {
            if ( slot < 0 || slot >= 9 ) {
                continue;
            }

            menu.setItem(((size-1) * 9) + slot, hotbar.get(slot));
        }

        return menu;
    }
    
}
