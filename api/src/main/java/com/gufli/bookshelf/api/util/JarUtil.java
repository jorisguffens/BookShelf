package com.gufli.bookshelf.api.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JarUtil {

    private JarUtil() {
    }

    public static Collection<Class<?>> findClassesInPackage(Class<?> sender, String packageName) {
        return findClassesInPackage(sender.getProtectionDomain().getCodeSource().getLocation(), packageName);
    }

    public static Collection<Class<?>> findClassesInPackage(URL jarURL, String packageName) {
        Map<String, Class<?>> classes = new HashMap<>();

        packageName = packageName.replaceAll(Pattern.quote("."), "/");
        try (
                JarInputStream jarFile = new JarInputStream(jarURL.openStream());
        ) {
            JarEntry jarEntry;
            while ((jarEntry = jarFile.getNextJarEntry()) != null) {
                if (!jarEntry.getName().startsWith(packageName)
                        || !jarEntry.getName().endsWith(".class")) {
                    continue;
                }

                String filename = jarEntry.getName().replaceAll(Pattern.quote("/"), "\\.").replace(".class", "");
                if (classes.containsKey(filename)) {
                    continue;
                }

                try {
                    Class<?> cls = Class.forName(filename);
                    classes.put(filename, cls);
                } catch (ClassNotFoundException ignored) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return classes.values();
    }

    public static InputStream findResource(Class<?> sender, String name) throws IOException {
        URL url = sender.getClassLoader().getResource(name);

        if (url == null) {
            return null;
        }

        URLConnection connection = url.openConnection();
        connection.setUseCaches(false);
        return connection.getInputStream();
    }

    public static String findAndReadResource(Class<?> sender, String name) throws IOException {
        InputStream inputStream = findResource(sender, name);
        if (inputStream == null) {
            return null;
        }

        try (
                inputStream;
                InputStreamReader isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
        ) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }


}
