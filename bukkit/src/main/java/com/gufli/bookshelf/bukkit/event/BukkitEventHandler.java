package com.gufli.bookshelf.bukkit.event;

import com.gufli.bookshelf.api.event.hook.EventHandler;
import com.gufli.bookshelf.api.event.EventPriority;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BukkitEventHandler extends EventHandler<Event> implements EventExecutor, Listener {

    private final JavaPlugin plugin;
    private final Map<Class<? extends Event>, EventPriority> registeredClasses = new HashMap<>();

    public BukkitEventHandler(JavaPlugin plugin, Consumer<Event> handler) {
        super(handler);
        this.plugin = plugin;
    }

    @Override
    public void register(Class<? extends Event> type, EventPriority priority) {
        Class<? extends Event> registrationType = getRegistrationClass(type);

        if ( registeredClasses.containsKey(registrationType) && registeredClasses.get(registrationType) == priority ) {
            throw new RuntimeException("Cannot register event again with different priority.");
        }
        registeredClasses.put(registrationType, priority);

        plugin.getServer().getPluginManager()
                .registerEvent(registrationType, this, getBukkitPriority(priority), this, plugin, false);
    }

    @Override
    public void unregister() {
        for ( Class<? extends Event> clazz : registeredClasses.keySet() ) {
            unregisterListener(clazz, this);
        }
    }

    private org.bukkit.event.EventPriority getBukkitPriority(EventPriority priority) {
        return org.bukkit.event.EventPriority.valueOf(priority.name());
    }

    private static void unregisterListener(Class<? extends Event> eventClass, Listener listener) {
        try {
            // unfortunately we can't cache this reflect call, as the method is static
            Method getHandlerListMethod = eventClass.getMethod("getHandlerList");
            HandlerList handlerList = (HandlerList) getHandlerListMethod.invoke(null);
            handlerList.unregister(listener);
        } catch (Throwable t) {
            // ignored
        }
    }

    private static Class<? extends Event> getRegistrationClass(Class<?> clazz) {
        try {
            clazz.getDeclaredMethod("getHandlerList");
            return (Class<? extends Event>) clazz;
        } catch (NoSuchMethodException var2) {
            if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Event.class) && Event.class.isAssignableFrom(clazz.getSuperclass())) {
                return getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
            } else {
                throw new IllegalPluginAccessException("Unable to find handler list for event " + clazz.getName() + ".");
            }
        }
    }

    @Override
    public void execute(Listener listener, Event event) {
        handler.accept(event);
    }
}
