package com.gufli.bookshelf.events.defaults;

import com.gufli.bookshelf.entity.ShelfPlayer;

public class PlayerAttackByPlayerEvent extends AbstractPlayerEvent implements Cancellable {

    private final ShelfPlayer attacker;
    private boolean cancelled = false;

    public PlayerAttackByPlayerEvent(ShelfPlayer player, ShelfPlayer attacker) {
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
