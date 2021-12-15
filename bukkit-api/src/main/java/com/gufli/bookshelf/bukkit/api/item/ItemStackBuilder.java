package com.gufli.bookshelf.bukkit.api.item;

import com.gufli.bookshelf.bukkit.api.reflection.Reflection;
import org.bukkit.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

public class ItemStackBuilder {

    public enum ItemColor {
        WHITE(0),
        ORANGE(1),
        MAGENTA(2),
        LIGHT_BLUE(3),
        YELLOW(4),
        LIME(5),
        PINK(6),
        GRAY(7),
        LIGHT_GRAY(8),
        CYAN(9),
        PURPLE(10),
        BLUE(11),
        BROWN(12),
        GREEN(13),
        RED(14),
        BLACK(15);

        public final int data;

        ItemColor(int data) {
            this.data = data;
        }
    }

    private final ItemStack itemStack;

    public static ItemStackBuilder of(Material material) {
        return new ItemStackBuilder(new ItemStack(material)).hideAttributes();
    }

    public static ItemStackBuilder of(ItemStack itemStack) {
        return new ItemStackBuilder(itemStack).hideAttributes();
    }

    private static ItemStackBuilder of(String material) {
        return ItemStackBuilder.of(Material.valueOf(material));
    }

    //

    public static ItemStackBuilder skull() {
        return ItemStackBuilder.of(Material.PLAYER_HEAD);
    }

    public static ItemStackBuilder wool(ItemColor color) {
        return ItemStackBuilder.of(color.name() + "_WOOL");
    }

    public static ItemStackBuilder terracotta(ItemColor color) {
        return ItemStackBuilder.of(color.name() + "_TERRACOTTA");
    }

    public static ItemStackBuilder glass(ItemColor color) {
        return ItemStackBuilder.of(color.name() + "_STAINED_GLASS");
    }

    public static ItemStackBuilder bed(ItemColor color) {
        return ItemStackBuilder.of(color.name() + "_BED");
    }

    public static ItemStackBuilder carpet(ItemColor color) {
        return ItemStackBuilder.of(color.name() + "_CARPET");
    }

    public static ItemStackBuilder dye(ItemColor color) {
        return ItemStackBuilder.of(color.name() + "_DYE");
    }

    public static ItemStackBuilder banner(ItemColor color) {
        return ItemStackBuilder.of(color.name() + "_BANNER");
    }

    //

    private ItemStackBuilder(ItemStack itemStack) {
        this.itemStack = Objects.requireNonNull(itemStack, "itemStack");
    }

    public ItemStack build() {
        return this.itemStack;
    }

    // Modifiers

    public ItemStackBuilder transform(Consumer<ItemStack> is) {
        is.accept(this.itemStack);
        return this;
    }

    public ItemStackBuilder apply(Consumer<ItemStackBuilder> consumer) {
        consumer.accept(this);
        return this;
    }

    public ItemStackBuilder apply(boolean condition, Consumer<ItemStackBuilder> consumer) {
        if ( condition ) {
            consumer.accept(this);
        }
        return this;
    }

    public ItemStackBuilder apply(boolean condition, Consumer<ItemStackBuilder> consumer, Consumer<ItemStackBuilder> elseConsumer) {
        if ( condition ) {
            consumer.accept(this);
        } else {
            elseConsumer.accept(this);
        }
        return this;
    }

    // Basic

    public ItemStackBuilder applyMeta(Consumer<ItemMeta> meta) {
        return applyMeta(ItemMeta.class, meta);
    }

    public <T> ItemStackBuilder applyMeta(Class<T> type, Consumer<T> meta) {
        T m = (T) this.itemStack.getItemMeta();
        if (m != null) {
            meta.accept(m);
            this.itemStack.setItemMeta((ItemMeta) m);
        }
        return this;
    }

    public ItemStackBuilder withName(String name) {
        return applyMeta(meta -> meta.setDisplayName(name));
    }

    public ItemStackBuilder withType(Material material) {
        return transform(itemStack -> itemStack.setType(material));
    }

    public ItemStackBuilder withLore(String... lines) {
        return withLore(Arrays.asList(lines));
    }

