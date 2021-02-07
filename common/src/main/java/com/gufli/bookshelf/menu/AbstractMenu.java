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

package com.gufli.bookshelf.menu;


import com.gufli.bookshelf.entity.ShelfPlayer;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMenu<T, U> implements Menu<T, U> {

    protected MenuCallback<U> callback;
    protected Map<Integer, MenuItem<U>> items = new HashMap<>();

    protected T handle;

    public AbstractMenu(T handle, MenuCallback<U> callback) {
        this.handle = handle;
        this.callback = callback;
    }

    public AbstractMenu(T handle) {
        this(handle, null);
    }

    public boolean dispatchClick(ShelfPlayer player, MenuClickType clickType, int slot) {
        MenuItem<U> item = items.get(slot);

        if ( callback != null ) {
            callback.onClick(player, clickType, slot, item);
        }

        if ( item instanceof AbstractMenuItem<?>) {
            return ((AbstractMenuItem<?>) item).dispatchClick(player, clickType);
        }
        return false;
    }

    public void dispatchClose(ShelfPlayer player) {
        if ( callback != null ) {
            callback.onClose(player);
        }
    }

    public void dispatchOpen(ShelfPlayer player) {
        if ( callback != null ) {
            callback.onOpen(player);
        }
    }

    public T getHandle() {
        return handle;
    }

    public MenuItem<U> getItem(int slot) {
        return items.get(slot);
    }

    public <V extends MenuItem<U>> void setItem(int slot, V item) {
        items.put(slot, item);
    }

    public void removeItem(int slot) {
        items.remove(slot);
    }

    public Map<Integer, MenuItem<U>> getItems() {
        return items;
    }

}
