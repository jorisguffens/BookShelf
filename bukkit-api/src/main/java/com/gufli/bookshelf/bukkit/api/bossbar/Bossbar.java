package com.gufli.bookshelf.bukkit.api.bossbar;

import org.bukkit.boss.BarColor;

public class Bossbar {

    private final String text;
    private final BarColor color;
    private final float progress;

    public Bossbar(String text, BarColor color, float progress) {
        this.text = text;
        this.color = color;
        this.progress = progress;
    }

    public String text() {
        return text;
    }

    public BarColor color() {
        return color;
    }

    public float progress() {
        return progress;
    }
}

