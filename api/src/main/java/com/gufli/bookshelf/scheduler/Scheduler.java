package com.gufli.bookshelf.scheduler;

import java.util.concurrent.*;

public interface Scheduler {

    Executor sync();

    Executor async();

    default void executeAsync(Runnable task) {
        async().execute(task);
    }

    default void executeSync(Runnable task) {
        sync().execute(task);
    }

    SchedulerTask asyncLater(Runnable task, long delay, TimeUnit unit);

    SchedulerTask asyncRepeating(Runnable task, long interval, TimeUnit unit);

    SchedulerTask syncLater(Runnable task, long delay, TimeUnit unit);

    SchedulerTask syncRepeating(Runnable task, long interval, TimeUnit unit);

    default <T> CompletableFuture<T> makeAsyncFuture(Callable<T> supplier) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return supplier.call();
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }, async());
    }

    default CompletableFuture<Void> makeAsyncFuture(Runnable runnable) {
        return CompletableFuture.runAsync(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }, async());
    }

}
