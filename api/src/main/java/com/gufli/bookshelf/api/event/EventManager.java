package com.gufli.bookshelf.api.event;

import com.gufli.bookshelf.api.event.hook.EventHook;
import com.gufli.bookshelf.api.event.subscription.SubscriptionBuilder;

public interface EventManager {

    void registerEventHook(EventHook<?> hook);

    void unregisterEventHook(EventHook<?> hook);

    <U> EventHook<?> findHook(Class<U> type);

    //

    <T> SubscriptionBuilder<T> subscribe(Class<T> handledClass);

    <S> SubscriptionBuilder<S> merge(Class<S> superClass, Class<? extends S>... eventClasses);

    <S> SubscriptionBuilder<S> merge(Class<S> superClass, EventPriority priority, Class<? extends S>... eventClasses);

    //

    <T> T call(T event);

}
