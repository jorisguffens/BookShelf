package com.gufli.bookshelf.api.event.hook;

import com.gufli.bookshelf.api.event.EventPriority;

import java.util.function.Consumer;

public abstract class EventHandler<T> {

    protected final Consumer<T> handler;

    public EventHandler(Consumer<T> handler) {
        this.handler = handler;
    }

    public void register(Class<? extends T> type) {
        register(type, EventPriority.NORMAL);
    }

    public abstract void register(Class<? extends T> type, EventPriority priority);

    public abstract void unregister();

}
