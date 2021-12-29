package com.gufli.bookshelf.api.placeholders;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface PlaceholderManager {

    String replace(ShelfPlayer player, String text);

    Set<String> placeholders();

    void registerReplacer(String key, BiFunction<ShelfPlayer, String, String> replacer);

    void registerReplacer(String key, Function<ShelfPlayer, String> replacer);

    void unregisterReplacer(String key);

}
