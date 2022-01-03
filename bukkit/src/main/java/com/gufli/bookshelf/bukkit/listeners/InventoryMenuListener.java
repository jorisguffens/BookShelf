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

package com.gufli.bookshelf.bukkit.listeners;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.server.Bookshelf;
import com.gufli.bookshelf.bukkit.api.entity.BukkitPlayer;
import com.gufli.bookshelf.bukkit.api.menu.InventoryMenu;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class InventoryMenuListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        ShelfPlayer player = Bookshelf.playerById(e.getPlayer().getUniqueId());
        handleClose(player);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) {
            return;
        }

        ShelfPlayer player = Bookshelf.playerById(e.getPlayer().getUniqueId());
        if (player == null) {
            return;
        }

        handleClose(player);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p)) {
            return;
        }

        ShelfPlayer player = Bookshelf.playerById(e.getWhoClicked().getUniqueId());
        if (player == null) {
            return;
        }

        BukkitPlayer bp = (BukkitPlayer) player;
        InventoryMenu inv = bp.openedMenu();
        if (inv == null || inv.handle() == null) {
            return;
        }

        if (e.getRawSlot() >= e.getView().getTopInventory().getSize()) {
            return;
        }
//        if ( !inv.getHandle().equals(e.getView().getTopInventory()) ) {
//            return;
//        }

        e.setCancelled(true);

        boolean playSound = false;

        try {
            playSound = inv.dispatchClick(bp, e.getClick(), e.getRawSlot());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!playSound) {
            return;
        }

        // play a sound
        Sound sound;
        try {
            sound = Sound.valueOf("CLICK"); // 1.8
        } catch (IllegalArgumentException ex) {
            sound = Sound.valueOf("UI_BUTTON_CLICK"); // 1.9 +
        }

        p.playSound(p.getLocation().add(0, 1.8, 0), sound, 1f, 1f);
    }

    private void handleClose(ShelfPlayer player) {
        BukkitPlayer bp = (BukkitPlayer) player;
        InventoryMenu inv = bp.openedMenu();
        if (inv == null) {
            return;
        }

        inv.dispatchClose(bp);
        bp.remove(BukkitPlayer.CUSTOM_MENU_KEY);
    }

}
