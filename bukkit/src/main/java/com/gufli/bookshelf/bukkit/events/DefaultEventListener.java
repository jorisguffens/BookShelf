package com.gufli.bookshelf.bukkit.events;

import com.gufli.bookshelf.events.Event;
import com.gufli.bookshelf.events.EventManager;
import org.bukkit.event.Listener;

public class DefaultEventListener implements Listener {

    @Event
    public void onEvent(org.bukkit.event.Event event) {
        EventManager.get().dispatch(event);
    }

}
