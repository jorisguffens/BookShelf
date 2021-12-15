package com.gufli.bookshelf.animation;

import com.gufli.bookshelf.api.scheduler.SchedulerTask;
import com.gufli.bookshelf.api.server.Bookshelf;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

public class AnimationBuilder {

    private final Animation animation = new Animation();

    public static AnimationBuilder get() {
        return new AnimationBuilder();
    }

    public Animation build() {
        return animation.copy();
    }

    private long nanos(long value, TemporalUnit temporalUnit) {
        long duration = temporalUnit.getDuration().getNano() + temporalUnit.getDuration().getSeconds() * 1000_000_000;
        return value * duration;
    }

    //

    public AnimationBuilder wait(long value, TemporalUnit temporalUnit) {
        animation.addStep(() -> {
            CompletableFuture<?> cf = new CompletableFuture<>();
            Bookshelf.getScheduler().syncLater(() -> cf.complete(null),
                    nanos(value,temporalUnit), TimeUnit.NANOSECONDS);
            return cf;
        });
        return this;
    }

    public AnimationBuilder waitUntil(Instant instant) {
        animation.addStep(() -> {
            CompletableFuture<?> cf = new CompletableFuture<>();
            Bookshelf.getScheduler().syncLater(() -> cf.complete(null),
                    Instant.now().getEpochSecond() - instant.getEpochSecond(), TimeUnit.SECONDS);
            return cf;
        });
        return this;
    }

    private AnimationBuilder waitUntil(Function<Animation, Boolean> test, long value, TemporalUnit temporalUnit) {
        animation.addStep((ac) -> {
            CompletableFuture<?> cf = new CompletableFuture<>();
            new Runnable() {
                SchedulerTask task;

                @Override
                public void run() {
                    if (cf.isCancelled()) {
                        return;
                    }
                    if (test.apply(ac)) {
                        cf.complete(null);
                        return;
                    }
                    task = Bookshelf.getScheduler().syncLater(this,
                            nanos(value, temporalUnit), TimeUnit.NANOSECONDS);
                }
            }.run();
            return cf;
        });
        return this;
    }

    public AnimationBuilder waitUntil(Supplier<Boolean> test, long value, TemporalUnit temporalUnit) {
        waitUntil((ignored) -> test.get(), value, temporalUnit);
        return this;
    }

    //

    public AnimationBuilder execute(Runnable runnable) {
        animation.addStep(() -> {
            runnable.run();
            return CompletableFuture.completedFuture(null);
        });
        return this;
    }

    public AnimationBuilder executeAsync(Runnable runnable) {
        animation.addStep(() -> Bookshelf.getScheduler().runAsync(runnable));
        return this;
    }

    //

    public AnimationBuilder executeIf(Supplier<Boolean> test, Runnable runnable) {
        animation.addStep(() -> {
            if (test.get()) {
                runnable.run();
            }
            return CompletableFuture.completedFuture(null);
        });
        return this;
    }

    public AnimationBuilder executeAsyncIf(Supplier<Boolean> test, Runnable runnable) {
        animation.addStep(() -> Bookshelf.getScheduler().runAsync(() -> {
            if (test.get()) {
                runnable.run();
            }
        }));
        return this;
    }

    //

    /**
     * Repeat until test complets, execute and checks every given time duration
     */
    public AnimationBuilder repeatUntil(Runnable runnable, Supplier<Boolean> test, long value, TemporalUnit temporalUnit) {
        waitUntil(() -> {
            boolean val = test.get();
            if (!val) {
                runnable.run();
            }
            return val;
        }, value, temporalUnit);
        return this;
    }

    /**
     * Repeat until test complets, execute and checks every given time duration
     */
    public AnimationBuilder repeatUntil(Supplier<Boolean> runnableAndTest, long value, TemporalUnit temporalUnit) {
        waitUntil(runnableAndTest, value, temporalUnit);
        return this;
    }

    /**
     * Repeat until time is reached, execute and checks every given time duration
     */
    public AnimationBuilder repeatUntil(Runnable runnable, Instant instant, long value, TemporalUnit temporalUnit) {
        waitUntil(() -> {
            boolean val = Instant.now().isAfter(instant);
            if (!val) {
                runnable.run();
            }
            return val;
        }, value, temporalUnit);
        return this;
    }

    //

    public AnimationBuilder cancelIf(Supplier<Boolean> test) {
        animation.cancelIf(test);
        return this;
    }

    public AnimationBuilder cancelAfter(long value, TemporalUnit temporalUnit) {
        animation.cancelIf((ac) -> Instant.now().isAfter(ac.startedAt().plus(value, temporalUnit)));
        return this;
    }

}
