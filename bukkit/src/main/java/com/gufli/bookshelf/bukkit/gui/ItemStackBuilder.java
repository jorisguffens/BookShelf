package com.gufli.bookshelf.bukkit.gui;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

    public ItemStack build() {
        return this.itemStack;
    }

}
