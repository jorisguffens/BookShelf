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

package com.gufli.bookshelf.bukkit.chat;

import com.gufli.bookshelf.api.chat.Chat;
import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.server.Bookshelf;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        ShelfPlayer player = Bookshelf.playerById(event.getPlayer().getUniqueId());
        Chat.handle(player, event.getMessage(), (channel, format, receivers) -> {
            event.setFormat(format);

            if ( channel.talkPrefix() != null && event.getMessage().startsWith(channel.talkPrefix())) {
                String msg = event.getMessage().substring(channel.talkPrefix().length());
                event.setMessage(msg);
            }

            try {
                event.getRecipients().clear();
                event.getRecipients().addAll(receivers.stream().map(p -> Bukkit.getPlayer(p.id())).toList());
            } catch (Exception ex) {
                event.setCancelled(true);
                String result = String.format(format, player.displayName(), event.getMessage());
                receivers.forEach(r -> r.sendMessage(result));
            }
        });
    }
}
