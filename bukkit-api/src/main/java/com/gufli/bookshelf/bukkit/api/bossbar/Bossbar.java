package com.gufli.bookshelf.bukkit.api.bossbar;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

public class Bossbar {

    private String text;
    private BarColor color;
    private BarStyle style;
    private float progress;

    public Bossbar(String text, BarColor color, BarStyle style, float progress) {
        this.text = text;
        this.color = color;
        this.style = style;
        this.progress = progress;
    }

    public Bossbar(String text, BarColor color, BarStyle style) {
        this(text, color, style, 1f);
    }

    public Bossbar(String text, BarColor color) {
        this(text, color, BarStyle.SOLID, 1f);
    }

    public String text() {
        return text;
    }

    public BarColor color() {
        return color;
    }

    public BarStyle style() {
         return style;
    }

    public float progress() {
        return progress;
    }

    public void changeProgress(float progress) {
        this.progress = progress;
    }

    public void changeColor(BarColor color) {
        this.color = color;
    }

    public void changeStyle(BarStyle style) {
        this.style = style;
    }

    public void changeText(String text) {
        this.text = text;
    }
}

