package com.gufli.bookshelf.event;

import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

public abstract class EventHook<T> {

    private final static Set<EventHook<?>> hooks = new CopyOnWriteArraySet<>();

    public static void register(EventHook<?> hook) {
        hooks.add(hook);
    }

    public static void unregister(EventHook<?> hook) {
        hooks.remove(hook);
    }

    public static <U> EventHook<?> getHook(Class<U> type) {
        return hooks.stream()
                .filter(inj -> inj.getType().isAssignableFrom(type))
                .max(Comparator.comparing(EventHook::getPriority))
                .orElse(null);
    }

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
