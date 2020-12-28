package com.gufli.bookshelf.bukkit.events;

import com.gufli.bookshelf.events.EventManager;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class MainEventListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEvent(Event event) {
        EventManager.dispatch(event);
    }

}
