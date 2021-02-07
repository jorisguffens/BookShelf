package com.gufli.bookshelf.util;

import java.util.function.Function;

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

    private static Function<TextColor, String> colorMapper;

    public static void register(Function<TextColor, String> mapper) {
        if ( colorMapper != null ) {
            throw new UnsupportedOperationException("Cannot redefine singleton colorMapper.");
        }
        colorMapper = mapper;
    }

    @Override
    public String toString() {
        return colorMapper.apply(this);
    }

}
