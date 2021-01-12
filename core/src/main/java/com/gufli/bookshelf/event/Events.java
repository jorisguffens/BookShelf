package com.gufli.bookshelf.event;

import com.gufli.bookshelf.event.injector.DefaultInjector;
import com.gufli.bookshelf.event.subscription.simple.SimpleSubscriptionBuilder;

public class Events {

    static {
        DefaultInjector injector = new DefaultInjector();
        EventInjector.register(injector);
    }

    /**
     * Makes a MergedSubscriptionBuilder for a given super type
     *
     * @param handledClass the super type of the event handler
     * @param <T>          the super type class
     * @return a {@link SimpleSubscriptionBuilder} to construct the event handler
     */
    public static <T> SimpleSubscriptionBuilder<T> subscribe(Class<T> handledClass) {
        SimpleSubscriptionBuilder<T> builder = SimpleSubscriptionBuilder.newBuilder(handledClass);
        builder.bindEvent(handledClass, e -> e);
        return builder;
    }

    /**
     * Makes a MergedSubscriptionBuilder for a super event class
     *
     * @param superClass   the abstract super event class
     * @param eventClasses the event classes to be bound to
     * @param <S>          the super class type
     * @return a {@link SimpleSubscriptionBuilder} to construct the event handler
     */
    @SafeVarargs
    public static <S> SimpleSubscriptionBuilder<S> merge(Class<S> superClass, Class<? extends S>... eventClasses) {
        return SimpleSubscriptionBuilder.newBuilder(superClass, eventClasses);
    }

    /**
     * Makes a MergedSubscriptionBuilder for a super event class
     *
     * @param superClass   the abstract super event class
     * @param priority     the priority to listen at
     * @param eventClasses the event classes to be bound to
     * @param <S>          the super class type
     * @return a {@link SimpleSubscriptionBuilder} to construct the event handler
     */
    @SafeVarargs
    public static <S> SimpleSubscriptionBuilder<S> merge(Class<S> superClass, EventPriority priority, Class<? extends S>... eventClasses) {
        return SimpleSubscriptionBuilder.newBuilder(superClass, priority, eventClasses);
    }

    /**
     * Submit the event on the current thread
     *
     * @param event the event to call
     */
    public static <T> T call(T event) {
        EventInjector<? super T> injector = (EventInjector<? super T>) EventInjector.getInjector(event.getClass());
        if ( injector == null ) {
            throw new RuntimeException("Cannot register event of type " + event.getClass().getSimpleName() + " because there is no valid injector.");
        }
        injector.call(event);
        return event;
    }

    private Events() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }



}
