package com.gufli.bookshelf.api.events;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.location.ShelfLocation;

public class PlayerMoveEvent extends AbstractPlayerEvent implements Cancellable {

    private final ShelfLocation from;
    private final ShelfLocation to;

    private boolean cancelled;

    public PlayerMoveEvent(ShelfPlayer player, ShelfLocation from, ShelfLocation to) {
        super(player);
        this.from = from;
        this.to = to;
    }

    public ShelfLocation from() {
        return from;
    }

    public ShelfLocation to() {
        return to;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
