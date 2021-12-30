/*
 * This file is part of KingdomCraft.
 *
 * KingdomCraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KingdomCraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with KingdomCraft. If not, see <https://www.gnu.org/licenses/>.
 */

package com.gufli.bookshelf.bukkit.api.menu;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.bukkit.api.entity.BukkitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class InventoryMenu {

    private final InventoryMenuCallback callback;
    private final Map<Integer, InventoryMenuItem> items = new HashMap<>();

    private final Inventory handle;

    public InventoryMenu(int rows, String title, InventoryMenuCallback callback) {
        this.handle = Bukkit.createInventory(null, rows * 9, title);
        this.callback = callback;
    }

    public InventoryMenu(int rows, String title) {
        this(Bukkit.createInventory(null, rows * 9, title), null);
    }

    public InventoryMenu(Inventory inv, InventoryMenuCallback callback) {
        this.handle = inv;
        this.callback = callback;
    }

    public InventoryMenu(Inventory inv) {
        this(inv, null);
    }

    public InventoryMenu(String name, int size, InventoryMenuCallback callback) {
        this(Bukkit.createInventory(null, size, name), callback);
    }

    public InventoryMenu(String name, int size) {
        this(name, size, null);
    }

    public void removeItem(int slot) {
        items.remove(slot);
        handle.setItem(slot, null);
    }

    public void setItem(int slot, InventoryMenuItem item) {
        items.put(slot, item);
        handle.setItem(slot, item.handle());
    }

    public Inventory handle() {
        return handle;
    }

    public InventoryMenuItem itemAt(int slot) {
        return items.get(slot);
    }

    public Map<Integer, InventoryMenuItem> items() {
        return Collections.unmodifiableMap(items);
    }

    // --- EVENTS ---

    public boolean dispatchClick(BukkitPlayer player, ClickType clickType, int slot) {
        InventoryMenuItem item = items.get(slot);

        if ( callback != null ) {
            callback.onClick(player, clickType, slot, item);
        }

        return item.dispatchClick(player, clickType);
    }

    public void dispatchClose(BukkitPlayer player) {
        if ( callback != null ) {
            callback.onClose(player);
        }
    }

    public void dispatchOpen(BukkitPlayer player) {
        if ( callback != null ) {
            callback.onOpen(player);
        }
    }
}
