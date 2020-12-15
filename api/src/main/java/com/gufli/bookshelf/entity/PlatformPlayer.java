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

package com.gufli.bookshelf.entity;


import com.gufli.bookshelf.gui.Inventory;

import java.util.UUID;

public interface PlatformPlayer extends PlatformSender {

    UUID getUniqueId();

    String getName();

    void teleport(PlatformLocation location);

    PlatformLocation getLocation();


    // gui

    String CUSTOM_GUI_KEY = "CUSTOM_GUI";

    default Inventory<?, ?> getInventory() {
        return has(CUSTOM_GUI_KEY) ? get(CUSTOM_GUI_KEY, Inventory.class) : null;
    }

    default void openInventory(Inventory<?, ?> inventory) {
        set(CUSTOM_GUI_KEY, inventory);
    }

    void closeInventory();

    // cache

    void set(String key, Object value);

    boolean has(String key);

    void remove(String key);

    Object get(String key);

    <T> T get(String key, Class<T> clazz);
}
