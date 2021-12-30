package com.gufli.bookshelf.api.sidebar;

import java.util.List;

public class Sidebar {

    private String title;
    private List<String> contents;

    public Sidebar(String title, List<String> contents) {
        this.title = title;
        this.contents = List.copyOf(contents);
    }

    public String title() {
        return title;
    }

    public List<String> contents() {
        return contents;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContents(List<String> contents) {
        this.contents = List.copyOf(contents);
    }

}
