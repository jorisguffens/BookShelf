package com.gufli.bookshelf.api.events;

public class AbstractEvent implements Event {

    private final boolean async;

    public AbstractEvent(boolean async) {
        this.async = async;
    }

    public AbstractEvent() {
        this(false);
    }

    public final boolean isAsync() {
        return async;
    }
}
