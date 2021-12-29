package com.gufli.bookshelf.bukkit.commands;


import com.gufli.bookshelf.api.color.TextColor;
import com.gufli.bookshelf.api.command.Command;
import com.gufli.bookshelf.api.command.CommandInfo;
import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.bukkit.api.entity.BukkitPlayer;
import com.gufli.bookshelf.bukkit.api.item.ItemStackBuilder;
import com.gufli.bookshelf.bukkit.api.menu.BukkitMenu;
import com.gufli.bookshelf.bukkit.api.menu.MenuBuilder;
import com.gufli.bookshelf.messages.DefaultMessages;
import org.bukkit.inventory.ItemStack;

@CommandInfo(commands = "menu", minArguments = 0)
public class BookshelfMenuCommand extends Command<ShelfPlayer> {

    @Override
    protected void onExecute(ShelfPlayer player, String[] args) {
        BukkitPlayer bp = (BukkitPlayer) player;

        ItemStack item = ItemStackBuilder.skull()
                .withSkullOwner(bp.getHandle())
                .withName(TextColor.AQUA + player.name())
                .withLore("Click for something fancy")
                .build();

        BukkitMenu menu = MenuBuilder.create(TextColor.DARK_RED + "Fancy menu")
                .withItem(item, (p, type) -> {
                    p.sendMessage("poggers");
                })
                .build();

        bp.openMenu(menu);
        DefaultMessages.send(player, "cmd.bookshelf.menu");
    }

}
