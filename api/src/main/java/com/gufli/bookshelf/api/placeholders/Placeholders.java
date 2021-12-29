package com.gufli.bookshelf.api.placeholders;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Placeholders {

    private static PlaceholderManager placeholderManager;

    public static void register(PlaceholderManager manager) {
        if (placeholderManager != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton placeholderManager.");
        }

        placeholderManager = manager;
    }

    public static String replace(ShelfPlayer player, String text) {
        return placeholderManager.replace(player, text);
    }

    public static Set<String> placeholders() {
        return placeholderManager.placeholders();
    }

    public static void registerReplacer(String key, BiFunction<ShelfPlayer, String, String> replacer) {
        placeholderManager.registerReplacer(key, replacer);
    }

    public static void registerReplacer(String key, Function<ShelfPlayer, String> replacer) {
        placeholderManager.registerReplacer(key, replacer);
    }

    public static void unregisterReplacer(String key) {
        placeholderManager.unregisterReplacer(key);
    }

}
