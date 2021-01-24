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

package com.gufli.bookshelf.bukkit.gui;

import com.gufli.bookshelf.gui.AbstractInventory;
import com.gufli.bookshelf.gui.InventoryCallback;
import com.gufli.bookshelf.gui.InventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BukkitInventory extends AbstractInventory<Inventory, ItemStack> {

    public BukkitInventory(int size, String title, InventoryCallback<ItemStack> callback) {
        super(Bukkit.createInventory(null, size, title), callback);
    }

    public BukkitInventory(int size, String title) {
        super(Bukkit.createInventory(null, size, title));
    }

    public BukkitInventory(org.bukkit.inventory.Inventory inv, InventoryCallback<ItemStack> callback) {
        super(inv, callback);
    }

    public BukkitInventory(org.bukkit.inventory.Inventory inv) {
        super(inv);
    }

    public BukkitInventory(String name, int size, InventoryCallback<ItemStack> callback) {
        this(Bukkit.createInventory(null, size, name), callback);
    }

    public BukkitInventory(String name, int size) {
        this(name, size, null);
    }

    @Override
    public void removeItem(int slot) {
        super.removeItem(slot);
        handle.setItem(slot, null);
    }

    @Override
    public <V extends InventoryItem<ItemStack>> void setItem(int slot, V item) {
        super.setItem(slot, item);
        handle.setItem(slot, item.getHandle());
    }
}
