package com.gufli.bookshelf.api.cooldowns;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public interface CooldownManager {

    boolean has(ShelfPlayer player, String key);

    void give(ShelfPlayer player, String key, long value, TemporalUnit timeUnit);

    void remove(ShelfPlayer player, String key);

    long remaining(ShelfPlayer player, String key, TemporalUnit timeUnit);

}
