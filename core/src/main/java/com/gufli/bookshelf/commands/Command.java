package com.gufli.bookshelf.commands;

import com.gufli.bookshelf.entity.ShelfCommandSender;

import java.util.Arrays;
import java.util.List;

public abstract class Command<T extends ShelfCommandSender> {

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

    final boolean hasAnyPermission(ShelfCommandSender sender) {
        return getInfo().permissions().length == 0 || Arrays.stream(getInfo().permissions()).anyMatch(sender::hasPermission);
    }

    final boolean hasAllPermissions(ShelfCommandSender sender) {
        return getInfo().permissions().length == 0 || Arrays.stream(getInfo().permissions()).allMatch(sender::hasPermission);
    }

    //

    public final void execute(ShelfCommandSender sender, String[] args) {
        execute((T) sender, args);
    }

    public final List<String> autocomplete(ShelfCommandSender sender, String[] args) {
        return autocomplete((T) sender, args);
    }

    // implementation

    protected abstract void onExecute(T sender, String[] args);

    protected List<String> onAutocomplete(T sender, String[] args) {
        return null;
    }

}
