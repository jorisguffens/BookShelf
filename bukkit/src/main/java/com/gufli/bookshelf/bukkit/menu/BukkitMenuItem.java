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

import com.gufli.bookshelf.menu.AbstractMenuItem;
import com.gufli.bookshelf.menu.MenuItemCallback;
import org.bukkit.inventory.ItemStack;

public class BukkitMenuItem extends AbstractMenuItem<ItemStack> {

    public BukkitMenuItem(ItemStack itemStack, MenuItemCallback callback) {
        super(itemStack, callback);
    }

    public BukkitMenuItem(ItemStack itemStack) {
        this(itemStack, null);
    }

}
