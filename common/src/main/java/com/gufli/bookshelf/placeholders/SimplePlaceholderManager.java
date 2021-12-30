package com.gufli.bookshelf.placeholders;

import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.location.ShelfLocation;
import com.gufli.bookshelf.api.placeholders.PlaceholderManager;
import com.gufli.bookshelf.api.placeholders.Placeholders;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimplePlaceholderManager implements PlaceholderManager {

    private final Map<String, BiFunction<ShelfPlayer, String, String>> replacers = new HashMap<>();

    public SimplePlaceholderManager() {
        Placeholders.register(this);

        registerReplacer("username", ShelfPlayer::name);

        registerReplacer("displayname", ShelfPlayer::displayName);
        registerReplacer("player", ShelfPlayer::displayName);

        registerReplacer("location", p -> {
            ShelfLocation loc = p.location();
            return (int) loc.x() + ", " + (int) loc.y() + ", " + (int) loc.z();
        });
    }

    @Override
    public String replace(ShelfPlayer player, String text) {
        if ( text == null ) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        Pattern p = Pattern.compile("(\\{[^}]+})");
        Matcher m = p.matcher(text);

        while ( m.find() ) {
            String placeholder = m.group(1).toLowerCase();
            placeholder = placeholder.substring(1, placeholder.length()-1); // remove brackets { and }

            if ( !replacers.containsKey(placeholder) ) {
                continue;
            }

            String replacement = replacers.get(placeholder).apply(player, placeholder);
            if ( replacement == null ) {
                replacement = "";
            }

            m.appendReplacement(sb, replacement);
        }
        m.appendTail(sb);

        return sb.toString();
    }

    @Override
    public Set<String> placeholders() {
        return Collections.unmodifiableSet(replacers.keySet());
    }

    @Override
    public void registerReplacer(String key, BiFunction<ShelfPlayer, String, String> replacer) {
        replacers.put(key.toLowerCase(), replacer);
    }

    @Override
    public void registerReplacer(String key, Function<ShelfPlayer, String> replacer) {
        replacers.put(key.toLowerCase(), (p, s) -> replacer.apply(p));
    }

    @Override
    public void unregisterReplacer(String key) {
        replacers.remove(key.toLowerCase());
    }
}
