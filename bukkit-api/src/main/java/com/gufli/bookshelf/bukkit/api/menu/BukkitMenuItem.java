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
import com.gufli.bookshelf.api.menu.MenuClickType;
import com.gufli.bookshelf.api.menu.MenuItem;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;

public class BukkitMenuItem extends MenuItem<ItemStack> {

    public BukkitMenuItem(ItemStack itemStack, BiFunction<ShelfPlayer, MenuClickType, Boolean> callback) {
        super(itemStack, callback);
    }

    public BukkitMenuItem(ItemStack itemStack) {
        this(itemStack, null);
    }

}
