package com.gufli.bookshelf.bukkit.command;

import com.gufli.bookshelf.bukkit.entity.BukkitCommandSender;
import com.gufli.bookshelf.commands.Command;
import com.gufli.bookshelf.entity.ShelfPlayer;
import com.gufli.bookshelf.entity.ShelfCommandSender;
import com.gufli.bookshelf.server.Shelf;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class BukkitCommandExecutor implements CommandExecutor, TabCompleter {

    private final Command<?> command;

    public BukkitCommandExecutor(Command<?> command) {
        this.command = command;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if ( !(sender instanceof Player) ) {
            return null;
        }

        ShelfPlayer psender = Shelf.getPlayer(((Player) sender).getUniqueId());
        return this.command.autocomplete(psender, args);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        ShelfCommandSender psender;
        if ( sender instanceof Player ) {
            psender = Shelf.getPlayer(((Player) sender).getUniqueId());
        } else {
            psender = new BukkitCommandSender(sender);
        }

        this.command.execute(psender, args);
        return true;
    }
}
