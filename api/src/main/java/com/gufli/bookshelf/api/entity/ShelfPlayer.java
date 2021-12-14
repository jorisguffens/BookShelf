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

package com.gufli.bookshelf.api.entity;



import com.gufli.bookshelf.api.location.ShelfLocation;
import com.gufli.bookshelf.api.menu.Menu;

import java.util.UUID;

public interface ShelfPlayer extends ShelfCommandSender {

    UUID getUniqueId();

    String getName();

    void teleport(ShelfLocation location);

    ShelfLocation getLocation();

    boolean isOnline();


    // gui

    String CUSTOM_MENU_KEY = "CUSTOM_MENU";

    default Menu<?, ?> getCurrentMenu() {
        return has(CUSTOM_MENU_KEY) ? get(CUSTOM_MENU_KEY, Menu.class) : null;
    }

    void openMenu(Menu<?, ?> inventory);

    void closeMenu();

    // cache

    void set(String key, Object value);

    boolean has(String key);

    void remove(String key);

    Object get(String key);

    <T> T get(String key, Class<T> clazz);
}
