package com.gufli.bookshelf.bukkit.menu;

import com.gufli.bookshelf.entity.ShelfPlayer;
import com.gufli.bookshelf.menu.MenuClickType;
import com.gufli.bookshelf.menu.MenuItemCallback;
import com.gufli.bookshelf.menu.MenuScheme;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.BiConsumer;

public class MenuBuilder {

    private final static MenuScheme[] SCHEMES = new MenuScheme[] {
            MenuScheme.of("000000000", "000010000"), // 1 item
            MenuScheme.of("000000000", "000101000"), // 2 items
            MenuScheme.of("000000000", "001010100"), // 3 items
            MenuScheme.of("000000000", "010101010"), // 4 items
            MenuScheme.of("000000000", "001000100", "000010000", "001000100"), // 5 items
            MenuScheme.of("000000000", "001010100", "001010100"), // 6 items
            MenuScheme.of("000000000", "010101010", "001010100"), // 7 items
            MenuScheme.of("000000000", "010101010", "010101010"), // 8 items
            MenuScheme.of("000000000", "010101010", "001010100", "010101010"), // 9 items
            MenuScheme.of("000000000", "010101010", "000101000", "010101010") // 10 items
    };

    //

    protected MenuBuilder() {}

    public static MenuBuilder create() {
        return new MenuBuilder();
    }

    public static MenuBuilder create(String title) {
        return new MenuBuilder().withTitle(title);
    }

    //

    protected String title;

    protected final List<BukkitMenuItem> items = new ArrayList<>();
    protected final Map<Integer, BukkitMenuItem> hotbar = new HashMap<>();

    //

    public final MenuBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public final MenuBuilder withItem(ItemStack item) {
        items.add(new BukkitMenuItem(item));
        return this;
    }

    public final MenuBuilder withItems(ItemStack... items) {
        return withItems(Arrays.asList(items));
    }

    public final MenuBuilder withItems(Iterable<ItemStack> items) {
        for ( ItemStack item : items ) {
            this.items.add(new BukkitMenuItem(item));
        }
        return this;
    }

    public final MenuBuilder withItem(ItemStack item, MenuItemCallback cb) {
        items.add(new BukkitMenuItem(item, cb));
        return this;
    }

    public final MenuBuilder withItem(ItemStack item, BiConsumer<ShelfPlayer, MenuClickType> cb) {
        items.add(new BukkitMenuItem(item, (player, clickType) -> {
            cb.accept(player, clickType);
            return true;
        }));
        return this;
    }

    public final MenuBuilder withHotbarItem(int slot, ItemStack item) {
        hotbar.put(slot, new BukkitMenuItem(item));
        return this;
    }

    public final MenuBuilder withHotbarItem(int slot, ItemStack item, MenuItemCallback cb) {
        hotbar.put(slot, new BukkitMenuItem(item, cb));
        return this;
    }

    public BukkitMenu build() {
        if ( items.size() > 10 ) {
            throw new UnsupportedOperationException("More than 10 items is not supported.");
        }

        MenuScheme scheme = SCHEMES[items.size()];
        int size = scheme.getRows() + 1;

        if ( !hotbar.isEmpty() ) {
            size += 1;
        }

        BukkitMenu menu = new BukkitMenu(size, title);

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