    public ItemStackBuilder withLore(Iterable<String> lines) {
        return applyMeta(meta -> {
            List<String> lore = meta.getLore() == null ? new ArrayList<>() : meta.getLore();
            for (String line : lines) {
                lore.add(ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', line));
            }
            meta.setLore(lore);
        });
    }

    public ItemStackBuilder clearLore() {
        return applyMeta(meta -> meta.setLore(new ArrayList<>()));
    }

    public ItemStackBuilder withToolDamage(int damage) {
        return applyMeta(Damageable.class, meta -> meta.setDamage(damage));
    }

    public ItemStackBuilder withAmount(int amount) {
        return transform(itemStack -> itemStack.setAmount(amount));
    }

    // Enchantments

    public ItemStackBuilder withEnchantment(Enchantment enchantment, int level) {
        return transform(itemStack -> itemStack.addUnsafeEnchantment(enchantment, level));
    }

    public ItemStackBuilder withEnchantment(Enchantment enchantment) {
        return transform(itemStack -> itemStack.addUnsafeEnchantment(enchantment, 1));
    }

    public ItemStackBuilder clearEnchantments() {
        return transform(itemStack -> itemStack.getEnchantments().keySet().forEach(itemStack::removeEnchantment));
    }

    // ItemFlags & Attributes

    public ItemStackBuilder withItemFlag(ItemFlag... flags) {
        return applyMeta(meta -> meta.addItemFlags(flags));
    }

    public ItemStackBuilder withoutItemFlag(ItemFlag... flags) {
        return applyMeta(meta -> meta.removeItemFlags(flags));
    }

    public ItemStackBuilder hideAttributes() {
        return withItemFlag(ItemFlag.values());
    }

    public ItemStackBuilder showAttributes() {
        return withoutItemFlag(ItemFlag.values());
    }

    // SKULLS

    private static Class<?> GameProfile;
    private static Method GameProfile_getProperties;
    private static Class<?> Property;
    private static Method Property_getName;
    private static Method PropertyMap_put;
    private static Field CraftMetaSkull_profile;
    private static Method CraftMetaSkull_setOwningPlayer;

    static {

        try {
            GameProfile = Class.forName("com.mojang.authlib.GameProfile");
            GameProfile_getProperties = GameProfile.getDeclaredMethod("getProperties");

            Property = Class.forName("com.mojang.authlib.properties.Property");
            Property_getName = Property.getMethod("getName");

            Class<?> PropertyMap = Class.forName("com.mojang.authlib.properties.PropertyMap");
            PropertyMap_put = PropertyMap.getMethod("put", Object.class, Object.class);

            Class<?> CraftMetaSkull = Reflection.PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftMetaSkull");
            CraftMetaSkull_profile = CraftMetaSkull.getDeclaredField("profile");
            CraftMetaSkull_profile.setAccessible(true);

            CraftMetaSkull_setOwningPlayer = CraftMetaSkull.getMethod("setOwningPlayer", OfflinePlayer.class);

            // Optional
            try {
                CraftMetaSkull_setOwningPlayer = CraftMetaSkull.getMethod("setOwningPlayer", OfflinePlayer.class);
            } catch (NoSuchMethodException ignored) {}

        } catch (NoSuchMethodException | ClassNotFoundException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public ItemStackBuilder withSkullOwner(OfflinePlayer owner) {
        if ( CraftMetaSkull_setOwningPlayer == null ) {
            return this;
        }
        return applyMeta(SkullMeta.class, meta -> {
            try {
                CraftMetaSkull_setOwningPlayer.invoke(meta, owner);
            } catch (IllegalAccessException | InvocationTargetException ignored) {}
        });
    }

    public ItemStackBuilder withSkullTexture(String texture) {
        try {
            UUID uuid = UUID.randomUUID();

            Object profile = GameProfile.getDeclaredConstructor(UUID.class, String.class).newInstance(uuid, uuid.toString().substring(0, 15));
            Object property = Property.getDeclaredConstructor(String.class, String.class).newInstance("textures", texture);
            Object properties = GameProfile_getProperties.invoke(profile);
            PropertyMap_put.invoke(properties, Property_getName.invoke(property), property);
            return withSkullProfile(profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return this;
    }

    public ItemStackBuilder withSkullTexture(UUID uuid) {
        try {
            Object profile = GameProfile.getDeclaredConstructor(UUID.class, String.class).newInstance(uuid, uuid.toString().substring(0, 15));
            return withSkullProfile(profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return this;
    }

    private ItemStackBuilder withSkullProfile(Object profile) {
        try {
            SkullMeta meta = (SkullMeta) this.itemStack.getItemMeta();
            CraftMetaSkull_profile.set(meta, profile);
            this.itemStack.setItemMeta(meta);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;
    }

    // LEATHER ARMOR

    public ItemStackBuilder withArmorColor(Color color) {
        Material type = itemStack.getType();
        if (type != Material.LEATHER_BOOTS && type != Material.LEATHER_CHESTPLATE
                && type != Material.LEATHER_HELMET && type != Material.LEATHER_LEGGINGS) {
            return this;
        }

        return applyMeta(LeatherArmorMeta.class, meta -> {
            meta.setColor(color);
        });
    }

    public ItemStackBuilder withArmorColor(com.gufli.bookshelf.api.color.Color color) {
        return withArmorColor(Color.fromRGB(color.rgb()));
    }

    // BANNER

    public ItemStackBuilder withBannerPattern(Pattern pattern) {
        return applyMeta(BannerMeta.class, meta ->
                meta.addPattern(pattern));
    }

    public ItemStackBuilder withBannerPattern(DyeColor color, PatternType type) {
        return applyMeta(BannerMeta.class, meta ->
                meta.addPattern(new Pattern(color, type)));
    }

    public ItemStackBuilder withBannerPattern(int layer, Pattern pattern) {
        return applyMeta(BannerMeta.class, meta ->
                meta.setPattern(layer, pattern));
    }

    public ItemStackBuilder withBannerPattern(int layer, DyeColor color, PatternType type) {
        return applyMeta(BannerMeta.class, meta ->
                meta.setPattern(layer, new Pattern(color, type)));
    }

    public ItemStackBuilder withBannerPatterns(Pattern... patterns) {
        return withPatterns(Arrays.asList(patterns));
    }

    public ItemStackBuilder withPatterns(List<Pattern> patterns) {
        return applyMeta(BannerMeta.class, meta ->
                meta.setPatterns(patterns));
    }

    // BOOK

    public ItemStackBuilder withBookAuthor(String author) {
        return applyMeta(BookMeta.class, meta ->
                meta.setAuthor(author));
    }

    public ItemStackBuilder withBookTitle(String title) {
        return applyMeta(BookMeta.class, meta ->
                meta.setTitle(title));
    }

    public ItemStackBuilder withBookPage(String contents) {
        return applyMeta(BookMeta.class, meta ->
                meta.addPage(contents));
    }

    public ItemStackBuilder withBookPages(String... contents) {
        return applyMeta(BookMeta.class, meta ->
                meta.setPages(contents));
    }

    public ItemStackBuilder withBookPages(List<String> contents) {
        return applyMeta(BookMeta.class, meta ->
                meta.setPages(contents));
    }

    // FIREWORK

    public ItemStackBuilder withFireworkPower(int power) {
        return applyMeta(FireworkMeta.class, meta ->
                meta.setPower(power));
    }

    public ItemStackBuilder withFireworkEffect(FireworkEffect effect) {
        return applyMeta(FireworkMeta.class, meta ->
                meta.addEffect(effect));
    }

    public ItemStackBuilder withFireworkEffects(FireworkEffect... effects) {
        return withFireworkEffects(Arrays.asList(effects));
    }

    public ItemStackBuilder withFireworkEffects(List<FireworkEffect> effects) {
        return applyMeta(FireworkMeta.class, meta -> {
            meta.clearEffects();
            meta.addEffects(effects);
        });
    }

    // POTION

    public ItemStackBuilder withPotionEffect(PotionEffect effect) {
        return applyMeta(PotionMeta.class, meta ->
                meta.addCustomEffect(effect, true));
    }

    public ItemStackBuilder withPotionEffects(PotionEffect... effects) {
        return withPotionEffects(Arrays.asList(effects));
    }

    public ItemStackBuilder withPotionEffects(List<PotionEffect> effects) {
        return applyMeta(PotionMeta.class, meta -> {
            for ( PotionEffect effect : effects ) {
                meta.addCustomEffect(effect, true);
            }
        });
    }

    public ItemStackBuilder withPotionEffect(PotionEffectType type) {
        return applyMeta(PotionMeta.class, meta ->
                meta.addCustomEffect(new PotionEffect(type, 1200, 0), true));
    }

    public ItemStackBuilder withPotionEffect(PotionEffectType type, int duration) {
        return applyMeta(PotionMeta.class, meta ->
                meta.addCustomEffect(new PotionEffect(type, duration, 0), true));
    }

    public ItemStackBuilder withPotionEffect(PotionEffectType type, int duration, int amplifier) {
        return applyMeta(PotionMeta.class, meta ->
                meta.addCustomEffect(new PotionEffect(type, duration, amplifier), true));
    }

    public ItemStackBuilder withPotionEffect(PotionEffectType type, int duration, int amplifier, boolean ambient) {
        return applyMeta(PotionMeta.class, meta ->
                meta.addCustomEffect(new PotionEffect(type, duration, amplifier, ambient), true));
    }

    public ItemStackBuilder withPotionEffect(PotionEffectType type, int duration, int amplifier, boolean ambient, boolean particles) {
        return applyMeta(PotionMeta.class, meta ->
                meta.addCustomEffect(new PotionEffect(type, duration, amplifier, ambient, particles), true));
    }

}
