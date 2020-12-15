package com.gufli.bookshelf.events;

import java.util.function.Function;

class EventListenerBoundary<T> {

    private final Class<T> eventType;
    private final Function<T, Boolean> callback;

    EventListenerBoundary(Class<T> eventType, Function<T, Boolean> callback) {
        this.eventType = eventType;
        this.callback = callback;
    }

    boolean test(Object event) {
        if (!eventType.isAssignableFrom(event.getClass())) {
            return true;
        }

        return callback.apply((T) event);
    }

}