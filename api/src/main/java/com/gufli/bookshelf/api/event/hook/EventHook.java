package com.gufli.bookshelf.api.event.hook;

import java.util.function.Consumer;

public abstract class EventHook<T> {

    //

    private final int priority;
    private final Class<T> type;

    protected EventHook(Class<T> type, int priority) {
        this.type = type;
        this.priority = priority;
    }

    protected EventHook(Class<T> type) {
        this(type, 0);
    }

    public final int getPriority() {
        return priority;
    }

    public final Class<T> getType() {
        return type;
    }

    public abstract EventHandler<T> createHandler(Consumer<T> handler);

    public abstract void call(T event);

}
