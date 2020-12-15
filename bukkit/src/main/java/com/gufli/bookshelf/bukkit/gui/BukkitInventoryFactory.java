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

import com.gufli.bookshelf.gui.*;

public class BukkitInventoryFactory implements InventoryFactory<BukkitInventory, BukkitInventoryItem> {
    
    public BukkitInventoryFactory() {
        InventoryFactoryRegistry.register(this);
    }
    
    @Override
    public BukkitInventory createInventory(Object handle) {
        return createInventory(handle, null);
    }

    @Override
    public BukkitInventory createInventory(Object handle, InventoryCallback callback) {
        if ( !(handle instanceof org.bukkit.inventory.Inventory) ) {
            throw new IllegalArgumentException("handle must be of type org.bukkit.inventory.Inventory");
        }
        return new BukkitInventory((org.bukkit.inventory.Inventory) handle, callback);
    }

    @Override
    public BukkitInventoryItem createItem(Object handle) {
        return createItem(handle, null);
    }

    @Override
    public BukkitInventoryItem createItem(Object handle, InventoryItemCallback callback) {
        if ( !(handle instanceof org.bukkit.inventory.ItemStack) ) {
            throw new IllegalArgumentException("handle must be of type org.bukkit.inventory.ItemStack");
        }
        return new BukkitInventoryItem((org.bukkit.inventory.ItemStack) handle, callback);
    }
}
