package com.gufli.bookshelf.animation;

import com.gufli.bookshelf.api.scheduler.SchedulerTask;
import com.gufli.bookshelf.api.server.Bookshelf;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

public class Animation {

    private final List<Function<Animation, CompletableFuture<?>>> steps = new ArrayList<>();
    private final Set<Function<Animation, Boolean>> cancellators = new HashSet<>();

    void addStep(Supplier<CompletableFuture<?>> supplier) {
        steps.add((ignored) -> supplier.get());
    }

    void addStep(Function<Animation, CompletableFuture<?>> supplier) {
        steps.add(supplier);
    }

    void cancelIf(Supplier<Boolean> test) {
        cancellators.add((ignored) -> test.get());
    }

    void cancelIf(Function<Animation, Boolean> test) {
        cancellators.add(test);
    }

    //

    Animation copy() {
        Animation clone = new Animation();
        clone.steps.addAll(this.steps);
        clone.cancellators.addAll(this.cancellators);
        return clone;
    }

    //

    private Instant startedAt;
    private boolean finished = false;
    private SchedulerTask cancelTask;
    private CompletableFuture<?> cf;

    public void start() {
        this.startedAt = Instant.now();
        Animation clone = this.copy();
        clone.next();

        cancelTask = Bookshelf.getScheduler().syncRepeating(() -> {
            if ( shouldCancel() ) {
                finish();
            }
        }, 1, TimeUnit.SECONDS);
    }

    private void finish() {
        this.finished = true;

        if ( cf != null ) {
            cf.cancel(true);
        }

        cancelTask.cancel();
    }

    private void next() {
        if ( finished ) {
            return;
        }

        if ( steps.isEmpty() ) {
            finish();
            return;
        }

        cf = steps.remove(0).apply(this);
        cf.thenRun(this::next);
    }

    private boolean shouldCancel() {
        for ( Function<Animation, Boolean> test : cancellators ) {
            if ( test.apply(this) ) {
                return true;
            }
        }
        return false;
    }

    //

    boolean isFinished() {
        return finished;
    }

    Instant startedAt() {
        return startedAt;
    }

}
