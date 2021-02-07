package com.gufli.bookshelf.event.hook;

import com.gufli.bookshelf.event.EventHandler;
import com.gufli.bookshelf.event.EventPriority;
import com.gufli.bookshelf.events.Event;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

public class ShelfEventHandler extends EventHandler<Event> {

    private final Set<Consumer<Event>> listeners = new CopyOnWriteArraySet<>();

    public ShelfEventHandler(Consumer<Event> handler) {
        super(handler);
    }

    @Override
    public void register(Class<? extends Event> type, EventPriority priority) {
        Consumer<Event> consumer = e -> handler.accept(e);
        listeners.add(consumer);
        ShelfEventHook.registerListener(type, priority, consumer);
    }

    @Override
    public void unregister() {
        listeners.forEach(ShelfEventHook::unregisterListener);
        listeners.clear();
    }
}
