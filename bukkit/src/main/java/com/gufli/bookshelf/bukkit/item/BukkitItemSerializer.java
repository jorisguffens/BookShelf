package com.gufli.bookshelf.bukkit.item;

import com.gufli.bookshelf.api.item.Item;
import com.gufli.bookshelf.api.item.ItemSerializer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class BukkitItemSerializer implements Item.Serializer {

    public BukkitItemSerializer() {
        ItemSerializer.register(this);
    }

    @Override
    public Item deserialize(String value) {
        ItemStack is = stringToItem(value);
        if ( is == null ) {
            return null;
        }
        return new BukkitItem(is);
    }

    @Override
    public String serialize(Item item) {
        if ( item == null || item.handle() == null ) {
            return null;
        }
        return itemToString((ItemStack) item.handle());
    }

    // I know its dirty, just leave me alone!
    public static String itemToString(ItemStack itemStack) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("item", itemStack);
        return config.saveToString();
    }

    public static ItemStack stringToItem(String string) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(string);
        } catch (Exception ignored) {
            return null;
        }
        return config.getItemStack("item", null);
    }

}