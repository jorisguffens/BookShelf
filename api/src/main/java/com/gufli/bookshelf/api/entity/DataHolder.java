package com.gufli.bookshelf.api.entity;

public interface DataHolder {

    void set(String key, Object value);

    boolean has(String key);

    void remove(String key);

    Object get(String key);

    <T> T get(String key, Class<T> clazz);

    default String getString(String key) {
        return get(key, String.class);
    }

    default boolean getBoolean(String key) {
        return get(key, Boolean.class);
    }

    default int getInt(String key) {
        return get(key, Integer.class);
    }

    default double getDouble(String key) {
        return get(key, Double.class);
    }

}
