package com.gufli.bookshelf.scheduler;

import java.util.concurrent.*;
import java.util.function.Supplier;

public interface Scheduler {

    Executor sync();

    Executor async();

    //

    default void async(Runnable task) {
        async().execute(task);
    }

    default void sync(Runnable task) {
        sync().execute(task);
    }

    //

    SchedulerTask asyncLater(Runnable task, long delay, TimeUnit unit);

    SchedulerTask asyncRepeating(Runnable task, long interval, TimeUnit unit);

    //

    SchedulerTask syncLater(Runnable task, long delay, TimeUnit unit);

    SchedulerTask syncRepeating(Runnable task, long interval, TimeUnit unit);

    //

    default <T> CompletableFuture<T> supply(Supplier<T> supplier) {
        CompletableFuture<T> future = new CompletableFuture<>();
        async(() -> future.complete(supplier.get()));
        return future;
    }

    default CompletableFuture<Void> run(Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        async(() -> {
            runnable.run();
            future.complete(null);
        });
        return future;
    }

}
