package com.gufli.bookshelf.api.config;

import com.gufli.bookshelf.api.config.Configuration;

import java.util.*;
import java.util.regex.Pattern;

public class TextConfiguration implements Configuration {

    private final Map<String, Object> options = new HashMap<>();

    public TextConfiguration(String config) {
        for ( String line : config.split(Pattern.quote("\n")) ) {
            parseLine(line);
        }
    }

    private void parseLine(String line) {
        line = line.trim();

        if ( line.equals("") || line.startsWith("#") ) {
            return;
        }

        if ( !line.matches("[ -~]+[=][\"']?[ -~]*[\"']?") ) {
            throw new IllegalArgumentException("Cannot parse line '" + line + "'.");
        }

        String[] args = line.split(Pattern.quote("="));
        String key = args[0].trim();
        String value = args[1].trim();

        if ( value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false") ) {
            options.put(key, Boolean.parseBoolean(value.toLowerCase()));
            return;
        }

        if ( value.matches("[0-9]+") ) {
            options.put(key, Integer.parseInt(value));
            return;
        }

        if ( value.matches("[0-9]?[\\.][0-9]+") ) {
            options.put(key, Double.parseDouble(value));
            return;
        }

        if ( value.matches("[\"'].*[\"']") ) {
            options.put(key, value.substring(1, value.length() - 1));
            return;
        }

        options.put(key, value);
    }

    @Override
    public Object get(String path) {
        return options.get(path);
    }

    @Override
    public boolean contains(String path) {
        return options.containsKey(path);
    }

    @Override
    public String getString(String path) {
        return (String) options.get(path);
    }

    @Override
    public boolean getBoolean(String path) {
        return (boolean) options.get(path);
    }

    @Override
    public int getInt(String path) {
        return (int) options.get(path);
    }

    @Override
    public double getDouble(String path) {
        return (double) options.get(path);
    }

    @Override
    public List<String> getStringList(String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Configuration getSection(String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> getKeys(boolean deep) {
        return Collections.unmodifiableSet(options.keySet());
    }
}
