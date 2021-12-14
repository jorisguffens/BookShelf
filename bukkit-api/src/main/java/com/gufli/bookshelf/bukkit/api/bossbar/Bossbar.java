package com.gufli.bookshelf.bukkit.api.bossbar;

import com.gufli.bookshelf.api.color.Color;

public class Bossbar {

    private String text;
    private Color color;
    private float percent;

    public Bossbar(String text, Color color, float percent) {
        this.text = text;
        this.color = color;
        this.percent = percent;
    }

    public Bossbar(String text, Color color) {
        this(text, color, 1);
    }

    public Bossbar(String text, float percent) {
        this(text, null, percent);
    }

    public Bossbar(String text) {
        this(text, null, 1);
    }

    public String getText() {
        return text;
    }

    public Color getColor() {
        return color;
    }

    public float getPercent() {
        return percent;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }
}
