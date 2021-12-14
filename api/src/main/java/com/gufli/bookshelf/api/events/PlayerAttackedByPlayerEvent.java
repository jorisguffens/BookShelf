package com.gufli.bookshelf.api.events;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

public class PlayerAttackedByPlayerEvent extends AbstractPlayerEvent implements Cancellable {

    private final ShelfPlayer attacker;
    private boolean cancelled = false;

    public PlayerAttackedByPlayerEvent(ShelfPlayer player, ShelfPlayer attacker) {
        super(player);
        this.attacker = attacker;
    }

    public ShelfPlayer getAttacker() {
        return attacker;
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
