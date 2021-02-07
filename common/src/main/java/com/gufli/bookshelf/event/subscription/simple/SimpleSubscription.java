/*
 * This file is part of helper, licensed under the MIT License.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.gufli.bookshelf.event.subscription.simple;

import com.google.common.collect.ImmutableMap;
import com.gufli.bookshelf.event.EventHandler;
import com.gufli.bookshelf.event.EventHook;
import com.gufli.bookshelf.event.subscription.Subscription;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class SimpleSubscription<T> implements Subscription<T> {

    protected final Class<T> handledClass;
    protected final Map<Class<?>, SimpleHandlerMapping<T, ?>> mappings;

    protected final BiConsumer<? super T, Throwable> exceptionConsumer;

    protected final Predicate<T>[] filters;

    protected final BiPredicate<SimpleSubscription<T>, T>[] preExpiryTests;
    protected final BiPredicate<SimpleSubscription<T>, T>[] midExpiryTests;
    protected final BiPredicate<SimpleSubscription<T>, T>[] postExpiryTests;

    protected final BiConsumer<SimpleSubscription<T>, ? super T>[] handlers;

    protected final AtomicLong callCount = new AtomicLong(0);
    protected final AtomicBoolean active = new AtomicBoolean(true);

    private final Map<EventHook<?>, EventHandler<?>> eventHandlers = new HashMap<>();

    @SuppressWarnings("unchecked")
    SimpleSubscription(SimpleSubscriptionBuilder<T> builder, List<BiConsumer<SimpleSubscription<T>, ? super T>> handlers) {
        this.handledClass = builder.handledClass;
        this.mappings = ImmutableMap.copyOf(builder.mappings);
        this.exceptionConsumer = builder.exceptionConsumer;

        this.filters = builder.filters.toArray(new Predicate[0]);
        this.preExpiryTests = builder.preExpiryTests.toArray(new BiPredicate[0]);
        this.midExpiryTests = builder.midExpiryTests.toArray(new BiPredicate[0]);
        this.postExpiryTests = builder.postExpiryTests.toArray(new BiPredicate[0]);
        this.handlers = handlers.toArray(new BiConsumer[0]);
    }

    void register() {
        for (Map.Entry<Class<?>, SimpleHandlerMapping<T, ?>> ent : this.mappings.entrySet()) {
            register(ent.getKey());
        }
    }

    private <U> void register(Class<U> type) {
        EventHook<? super U> injector = (EventHook<? super U>) EventHook.getHook(type);
        if ( injector == null ) {
            throw new RuntimeException("Cannot register event of type " + type.getSimpleName() + " because there is no valid injector.");
        }

        EventHandler<? super U> handler;
        if ( eventHandlers.containsKey(injector) ) {
            handler = (EventHandler<? super U>)eventHandlers.get(injector);
        } else {
            handler = injector.createHandler(this::handler);
            eventHandlers.put(injector, handler);
        }

        handler.register(type);
    }

    public boolean unregister() {
        if ( !this.active.getAndSet(false) ) {
            return false;
        }

        for ( EventHandler<?> handler : eventHandlers.values() ) {
            handler.unregister();
        }

        eventHandlers.clear();
        return true;
    }

    private void handler(Object event) {
        SimpleHandlerMapping<T, ?> mapping = this.mappings.get(event.getClass());
        if (mapping == null) {
            return;
        }

        Function<Object, T> function = mapping.getFunction();

        // this handler is disabled, so unregister from the event.
        if (!this.active.get()) {
            unregister();
            return;
        }

        // obtain the handled instance
        T handledInstance = function.apply(event);

        // check pre-expiry tests
        for (BiPredicate<SimpleSubscription<T>, T> test : this.preExpiryTests) {
            if (test.test(this, handledInstance)) {
                unregister();
                this.active.set(false);
                return;
            }
        }

        // begin "handling" of the event
        try {
            // check the filters
            for (Predicate<T> filter : this.filters) {
                if (!filter.test(handledInstance)) {
                    return;
                }
            }

            // check mid-expiry tests
            for (BiPredicate<SimpleSubscription<T>, T> test : this.midExpiryTests) {
                if (test.test(this, handledInstance)) {
                    unregister();
                    this.active.set(false);
                    return;
                }
            }

            // call the handler
            for (BiConsumer<SimpleSubscription<T>, ? super T> handler : this.handlers) {
                handler.accept(this, handledInstance);
            }

            // increment call counter
            this.callCount.incrementAndGet();
        } catch (Throwable t) {
            this.exceptionConsumer.accept((T) event, t);
        }

        // check post-expiry tests
        for (BiPredicate<SimpleSubscription<T>, T> test : this.postExpiryTests) {
            if (test.test(this, handledInstance)) {
                unregister();
                this.active.set(false);
                return;
            }
        }
    }

    @Override
    public final boolean isActive() {
        return this.active.get();
    }

    @Override
    public final boolean isClosed() {
        return !this.active.get();
    }

    @Override
    public final long getCallCounter() {
        return this.callCount.get();
    }

    @Override
    public final Class<? super T> getHandledClass() {
        return this.handledClass;
    }

    @Override
    public final Set<Class<?>> getEventClasses() {
        return this.mappings.keySet();
    }
}
