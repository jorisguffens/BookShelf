package com.gufli.bookshelf.events.defaults;

public interface Cancellable {

    boolean isCancelled();

    void setCancelled(boolean cancelled);

}
