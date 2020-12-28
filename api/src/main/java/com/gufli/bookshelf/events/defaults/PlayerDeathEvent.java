package com.gufli.bookshelf.events.defaults;

import com.gufli.bookshelf.entity.PlatformPlayer;

public class PlayerDeathEvent extends AbstractPlayerEvent {

    private final PlatformPlayer killer;
    private String deathMessage;

    public PlayerDeathEvent(PlatformPlayer player, PlatformPlayer killer, String deathMessage) {
        super(player);
        this.killer = killer;
        this.deathMessage = deathMessage;
    }

    public PlayerDeathEvent(PlatformPlayer player, String deathMessage) {
        this(player, null, deathMessage);
    }

    public PlatformPlayer getKiller() {
        return killer;
    }

    public String getDeathMessage() {
        return deathMessage;
    }

    public void setDeathMessage(String deathMessage) {
        this.deathMessage = deathMessage;
    }
}
