package com.gufli.bookshelf.api.sidebar;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

import java.util.List;
import java.util.function.BiConsumer;

public class SidebarTemplate {

    private final String title;
    private final List<String> contents;

    private final BiConsumer<ShelfPlayer, List<String>> updater;

    public SidebarTemplate(String title, List<String> contents, BiConsumer<ShelfPlayer, List<String>> updater) {
        this.title = title;
        this.contents = List.copyOf(contents);
        this.updater = updater;
    }

    public SidebarTemplate(String title, List<String> contents) {
        this(title, contents, null);
    }

    public String title() {
        return title;
    }

    public List<String> contents() {
        return contents;
    }

    public BiConsumer<ShelfPlayer, List<String>> updater() {
        return updater;
    }

}
