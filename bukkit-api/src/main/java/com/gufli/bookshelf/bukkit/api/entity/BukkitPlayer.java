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

package com.gufli.bookshelf.bukkit.api.entity;

import com.gufli.bookshelf.api.location.ShelfLocation;
import com.gufli.bookshelf.bukkit.api.location.LocationConverter;
import com.gufli.bookshelf.bukkit.api.menu.InventoryMenu;
import com.gufli.bookshelf.entity.AbstractShelfPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitPlayer extends AbstractShelfPlayer {

    private final Player handle;

    public BukkitPlayer(Player handle) {
        this.handle = handle;
    }

    public Player handle() {
        return handle;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BukkitPlayer && ((BukkitPlayer) obj).handle == handle;
    }

    //

    @Override
    public UUID id() {
        return this.handle.getUniqueId();
    }

    @Override
    public String name() {
        return this.handle.getName();
    }

    @Override
    public String displayName() {
        return this.handle.getDisplayName();
    }

    @Override
    public void teleport(ShelfLocation location) {
        Location loc = LocationConverter.convert(location);
        if (loc == null) {
            return;
        }

        handle.teleport(loc);
    }

    @Override
    public ShelfLocation location() {
        return LocationConverter.convert(handle.getLocation());
    }

    @Override
    public boolean hasPermission(String permission) {
        return handle.hasPermission(permission);
    }

    @Override
    public void sendMessage(String msg) {
        handle.sendMessage(msg);
    }

    // gui

    private final String CUSTOM_MENU_KEY = "BUKKIT_INVENTORY_MENU";

    public void openMenu(InventoryMenu inventoryMenu) {
        handle.openInventory(inventoryMenu.handle());
        set(CUSTOM_MENU_KEY, inventoryMenu);
        inventoryMenu.dispatchOpen(this);
    }

    public InventoryMenu openedMenu() {
        if (has(CUSTOM_MENU_KEY)) {
            return get(CUSTOM_MENU_KEY, InventoryMenu.class);
        }
        return null;
    }

    public void closeMenu() {
        handle.closeInventory();
        remove(CUSTOM_MENU_KEY);
    }

}
