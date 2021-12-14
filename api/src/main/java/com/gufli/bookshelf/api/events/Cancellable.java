package com.gufli.bookshelf.api.events;

public interface Cancellable {

    boolean isCancelled();

    void setCancelled(boolean cancelled);

}
