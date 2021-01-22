package com.gufli.bookshelf.bukkit.event;

import com.gufli.bookshelf.event.EventHandler;
import com.gufli.bookshelf.event.EventHook;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class BukkitEventHook extends EventHook<Event> {

    private final JavaPlugin plugin;

    public BukkitEventHook(JavaPlugin plugin) {
        super(Event.class, 10);
        this.plugin = plugin;
    }

    @Override
    public EventHandler<Event> createHandler(Consumer<Event> handler) {
        return new BukkitEventHandler(plugin, handler);
    }

    @Override
    public void call(Event event) {
        plugin.getServer().getPluginManager().callEvent(event);
    }
}


