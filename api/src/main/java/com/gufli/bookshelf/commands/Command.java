package com.gufli.bookshelf.commands;

import com.gufli.bookshelf.entity.PlatformSender;

import java.util.Arrays;
import java.util.List;

public abstract class Command<T extends PlatformSender> {

    private final CommandInfo info;

    public Command() {
        if ( !this.getClass().isAnnotationPresent(CommandInfo.class) ) {
            throw new IllegalStateException("CommandInfo annotation is required");
        }

        info = this.getClass().getAnnotation(CommandInfo.class);
    }

    final CommandInfo getInfo() {
        return info;
    }

    final boolean hasAnyPermission(PlatformSender sender) {
        return getInfo().permissions().length == 0 || Arrays.stream(getInfo().permissions()).anyMatch(sender::hasPermission);
    }

    final boolean hasAllPermissions(PlatformSender sender) {
        return getInfo().permissions().length == 0 || Arrays.stream(getInfo().permissions()).allMatch(sender::hasPermission);
    }

    //

    final void executeInternal(PlatformSender sender, String[] args) {
        execute((T) sender, args);
    }

    final List<String> autocompleteInternal(PlatformSender sender, String[] args) {
        return autocomplete((T) sender, args);
    }

    // implementation

    public abstract void execute(T sender, String[] args);

    public List<String> autocomplete(T sender, String[] args) {
        return null;
    }

}
