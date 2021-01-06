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

package com.gufli.bookshelf.bukkit.entity;

import com.gufli.bookshelf.bukkit.util.LocationConverter;
import com.gufli.bookshelf.entity.AbstractShelfPlayer;
import com.gufli.bookshelf.entity.ShelfLocation;
import com.gufli.bookshelf.gui.AbstractInventory;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitPlayer extends AbstractShelfPlayer {

    private boolean admin;
    private final Player player;

    public BukkitPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BukkitPlayer && ((BukkitPlayer) obj).player == player;
    }

    //

    @Override
    public UUID getUniqueId() {
        return this.player.getUniqueId();
    }

    @Override
    public String getName() {
        return this.player.getName();
    }

    @Override
    public void teleport(ShelfLocation location) {
        Location loc = LocationConverter.convert(location);
        if ( loc == null ) {
            return;
        }

        player.teleport(loc);
    }

    @Override
    public ShelfLocation getLocation() {
        return LocationConverter.convert(player.getLocation());
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public void sendMessage(String msg) {
        player.sendMessage(msg);
    }

    @Override
    public boolean isOnline() {
        return player.isOnline();
    }


    // gui

    @Override
    public void openInventory(AbstractInventory<?, ?> inventory) {
        if ( inventory.getHandle() instanceof org.bukkit.inventory.Inventory) {
            player.openInventory((org.bukkit.inventory.Inventory) inventory.getHandle());
            super.openInventory(inventory);
        }
    }

    @Override
    public void closeInventory() {
        player.closeInventory();
    }

}
