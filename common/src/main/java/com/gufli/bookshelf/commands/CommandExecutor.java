package com.gufli.bookshelf.commands;

import com.gufli.bookshelf.entity.ShelfCommandSender;

import java.util.List;

public interface CommandExecutor<T extends ShelfCommandSender> {

    void execute(T sender, String[] args);

    default List<String> autocomplete(T sender, String[] args) {
        return null;
    }

}
