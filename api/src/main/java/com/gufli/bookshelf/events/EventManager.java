package com.gufli.bookshelf.events;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class EventManager {

    private final static EventManager manager = new EventManager();

    public static EventManager get() {
        return manager;
    }

    private EventManager() {}

    //

    private final Map<EventListener, EventListenerExecutor> listeners = new HashMap<>();

    public EventListenerExecutor register(EventListener listener) {
        List<EventExecutor> executors = new ArrayList<>();
        for ( Method method : listener.getClass().getMethods() ) {
            if ( method.getParameterCount() != 1 ) {
                continue;
            }

            if ( !method.isAnnotationPresent(Event.class) ) {
                continue;
            }
            executors.add(new EventExecutor(listener, method));
        }

        EventListenerExecutor eventListenerExecutor = new EventListenerExecutor(executors.toArray(new EventExecutor[0]));
        listeners.put(listener, eventListenerExecutor);

        return eventListenerExecutor;
    }

    public void unregister(EventListener listener) {
        listeners.remove(listener);
    }

    public void dispatch(Object event) {
        List<EventExecutor> executors = listeners.values().stream()
                .flatMap(ex -> ex.match(event).stream())
                .sorted(Comparator.comparing(ex -> ex.getPriority().ordinal()))
                .collect(Collectors.toList());

        for ( EventExecutor ex : executors ) {
            ex.execute(event);
        }
    }

}
