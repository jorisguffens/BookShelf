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

package com.gufli.bookshelf.bukkit.menu;

import com.gufli.bookshelf.menu.AbstractMenu;
import com.gufli.bookshelf.menu.MenuCallback;
import com.gufli.bookshelf.menu.MenuItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BukkitMenu extends AbstractMenu<Inventory, ItemStack> {

    public BukkitMenu(int size, String title, MenuCallback<ItemStack> callback) {
        super(Bukkit.createInventory(null, size, title), callback);
    }

    public BukkitMenu(int size, String title) {
        super(Bukkit.createInventory(null, size, title));
    }

    public BukkitMenu(org.bukkit.inventory.Inventory inv, MenuCallback<ItemStack> callback) {
        super(inv, callback);
    }

    public BukkitMenu(org.bukkit.inventory.Inventory inv) {
        super(inv);
    }

    public BukkitMenu(String name, int size, MenuCallback<ItemStack> callback) {
        this(Bukkit.createInventory(null, size, name), callback);
    }

    public BukkitMenu(String name, int size) {
        this(name, size, null);
    }

    @Override
    public void removeItem(int slot) {
        super.removeItem(slot);
        handle.setItem(slot, null);
    }

    @Override
    public <V extends MenuItem<ItemStack>> void setItem(int slot, V item) {
        super.setItem(slot, item);
        handle.setItem(slot, item.getHandle());
    }
}
