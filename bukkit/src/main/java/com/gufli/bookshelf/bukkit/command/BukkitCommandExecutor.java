package com.gufli.bookshelf.bukkit.command;

import com.gufli.bookshelf.api.command.Command;
import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.entity.ShelfCommandSender;
import com.gufli.bookshelf.api.server.Bookshelf;
import com.gufli.bookshelf.bukkit.api.entity.BukkitCommandSender;
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

        ShelfPlayer psender = Bookshelf.getPlayer(((Player) sender).getUniqueId());
        return this.command.autocomplete(psender, args);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        ShelfCommandSender psender;
        if ( sender instanceof Player ) {
            psender = Bookshelf.getPlayer(((Player) sender).getUniqueId());
        } else {
            psender = new BukkitCommandSender(sender);
        }

        this.command.execute(psender, args);
        return true;
    }
}
