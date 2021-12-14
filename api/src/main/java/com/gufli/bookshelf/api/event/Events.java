package com.gufli.bookshelf.api.event;

import com.gufli.bookshelf.api.event.hook.EventHook;
import com.gufli.bookshelf.api.event.subscription.SubscriptionBuilder;

public class Events {

    private static EventManager eventManager;

    private Events() {}

    public static void register(EventManager manager) {
        if (eventManager != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton eventManager.");
        }

        eventManager = manager;
    }

    public static void registerEventHook(EventHook<?> hook) {
        eventManager.registerEventHook(hook);
    }

    public static void unregisterEventHook(EventHook<?> hook) {
        eventManager.unregisterEventHook(hook);
    }

    public static <U> EventHook<?> findHook(Class<U> type) {
        return eventManager.findHook(type);
    }

    /**
     * Makes a MergedSubscriptionBuilder for a given super type
     *
     * @param handledClass the super type of the event handler
     * @param <T>          the super type class
     * @return a {@link SubscriptionBuilder} to construct the event handler
     */
    public static <T> SubscriptionBuilder<T> subscribe(Class<T> handledClass) {
        return eventManager.subscribe(handledClass);
    }

    /**
     * Makes a MergedSubscriptionBuilder for a super event class
     *
     * @param superClass   the abstract super event class
     * @param eventClasses the event classes to be bound to
     * @param <S>          the super class type
     * @return a {@link SubscriptionBuilder} to construct the event handler
     */
    @SafeVarargs
    public static <S> SubscriptionBuilder<S> merge(Class<S> superClass, Class<? extends S>... eventClasses) {
        return eventManager.merge(superClass, eventClasses);
    }

    /**
     * Makes a MergedSubscriptionBuilder for a super event class
     *
     * @param superClass   the abstract super event class
     * @param priority     the priority to listen at
     * @param eventClasses the event classes to be bound to
     * @param <S>          the super class type
     * @return a {@link SubscriptionBuilder} to construct the event handler
     */
    @SafeVarargs
    public static <S> SubscriptionBuilder<S> merge(Class<S> superClass, EventPriority priority, Class<? extends S>... eventClasses) {
        return eventManager.merge(superClass, priority, eventClasses);
    }

    /**
     * Submit the event on the current thread
     *
     * @param event the event to call
     */
    public static <T> T call(T event) {
        return eventManager.call(event);
    }

}
