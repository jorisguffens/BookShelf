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
import com.gufli.bookshelf.api.menu.Menu;
import com.gufli.bookshelf.bukkit.api.location.LocationConverter;
import com.gufli.bookshelf.entity.AbstractShelfPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitPlayer extends AbstractShelfPlayer {

    private final Player handle;

    public BukkitPlayer(Player handle) {
        this.handle = handle;
    }

    public Player getHandle() {
        return handle;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BukkitPlayer && ((BukkitPlayer) obj).handle == handle;
    }

    //

    @Override
    public UUID getUniqueId() {
        return this.handle.getUniqueId();
    }

    @Override
    public String getName() {
        return this.handle.getName();
    }

    @Override
    public void teleport(ShelfLocation location) {
        Location loc = LocationConverter.convert(location);
        if ( loc == null ) {
            return;
        }

        handle.teleport(loc);
    }

    @Override
    public ShelfLocation getLocation() {
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

    @Override
    public boolean isOnline() {
        return handle.isOnline();
    }

    // gui

    @Override
    public void openMenu(Menu<?, ?> inventory) {
        if ( inventory.getHandle() instanceof org.bukkit.inventory.Inventory) {
            handle.openInventory((org.bukkit.inventory.Inventory) inventory.getHandle());
            set(CUSTOM_MENU_KEY, inventory);
        }
    }

    @Override
    public void closeMenu() {
        handle.closeInventory();
    }

}
