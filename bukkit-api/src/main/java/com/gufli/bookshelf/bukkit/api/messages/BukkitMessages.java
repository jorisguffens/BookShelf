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

package com.gufli.bookshelf.bukkit.api.messages;

import com.gufli.bookshelf.api.entity.ShelfCommandSender;
import com.gufli.bookshelf.api.messages.SimpleMessages;
import org.bukkit.ChatColor;


public class BukkitMessages extends SimpleMessages {

    @Override
    public void changePrefix(String prefix) {
        super.changePrefix(colorify(prefix));
    }

    @Override
    public String get(String name) {
        return colorify(super.get(name));
    }

    public String get(String name, boolean colorify, String... placeholders) {
        if (colorify) {
            String msg = get(name, placeholders);
            if (msg == null) return null;
            return colorify(msg);
        }
        return get(name, placeholders);
    }

    public void send(ShelfCommandSender sender, String name, boolean colorify, String... placeholders) {
        if (isEmpty(name)) return;
        sender.sendMessage(colorify(prefix()) + get(name, colorify, placeholders));
    }

    private String colorify(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

}
