package com.gufli.bookshelf.api.item;

public class ItemSerializer {

    private ItemSerializer() {}

    private static Item.Serializer itemSerializer;

    public static void register(Item.Serializer serializer) {
        if (itemSerializer != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton itemSerializer.");
        }

        itemSerializer = serializer;
    }

    public static Item deserialize(String value) {
        return itemSerializer.deserialize(value);
    }

    public static String serialize(Item item) {
        return itemSerializer.serialize(item);
    }

}