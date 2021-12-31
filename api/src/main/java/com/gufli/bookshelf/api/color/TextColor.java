package com.gufli.bookshelf.api.color;

public enum TextColor {
    BLACK,
    DARK_BLUE,
    DARK_GREEN,
    DARK_AQUA,
    DARK_RED,
    DARK_PURPLE,
    GOLD,
    GRAY,
    DARK_GRAY,
    BLUE,
    GREEN,
    AQUA,
    RED,
    LIGHT_PURPLE,
    YELLOW,
    WHITE;

    private static TextColorMapper colorMapper;

    public static void register(TextColorMapper mapper) {
        if (colorMapper != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton colorMapper.");
        }
        colorMapper = mapper;
    }

    @Override
    public String toString() {
        return colorMapper.map(this);
    }

    public static String translate(String str) {
        return colorMapper.translate(str);
    }

    public static String strip(String str) {
        return colorMapper.strip(str);
    }

}
