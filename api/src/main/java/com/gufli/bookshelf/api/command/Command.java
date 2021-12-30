package com.gufli.bookshelf.api.command;

import com.gufli.bookshelf.api.entity.ShelfCommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Command<T extends ShelfCommandSender> {

    private final CommandInfo info;

    public Command() {
        if ( !this.getClass().isAnnotationPresent(CommandInfo.class) ) {
            throw new IllegalStateException("CommandInfo annotation is required");
        }

        info = this.getClass().getAnnotation(CommandInfo.class);
    }

    public final CommandInfo info() {
        return info;
    }

    public final boolean hasAnyPermission(ShelfCommandSender sender) {
        return info().permissions().length == 0 || Arrays.stream(info().permissions()).anyMatch(sender::hasPermission);
    }

    public final boolean hasAllPermissions(ShelfCommandSender sender) {
        return info().permissions().length == 0 || Arrays.stream(info().permissions()).allMatch(sender::hasPermission);
    }

    //

    public final void execute(ShelfCommandSender sender, String[] args) {
        onExecute((T) sender, args);
    }

    public final List<String> autocomplete(ShelfCommandSender sender, String[] args) {
        List<String> result = onAutocomplete((T) sender, args);
        if ( result == null ) {
            return Collections.emptyList();
        }
        return result;
    }

    // implementation

    protected abstract void onExecute(T sender, String[] args);

    protected List<String> onAutocomplete(T sender, String[] args) {
        return null;
    }

}
