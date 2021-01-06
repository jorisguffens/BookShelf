package com.gufli.bookshelf.events.defaults;

import com.gufli.bookshelf.entity.ShelfPlayer;

public class PlayerDeathEvent extends AbstractPlayerEvent {

    private final ShelfPlayer killer;
    private String deathMessage;

    public PlayerDeathEvent(ShelfPlayer player, ShelfPlayer killer, String deathMessage) {
        super(player);
        this.killer = killer;
        this.deathMessage = deathMessage;
    }

    public PlayerDeathEvent(ShelfPlayer player, String deathMessage) {
        this(player, null, deathMessage);
    }

    public ShelfPlayer getKiller() {
        return killer;
    }

    public String getDeathMessage() {
        return deathMessage;
    }

    public void setDeathMessage(String deathMessage) {
        this.deathMessage = deathMessage;
    }
}
