package com.gufli.bookshelf.event;

import com.gufli.bookshelf.api.event.EventManager;
import com.gufli.bookshelf.api.event.EventPriority;
import com.gufli.bookshelf.api.event.hook.EventHook;
import com.gufli.bookshelf.event.hook.ShelfEventHook;
import com.gufli.bookshelf.event.subscription.SimpleSubscriptionBuilder;

import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class SimpleEventManager implements EventManager {

    private final Set<EventHook<?>> hooks = new CopyOnWriteArraySet<>();

    public SimpleEventManager() {
        ShelfEventHook injector = new ShelfEventHook();
        registerEventHook(injector);
    }

    public <U> EventHook<?> findHook(Class<U> type) {
        return hooks.stream()
                .filter(inj -> inj.getType().isAssignableFrom(type))
                .max(Comparator.comparing(EventHook::getPriority))
                .orElse(null);
    }

    public void registerEventHook(EventHook<?> hook) {
        hooks.add(hook);
    }

    public void unregisterEventHook(EventHook<?> hook) {
        hooks.remove(hook);
    }

    public <T> SimpleSubscriptionBuilder<T> subscribe(Class<T> handledClass) {
        SimpleSubscriptionBuilder<T> builder = SimpleSubscriptionBuilder.newBuilder(handledClass);
        builder.bindEvent(handledClass, e -> e);
        return builder;
    }

    public <T> SimpleSubscriptionBuilder<T> subscribe(Class<T> handledClass, EventPriority priority) {
        SimpleSubscriptionBuilder<T> builder = SimpleSubscriptionBuilder.newBuilder(handledClass);
        builder.bindEvent(handledClass, e -> e);
        return builder;
    }

    public <S> SimpleSubscriptionBuilder<S> merge(Class<S> superClass, Class<? extends S>... eventClasses) {
        return SimpleSubscriptionBuilder.newBuilder(superClass, eventClasses);
    }

    public <S> SimpleSubscriptionBuilder<S> merge(Class<S> superClass, EventPriority priority, Class<? extends S>... eventClasses) {
        return SimpleSubscriptionBuilder.newBuilder(superClass, priority, eventClasses);
    }

    public <T> T call(T event) {
        EventHook<? super T> injector = (EventHook<? super T>) findHook(event.getClass());
        if ( injector == null ) {
            throw new RuntimeException("Cannot register event of type " + event.getClass().getSimpleName() + " because there is no valid hook.");
        }
        injector.call(event);
        return event;
    }

}
