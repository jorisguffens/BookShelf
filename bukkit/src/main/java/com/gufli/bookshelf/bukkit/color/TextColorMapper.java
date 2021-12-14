package com.gufli.bookshelf.bukkit.color;

import com.gufli.bookshelf.api.color.TextColor;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class TextColorMapper {

    private final static Map<TextColor, String> mapper = new HashMap<>();
    static {
        mapper.put(TextColor.BLACK, ChatColor.BLACK.toString());
        mapper.put(TextColor.DARK_BLUE, ChatColor.DARK_BLUE.toString());
        mapper.put(TextColor.DARK_GREEN, ChatColor.DARK_GREEN.toString());
        mapper.put(TextColor.DARK_AQUA, ChatColor.DARK_AQUA.toString());
        mapper.put(TextColor.DARK_RED, ChatColor.DARK_RED.toString());
        mapper.put(TextColor.DARK_PURPLE, ChatColor.DARK_PURPLE.toString());
        mapper.put(TextColor.GOLD, ChatColor.GOLD.toString());
        mapper.put(TextColor.GRAY, ChatColor.GRAY.toString());
        mapper.put(TextColor.DARK_GRAY, ChatColor.DARK_GRAY.toString());
        mapper.put(TextColor.BLUE, ChatColor.BLUE.toString());
        mapper.put(TextColor.GREEN, ChatColor.GREEN.toString());
        mapper.put(TextColor.AQUA, ChatColor.AQUA.toString());
        mapper.put(TextColor.RED, ChatColor.RED.toString());
        mapper.put(TextColor.LIGHT_PURPLE, ChatColor.LIGHT_PURPLE.toString());
        mapper.put(TextColor.YELLOW, ChatColor.YELLOW.toString());
        mapper.put(TextColor.WHITE, ChatColor.WHITE.toString());
    }

    public TextColorMapper() {
        TextColor.register(mapper::get);
    }

}
