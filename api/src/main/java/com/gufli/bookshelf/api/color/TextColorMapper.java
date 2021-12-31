package com.gufli.bookshelf.api.color;

public interface TextColorMapper {

    String map(TextColor color);

    String translate(String string);

    String strip(String string);

}
