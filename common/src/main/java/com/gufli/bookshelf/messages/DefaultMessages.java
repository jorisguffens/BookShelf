package com.gufli.bookshelf.messages;

import com.gufli.bookshelf.api.config.Configuration;
import com.gufli.bookshelf.api.entity.ShelfCommandSender;
import com.gufli.bookshelf.api.messages.Messages;
import com.gufli.bookshelf.config.TextConfiguration;

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
        InputStream is;
        try {
            URL url = DefaultMessages.class.getClassLoader().getResource("default.language.txt");

            if (url == null) {
                return null;
            }

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            is = connection.getInputStream();
        } catch (IOException ex) {
            return null;
        }

        if (is == null) {
            return null;
        }

        try (
                is;
                InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
        ) {
            Configuration config = new TextConfiguration(br.lines().collect(Collectors.joining("\n")));
            return new SimpleMessages(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //

    public static String getPrefix() {
        return messages.getPrefix();
    }

    public static String getMessage(String name) {
        return messages.getMessage(name);
    }

    public static String getMessage(String name, String... placeholders) {
        return messages.getMessage(name, placeholders);
    }

    public static void send(ShelfCommandSender sender, String name, String... placeholders) {
        messages.send(sender, name, placeholders);
    }


}
