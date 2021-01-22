package com.gufli.bookshelf.event.hook;

import com.gufli.bookshelf.event.EventHandler;
import com.gufli.bookshelf.event.EventHook;
import com.gufli.bookshelf.event.EventPriority;
import com.gufli.bookshelf.events.Event;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ShelfEventHook extends EventHook<Event> {

    static Map<Class<? extends Event>, Map<Consumer<? extends Event>, EventPriority>> listeners = new ConcurrentHashMap<>();

    static void registerListener(Class<? extends Event> type, Consumer<? extends Event> consumer) {
        registerListener(type, EventPriority.NORMAL, consumer);
    }

    static void registerListener(Class<? extends Event> type, EventPriority priority, Consumer<? extends Event> consumer) {
        Map<Consumer<? extends Event>, EventPriority> consumers = listeners.computeIfAbsent(type, clazz -> new ConcurrentHashMap<>());
        consumers.put(consumer, priority);
    }

    static void unregisterListener(Consumer<? extends Event> consumer) {
        for ( Class<? extends Event> type : new HashSet<>(listeners.keySet()) ) {
            Map<Consumer<? extends Event>, EventPriority> consumers = listeners.get(type);
            consumers.remove(consumer);

            if ( consumers.isEmpty() ) {
                listeners.remove(type);
            }
        }
    }


    //

    public ShelfEventHook() {
        super(Event.class, 0);
    }

    @Override
    public EventHandler<Event> createHandler(Consumer<Event> handler) {
        return new ShelfEventHandler(handler);
    }

    @Override
    public void call(Event event) {
        for ( Class<?> type : new HashSet<>(listeners.keySet()) ) {
            if ( !type.isAssignableFrom(event.getClass()) ) continue;
            Map<Consumer<? extends Event>, EventPriority> consumers = listeners.get(type);

            consumers.keySet().stream()
                    .sorted(Comparator.comparing(c -> consumers.get(c).ordinal()))
                    .forEach(consumer -> ((Consumer<Event>) consumer).accept(event));
        }
    }
}
