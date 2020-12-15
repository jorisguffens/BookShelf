package com.gufli.bookshelf.events;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

class EventListenerExecutor {

    private final EventExecutor[] executors;

    private final Set<EventListenerBoundary<?>> boundaries = new HashSet<>();

    EventListenerExecutor(EventExecutor[] executors) {
        this.executors = executors;
    }

    Set<EventExecutor> match(Object event) {
        if (!boundaries.isEmpty() && !boundaries.stream().allMatch(b -> b.test(event))) {
            return Collections.emptySet();
        }
        return Arrays.stream(executors)
                .filter(ex -> ex.shouldExecute(event))
                .collect(Collectors.toSet());
    }

    public <T> EventListenerExecutor boundary(Class<T> eventType, Function<T, Boolean> callback) {
        boundaries.add(new EventListenerBoundary<>(eventType, callback));
        return this;
    }

}