package com.gufli.bookshelf.cooldowns;

import com.gufli.bookshelf.api.cooldown.CooldownManager;
import com.gufli.bookshelf.api.entity.ShelfPlayer;

import java.time.Instant;
import java.time.temporal.TemporalUnit;

public class SimpleCooldownManager implements CooldownManager {

    private final static String KEY_PREFIX = "COOLDOWNS_";

    private String adapt(String key) {
        return KEY_PREFIX + key;
    }

    @Override
    public boolean has(ShelfPlayer player, String key) {
        if ( !player.has(adapt(key)) ) {
            return false;
        }
        Instant expireAt = player.get(adapt(key), Instant.class);
        return expireAt.isAfter(Instant.now());
    }

    @Override
    public void give(ShelfPlayer player, String key, long value, TemporalUnit timeUnit) {
        player.set(adapt(key), Instant.now().plus(value, timeUnit));
    }

    @Override
    public void remove(ShelfPlayer player, String key) {
        player.remove(adapt(key));
    }

    @Override
    public long remaining(ShelfPlayer player, String key, TemporalUnit timeUnit) {
        if ( !player.has(adapt(key)) ) {
            return 0;
        }
        Instant expireAt = player.get(adapt(key), Instant.class);
        long diff = expireAt.getNano() - Instant.now().getNano();
        if ( diff < 0 ) {
            return 0;
        }

        return (int) (diff / timeUnit.getDuration().getNano());
    }
}
