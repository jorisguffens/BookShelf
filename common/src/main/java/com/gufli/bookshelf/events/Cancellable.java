package com.gufli.bookshelf.events;

public interface Cancellable {

    boolean isCancelled();

    void setCancelled(boolean cancelled);

}
