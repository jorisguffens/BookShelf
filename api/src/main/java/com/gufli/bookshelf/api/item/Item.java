package com.gufli.bookshelf.api.item;

public interface Item {

    Object handle();

    interface Serializer {

        Item deserialize(String value);

        String serialize(Item item);

    }

}