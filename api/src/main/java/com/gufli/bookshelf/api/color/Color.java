package com.gufli.bookshelf.api.color;

import java.util.HashMap;
import java.util.Map;

public class Color {

    private static final int BIT_MASK = 0xff;

    private static final Map<String, Color> colors = new HashMap<>();

    public static final Color WHITE = new Color("WHITE", 0xFFFFFF);
    public static final Color SILVER = new Color("SILVER", 0xC0C0C0);
    public static final Color GRAY = new Color("GRAY", 0x808080);
    public static final Color BLACK = new Color("BLACK", 0x000000);
    public static final Color RED = new Color("RED", 0xFF0000);
    public static final Color MAROON = new Color("MAROON", 0x800000);
    public static final Color YELLOW = new Color("YELLOW", 0xFFFF00);
    public static final Color OLIVE = new Color("OLIVE", 0x808000);
    public static final Color LIME = new Color("LIME", 0x00FF00);
    public static final Color GREEN = new Color("GREEN", 0x008000);
    public static final Color AQUA = new Color("AQUA", 0x00FFFF);
    public static final Color TEAL = new Color("TEAL", 0x008080);
    public static final Color BLUE = new Color("BLUE", 0x0000FF);
    public static final Color NAVY = new Color("NAVY", 0x000080);
    public static final Color FUCHSIA = new Color("FUCHSIA", 0xFF00FF);
    public static final Color PURPLE = new Color("PURPLE", 0x800080);
    public static final Color ORANGE = new Color("ORANGE", 0xFFA500);
    public static final Color PINK = new Color("PINK", 0xFFC0CB);

    private int red, green, blue;

    public static Color fromRGB(int red, int green, int blue) {
        return new Color(red, green, blue);
    }

    public static Color fromRGB(int rgb) {
        return new Color(rgb);
    }

    public static Color fromHSV(float hue, float saturation, float value) {
        return fromRGB(java.awt.Color.HSBtoRGB(hue, saturation, value));
    }

    // For internal use only
    private Color(String name, int rgb) {
        this(rgb);
        colors.put(name, this);
    }

    public Color(int rgb) {
        this.red = rgb >> 16 & BIT_MASK;
        this.green = rgb >> 8 & BIT_MASK;
        this.blue = rgb & BIT_MASK;
    }

    public Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int red() {
        return red;
    }

    public int green() {
        return green;
    }

    public int blue() {
        return blue;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int rgb() {
        return red() << 16 | green() << 8 | blue() << 0;
    }

    public float[] hsv() {
        return java.awt.Color.RGBtoHSB(red, green, blue, null);
    }

    public int mse(int pixR, int pixG, int pixB) {
        return ((pixR - red) * (pixR - red)
                + (pixG - green) * (pixG - green)
                + (pixB - blue) * (pixB - blue)) / 3;
    }

    public int mse(Color color) {
        return mse(color.red(), color.green(), color.blue());
    }

    public String name() {
        if ( colors.containsValue(this) ) {
            return colors.keySet().stream().filter(name -> colors.get(name).equals(this))
                    .findFirst().orElse(null);
        }

        String closestMatch = null;
        int minMSE = Integer.MAX_VALUE;

        for ( String name : colors.keySet() ) {
            Color c = colors.get(name);
            int mse = c.mse(this);
            if (mse < minMSE) {
                minMSE = mse;
                closestMatch = name;
            }
        }

        return closestMatch;
    }

    public static Color findColor(String name) {
        return colors.get(name);
    }

}
