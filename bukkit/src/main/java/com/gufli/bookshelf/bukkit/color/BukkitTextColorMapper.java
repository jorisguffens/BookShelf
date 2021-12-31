package com.gufli.bookshelf.bukkit.color;

import com.gufli.bookshelf.api.color.TextColor;
import com.gufli.bookshelf.api.color.TextColorMapper;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class BukkitTextColorMapper implements TextColorMapper {

    private final static Map<TextColor, ChatColor> mapper = new HashMap<>();
    static {
        for ( TextColor value : TextColor.values() ) {
            mapper.put(value, ChatColor.valueOf(value.name()));
        }
    }

    public BukkitTextColorMapper() {
        TextColor.register(this);
    }

    @Override
    public String map(TextColor color) {
        return mapper.get(color).toString();
    }

    @Override
    public String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    @Override
    public String strip(String string) {
        return ChatColor.stripColor(translate(string));
    }
}
