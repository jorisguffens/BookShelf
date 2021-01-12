package com.gufli.bookshelf.event;

import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

public abstract class EventInjector<T> {

    private final static Set<EventInjector<?>> injectors = new CopyOnWriteArraySet<>();

    public static void register(EventInjector<?> injector) {
        injectors.add(injector);
    }

    public static void unregister(EventInjector<?> injector) {
        injectors.remove(injector);
    }

    public static <U> EventInjector<?> getInjector(Class<U> type) {
        return injectors.stream()
                .filter(inj -> inj.getType().isAssignableFrom(type))
                .max(Comparator.comparing(EventInjector::getPriority))
                .orElse(null);
    }

    //

    private final int priority;
    private final Class<T> type;

    protected EventInjector(Class<T> type, int priority) {
        this.type = type;
        this.priority = priority;
    }

    protected EventInjector(Class<T> type) {
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
