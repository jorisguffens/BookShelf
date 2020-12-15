package com.gufli.bookshelf.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EventExecutor {

    private final Object eventListener;
    private final Method method;
    private final Class<?> type;
    private final EventPriority priority;

    public EventExecutor(Object eventListener, Method method) {
        this.eventListener = eventListener;
        this.method = method;

        if ( !method.isAnnotationPresent(Event.class) ) {
            throw new IllegalStateException("Method must have @Event annotation.");
        }
        this.priority = method.getAnnotation(Event.class).priority();

        if ( method.getParameterCount() != 1 ) {
            throw new IllegalStateException("Method must have exactly 1 argument.");
        }
        this.type = method.getParameterTypes()[0];
    }

    public EventPriority getPriority() {
        return priority;
    }

    public boolean shouldExecute(Object event) {
        return type.isAssignableFrom(event.getClass());
    }

    public void execute(Object event) {
        try {
            method.invoke(eventListener, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
