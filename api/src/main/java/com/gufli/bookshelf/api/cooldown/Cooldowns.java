package com.gufli.bookshelf.api.cooldown;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public class Cooldowns {
    
    private Cooldowns() {}

    private static CooldownManager cooldownManager;

    public static void register(CooldownManager manager) {
        if (cooldownManager != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton cooldownManager.");
        }

        cooldownManager = manager;
    }

    public static boolean has(ShelfPlayer player, String key) {
        return cooldownManager.has(player, key);
    }

    public static void give(ShelfPlayer player, String key, int value, TemporalUnit timeUnit) {
        cooldownManager.give(player, key, value, timeUnit);
    }

    public static void giveSeconds(ShelfPlayer player, String key, float value) {
        int millis = (int) (value * 1000);
        cooldownManager.give(player, key, millis, ChronoUnit.SECONDS);
    }

    public static void remove(ShelfPlayer player, String key) {
        cooldownManager.remove(player, key);
    }

    public static int remaining(ShelfPlayer player, String key, TemporalUnit timeUnit) {
        return remaining(player, key, timeUnit);
    }

    public static float remainingSeconds(ShelfPlayer player, String key) {
        int millies = remaining(player, key, ChronoUnit.MILLIS);
        return millies / 1000f;
    }
    
}
