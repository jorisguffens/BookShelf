package com.gufli.bookshelf.util;

import java.util.HashMap;
import java.util.Map;

public class Color {

    private static final int BIT_MASK = 0xff;

    public static final Color WHITE = fromRGB(0xFFFFFF);
    public static final Color SILVER = fromRGB(0xC0C0C0);
    public static final Color GRAY = fromRGB(0x808080);
    public static final Color BLACK = fromRGB(0x000000);
    public static final Color RED = fromRGB(0xFF0000);
    public static final Color MAROON = fromRGB(0x800000);
    public static final Color YELLOW = fromRGB(0xFFFF00);
    public static final Color OLIVE = fromRGB(0x808000);
    public static final Color LIME = fromRGB(0x00FF00);
    public static final Color GREEN = fromRGB(0x008000);
    public static final Color AQUA = fromRGB(0x00FFFF);
    public static final Color TEAL = fromRGB(0x008080);
    public static final Color BLUE = fromRGB(0x0000FF);
    public static final Color NAVY = fromRGB(0x000080);
    public static final Color FUCHSIA = fromRGB(0xFF00FF);
    public static final Color PURPLE = fromRGB(0x800080);
    public static final Color ORANGE = fromRGB(0xFFA500);
    public static final Color PINK = fromRGB(0xFFC0CB);

    private static final Map<String, Color> colors = new HashMap<>();
    static {
        colors.put("WHITE", WHITE);
        colors.put("SILVER", SILVER);
        colors.put("GRAY", GRAY);
        colors.put("BLACK", BLACK);
        colors.put("RED", RED);
        colors.put("MAROON", MAROON);
        colors.put("YELLOW", YELLOW);
        colors.put("OLIVE", OLIVE);
        colors.put("LIME", LIME);
        colors.put("GREEN", GREEN);
        colors.put("AQUA", AQUA);
        colors.put("TEAL", TEAL);
        colors.put("BLUE", BLUE);
        colors.put("NAVY", NAVY);
        colors.put("FUCHSIA", FUCHSIA);
        colors.put("PURPLE", PURPLE);
        colors.put("ORANGE", ORANGE);
        colors.put("PINK", PINK);
    }

    private int red, green, blue;

    public static Color fromRGB(int red, int green, int blue) {
        return new Color(red, green, blue);
    }

    public static Color fromRGB(int rgb) {
        return fromRGB(rgb >> 16 & BIT_MASK, rgb >> 8 & BIT_MASK, rgb >> 0 & BIT_MASK);
    }

    public static Color fromHSV(float hue, float saturation, float value) {
        return fromRGB(java.awt.Color.HSBtoRGB(hue, saturation, value));
    }

    public Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
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

    public int getRGB() {
        return getRed() << 16 | getGreen() << 8 | getBlue() << 0;
    }

    public float[] getHSV() {
        return java.awt.Color.RGBtoHSB(red, green, blue, null);
    }

    public int getMSE(int pixR, int pixG, int pixB) {
        return ((pixR - red) * (pixR - red)
                + (pixG - green) * (pixG - green)
                + (pixB - blue) * (pixB - blue)) / 3;
    }

    public int getMSE(Color color) {
        return getMSE(color.getRed(), color.getGreen(), color.getBlue());
    }

    public String getName() {
        String closestMatch = null;
        int minMSE = Integer.MAX_VALUE;

        for ( String name : colors.keySet() ) {
            Color c = colors.get(name);
            int mse = c.getMSE(this);
            if (mse < minMSE) {
                minMSE = mse;
                closestMatch = name;
            }
        }

        return closestMatch;
    }

}
