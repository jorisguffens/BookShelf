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

package com.gufli.bookshelf.event.subscription;

import com.google.common.base.Preconditions;
import com.gufli.bookshelf.api.util.delegates.Delegates;
import com.gufli.bookshelf.api.event.EventPriority;
import com.gufli.bookshelf.api.event.subscription.ExpiryTestStage;
import com.gufli.bookshelf.api.event.subscription.SubscriptionBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.*;

public class SimpleSubscriptionBuilder<T> implements SubscriptionBuilder<T> {

    final Class<T> handledClass;
    final Map<Class<?>, SimpleHandlerMapping<T, ?>> mappings = new HashMap<>();

    BiConsumer<? super T, Throwable> exceptionConsumer = DEFAULT_EXCEPTION_CONSUMER;

    final List<Predicate<T>> filters = new ArrayList<>();
    final List<BiPredicate<SimpleSubscription<T>, T>> preExpiryTests = new ArrayList<>(0);
    final List<BiPredicate<SimpleSubscription<T>, T>> midExpiryTests = new ArrayList<>(0);
    final List<BiPredicate<SimpleSubscription<T>, T>> postExpiryTests = new ArrayList<>(0);

    SimpleSubscriptionBuilder(Class<T> handledClass) {
        this.handledClass = handledClass;
    }

    /**
     * Makes a MergedHandlerBuilder for a given super type
     *
     * @param handledClass the super type of the event handler
     * @param <T>          the super type class
     * @return a {@link SimpleSubscriptionBuilder} to construct the event handler
     */
    public static <T> SimpleSubscriptionBuilder<T> newBuilder(Class<T> handledClass) {
        Objects.requireNonNull(handledClass, "handledClass");
        return new SimpleSubscriptionBuilder<>(handledClass);
    }

    /**
     * Makes a MergedHandlerBuilder for a super event class
     *
     * @param superClass   the abstract super event class
     * @param eventClasses the event classes to be bound to
     * @param <S>          the super class type
     * @return a {@link SimpleSubscriptionBuilder} to construct the event handler
     */
    @SafeVarargs
    public static <S> SimpleSubscriptionBuilder<S> newBuilder(Class<S> superClass, Class<? extends S>... eventClasses) {
        return newBuilder(superClass, EventPriority.NORMAL, eventClasses);
    }

    /**
     * Makes a MergedHandlerBuilder for a super event class
     *
     * @param superClass   the abstract super event class
     * @param priority     the priority to listen at
     * @param eventClasses the event classes to be bound to
     * @param <S>          the super class type
     * @return a {@link SimpleSubscriptionBuilder} to construct the event handler
     */
    @SafeVarargs
    public static <S> SimpleSubscriptionBuilder<S> newBuilder(Class<S> superClass, EventPriority priority, Class<? extends S>... eventClasses) {
        Objects.requireNonNull(superClass, "superClass");
        Objects.requireNonNull(eventClasses, "eventClasses");
        Objects.requireNonNull(priority, "priority");
        if (eventClasses.length < 2) {
            throw new IllegalArgumentException("merge method used for only one subclass");
        }

        SimpleSubscriptionBuilder<S> h = new SimpleSubscriptionBuilder<>(superClass);
        for (Class<? extends S> clazz : eventClasses) {
            h.bindEvent(clazz, priority, e -> e);
        }
        return h;
    }

    // override return type - we return SimpleSubscriptionBuilder, not SubscriptionBuilder

    @Override
    public SimpleSubscriptionBuilder<T> expireIf(Predicate<T> predicate) {
        return expireIf(Delegates.predicateToBiPredicateSecond(predicate), ExpiryTestStage.PRE, ExpiryTestStage.POST_HANDLE);
    }

    @Override
    public SimpleSubscriptionBuilder<T> expireAfter(long duration, TimeUnit unit) {
        Objects.requireNonNull(unit, "unit");
        Preconditions.checkArgument(duration >= 1, "duration < 1");
        long expiry = Math.addExact(System.currentTimeMillis(), unit.toMillis(duration));
        return expireIf((handler, event) -> System.currentTimeMillis() > expiry, ExpiryTestStage.PRE);
    }

    @Override
    public SimpleSubscriptionBuilder<T> expireAfter(long maxCalls) {
        Preconditions.checkArgument(maxCalls >= 1, "maxCalls < 1");
        return expireIf((handler, event) -> handler.getCallCounter() >= maxCalls, ExpiryTestStage.PRE, ExpiryTestStage.POST_HANDLE);
    }

    @Override
    public SimpleSubscription<T> handler(Consumer<? super T> handler) {
        return handlers().consumer(handler).register();
    }

    /**
     * Builds and registers the Handler.
     *
     * @param handler the bi-consumer responsible for handling the event.
     * @return a registered {@link SimpleSubscription} instance.
     * @throws NullPointerException  if the handler is null
     * @throws IllegalStateException if no events have been bound to
     */
    public SimpleSubscription<T> biHandler(BiConsumer<SimpleSubscription<T>, ? super T> handler) {
        return handlers().biConsumer(handler).register();
    }

    @Override
    public <E> SimpleSubscriptionBuilder<T> bindEvent(Class<E> eventClass, Function<E, T> function) {
        return bindEvent(eventClass, EventPriority.NORMAL, function);
    }

    @Override
    public <E> SimpleSubscriptionBuilder<T> bindEvent(Class<E> eventClass, EventPriority priority, Function<E, T> function) {
        Objects.requireNonNull(eventClass, "eventClass");
        Objects.requireNonNull(priority, "priority");
        Objects.requireNonNull(function, "function");

        this.mappings.put(eventClass, new SimpleHandlerMapping<>(priority, function));
        return this;
    }

    public SimpleSubscriptionBuilder<T> expireIf(BiPredicate<SimpleSubscription<T>, T> predicate, ExpiryTestStage... testPoints) {
        Objects.requireNonNull(testPoints, "testPoints");
        Objects.requireNonNull(predicate, "predicate");
        for (ExpiryTestStage testPoint : testPoints) {
            switch (testPoint) {
                case PRE:
                    this.preExpiryTests.add(predicate);
                    break;
                case POST_FILTER:
                    this.midExpiryTests.add(predicate);
                    break;
                case POST_HANDLE:
                    this.postExpiryTests.add(predicate);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown ExpiryTestPoint: " + testPoint);
            }
        }
        return this;
    }

    @Override
    public SimpleSubscriptionBuilder<T> filter(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate");
        this.filters.add(predicate);
        return this;
    }

    @Override
    public SimpleSubscriptionBuilder<T> exceptionConsumer(BiConsumer<? super T, Throwable> exceptionConsumer) {
        Objects.requireNonNull(exceptionConsumer, "exceptionConsumer");
        this.exceptionConsumer = exceptionConsumer;
        return this;
    }

    public SimpleHandlerList<T> handlers() {
        if (this.mappings.isEmpty()) {
            throw new IllegalStateException("No mappings were created");
        }

        return new SimpleHandlerList<>(this);
    }

}
