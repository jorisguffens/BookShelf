package com.gufli.bookshelf.messages;

import com.gufli.bookshelf.api.config.Configuration;
import com.gufli.bookshelf.api.entity.ShelfCommandSender;
import com.gufli.bookshelf.api.messages.Messages;
import com.gufli.bookshelf.api.config.TextConfiguration;
import com.gufli.bookshelf.api.messages.SimpleMessages;
import com.gufli.bookshelf.api.util.JarUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class DefaultMessages {

    private final static Messages messages = load();

    private static Messages load() {
        try {
            String contents = JarUtil.findAndReadResource(DefaultMessages.class, "default.language.txt");
            if ( contents == null ) return null;
            Configuration config = new TextConfiguration(contents);
            return new SimpleMessages(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //

    public static String prefix() {
        return messages.prefix();
    }

    public static String get(String name) {
        return messages.get(name);
    }

    public static String get(String name, String... placeholders) {
        return messages.get(name, placeholders);
    }

    public static void send(ShelfCommandSender sender, String name, String... placeholders) {
        messages.send(sender, name, placeholders);
    }


}
