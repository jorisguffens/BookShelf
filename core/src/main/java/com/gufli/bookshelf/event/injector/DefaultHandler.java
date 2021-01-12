package com.gufli.bookshelf.event.injector;

import com.gufli.bookshelf.event.EventHandler;
import com.gufli.bookshelf.event.EventPriority;
import com.gufli.bookshelf.events.Event;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

public class DefaultHandler extends EventHandler<Event> {

    private final Set<Consumer<Event>> listeners = new CopyOnWriteArraySet<>();

    public DefaultHandler(Consumer<Event> handler) {
        super(handler);
    }

    @Override
    public void register(Class<? extends Event> type, EventPriority priority) {
        Consumer<Event> consumer = handler::accept;
        listeners.add(consumer);
        DefaultInjector.registerListener(type, priority, consumer);
    }

    @Override
    public void unregister() {
        listeners.forEach(DefaultInjector::unregisterListener);
        listeners.clear();
    }
}
