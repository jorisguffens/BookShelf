package com.gufli.bookshelf.messages;

import com.gufli.bookshelf.api.config.Configuration;
import com.gufli.bookshelf.api.entity.ShelfCommandSender;
import com.gufli.bookshelf.api.messages.Messages;
import com.gufli.bookshelf.config.TextConfiguration;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class DefaultMessages {

    private static Messages messages;

    static {
        URL url = DefaultMessages.class.getClassLoader().getResource("default.language.txt");
        if ( url != null ) {
            try (
                    InputStream is = url.openStream();
                    InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                    BufferedReader br = new BufferedReader(isr);
            ) {
                Configuration config = new TextConfiguration(br.lines().collect(Collectors.joining("\n")));
                register(new SimpleMessages(config));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void register(Messages messages) {
        if (DefaultMessages.messages != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton messages.");
        }

        DefaultMessages.messages = messages;
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
