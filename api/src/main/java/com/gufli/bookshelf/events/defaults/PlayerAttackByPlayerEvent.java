package com.gufli.bookshelf.events.defaults;

import com.gufli.bookshelf.entity.PlatformPlayer;

public class PlayerAttackByPlayerEvent extends AbstractPlayerEvent implements Cancellable {

    private final PlatformPlayer attacker;
    private boolean cancelled = false;

    public PlayerAttackByPlayerEvent(PlatformPlayer player, PlatformPlayer attacker) {
        super(player);
        this.attacker = attacker;
    }

    public PlatformPlayer getAttacker() {
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
