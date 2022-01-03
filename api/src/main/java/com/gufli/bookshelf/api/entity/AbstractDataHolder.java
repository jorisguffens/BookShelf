package com.gufli.bookshelf.api.entity;

import java.util.HashMap;
import java.util.Map;

public class AbstractDataHolder implements DataHolder {

    final Map<String, Object> cache = new HashMap<>();

    public void set(String key, Object value) {
        if (value == null) {
            remove(key);
        } else {
            cache.put(key, value);
        }
    }

    public boolean has(String key) {
        return cache.containsKey(key);
    }

    public void remove(String key) {
        cache.remove(key);
    }

    public Object get(String key) {
        return cache.get(key);
    }

    public <T> T get(String key, Class<T> clazz) {
        return clazz.cast(cache.get(key));
    }

}
