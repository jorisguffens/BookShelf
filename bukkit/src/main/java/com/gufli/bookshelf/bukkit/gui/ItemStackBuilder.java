package com.gufli.bookshelf.bukkit.gui;

import com.gufli.bookshelf.bukkit.reflection.Reflection;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

public class ItemStackBuilder {

    private final ItemStack itemStack;

    public static ItemStackBuilder of(Material material) {
        return new ItemStackBuilder(new ItemStack(material)).hideAttributes();
    }

    public static ItemStackBuilder of(ItemStack itemStack) {
        return new ItemStackBuilder(itemStack).hideAttributes();
    }

    private ItemStackBuilder(ItemStack itemStack) {
        this.itemStack = Objects.requireNonNull(itemStack, "itemStack");
    }

    public ItemStack build() {
        return this.itemStack;
    }

    public ItemStackBuilder transform(Consumer<ItemStack> is) {
        is.accept(this.itemStack);
        return this;
    }

    public ItemStackBuilder transformMeta(Consumer<ItemMeta> meta) {
        return transformMeta(ItemMeta.class, meta);
    }

    public <T extends ItemMeta> ItemStackBuilder transformMeta(Class<T> type, Consumer<T> meta) {
        T m = (T) this.itemStack.getItemMeta();
        if (m != null) {
            meta.accept(m);
            this.itemStack.setItemMeta(m);
        }
        return this;
    }

    public ItemStackBuilder withName(String name) {
        return transformMeta(meta -> meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name)));
    }

    public ItemStackBuilder withType(Material material) {
        return transform(itemStack -> itemStack.setType(material));
    }

    public ItemStackBuilder withLore(String... lines) {
        return withLore(Arrays.asList(lines));
    }

    public ItemStackBuilder withLore(Iterable<String> lines) {
        return transformMeta(meta -> {
            List<String> lore = meta.getLore() == null ? new ArrayList<>() : meta.getLore();
            for (String line : lines) {
                lore.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            meta.setLore(lore);
        });
    }

    public ItemStackBuilder clearLore() {
        return transformMeta(meta -> meta.setLore(new ArrayList<>()));
    }

    public ItemStackBuilder withDurability(int durability) {
        return transform(itemStack -> itemStack.setDurability((short) durability));
    }

    public ItemStackBuilder withData(int data) {
        return withDurability(data);
    }

    public ItemStackBuilder withAmount(int amount) {
        return transform(itemStack -> itemStack.setAmount(amount));
    }

    public ItemStackBuilder withEnchantment(Enchantment enchantment, int level) {
        return transform(itemStack -> itemStack.addUnsafeEnchantment(enchantment, level));
    }

    public ItemStackBuilder withEnchantment(Enchantment enchantment) {
        return transform(itemStack -> itemStack.addUnsafeEnchantment(enchantment, 1));
    }

    public ItemStackBuilder clearEnchantments() {
        return transform(itemStack -> itemStack.getEnchantments().keySet().forEach(itemStack::removeEnchantment));
    }

    public ItemStackBuilder withItemFlag(ItemFlag... flags) {
        return transformMeta(meta -> meta.addItemFlags(flags));
    }

    public ItemStackBuilder withoutItemFlag(ItemFlag... flags) {
        return transformMeta(meta -> meta.removeItemFlags(flags));
    }

    public ItemStackBuilder hideAttributes() {
        return withItemFlag(ItemFlag.values());
    }

    public ItemStackBuilder showAttributes() {
        return withoutItemFlag(ItemFlag.values());
    }

    public ItemStackBuilder withColor(Color color) {
        Material type = itemStack.getType();
        if (type != Material.LEATHER_BOOTS && type != Material.LEATHER_CHESTPLATE
                && type != Material.LEATHER_HELMET && type != Material.LEATHER_LEGGINGS) {
            return this;
        }

        return transformMeta(LeatherArmorMeta.class, meta -> {
            meta.setColor(color);
        });
    }

    public ItemStackBuilder setBreakable(boolean flag) {
        return transformMeta(meta -> meta.setUnbreakable(!flag));
    }

    public ItemStackBuilder apply(Consumer<ItemStackBuilder> consumer) {
        consumer.accept(this);
        return this;
    }

    public ItemStackBuilder setPlayer(OfflinePlayer owner) {
        return transformMeta(SkullMeta.class, meta -> {
            meta.setOwningPlayer(owner);
        });
    }

    // SKULLS

    private static Class<?> GameProfile;
    private static Method GameProfile_getProperties;
    private static Class<?> Property;
    private static Method Property_getName;
    private static Method PropertyMap_put;
    private static Field CraftSkullMeta_profile;

    static {
        try {
            GameProfile = Class.forName("com.mojang.authlib.GameProfile");
            GameProfile_getProperties = GameProfile.getDeclaredMethod("getProperties");

            Property = Class.forName("com.mojang.authlib.properties.Property");
            Property_getName = Property.getMethod("getName");

            Class<?> PropertyMap = Class.forName("com.mojang.authlib.properties.PropertyMap");
            PropertyMap_put = PropertyMap.getMethod("put", Object.class, Object.class);

            Class<?> CraftSkullMeta = Reflection.PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftSkullMeta");
            CraftSkullMeta_profile = CraftSkullMeta.getDeclaredField("profile");
            CraftSkullMeta_profile.setAccessible(true);
        } catch (NoSuchMethodException | ClassNotFoundException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public ItemStackBuilder withTexture(String texture) {
        try {
            UUID uuid = UUID.randomUUID();

            Object profile = GameProfile.getDeclaredConstructor(UUID.class, String.class).newInstance(uuid, uuid.toString().substring(0, 15));
            Object property = Property.getDeclaredConstructor(String.class, String.class).newInstance("textures", texture);
            Object properties = GameProfile_getProperties.invoke(profile);
            PropertyMap_put.invoke(properties, Property_getName.invoke(property), property);
            return withProfile(profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return this;
    }

    public ItemStackBuilder withTexture(UUID uuid) {
        try {
            Object profile = GameProfile.getDeclaredConstructor(UUID.class, String.class).newInstance(uuid, uuid.toString().substring(0, 15));
            return withProfile(profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return this;
    }

    private ItemStackBuilder withProfile(Object profile) {
        try {
            SkullMeta meta = (SkullMeta) this.itemStack.getItemMeta();
            CraftSkullMeta_profile.set(meta, profile);
            this.itemStack.setItemMeta(meta);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;
    }


}
