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

import java.util.UUID;

public interface ShelfPlayer extends ShelfCommandSender, DataHolder {

    UUID id();

    String name();

    String displayName();

    void teleport(ShelfLocation location);

    ShelfLocation location();

    String locale();

    String ipAddress();

}
