package com.gufli.bookshelf.bukkit.nametags;

import com.gufli.bookshelf.api.color.TextColor;
import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.event.Events;
import com.gufli.bookshelf.api.events.PlayerJoinEvent;
import com.gufli.bookshelf.api.events.PlayerQuitEvent;
import com.gufli.bookshelf.api.events.ShelfShutdownEvent;
import com.gufli.bookshelf.api.nametags.NametagManager;
import com.gufli.bookshelf.api.nametags.Nametags;
import com.gufli.bookshelf.api.placeholders.Placeholders;
import com.gufli.bookshelf.api.server.Bookshelf;
import com.gufli.bookshelf.bukkit.api.entity.BukkitPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

public class BukkitNametagManager implements NametagManager {

    private final static String UNIQUEID = "BSNTGS";
    private long COUNTER = 0;

    private final Map<ShelfPlayer, NametagSettings> nametagSettings = new ConcurrentHashMap<>();
    private final Set<FakeTeam> fakeTeams = new CopyOnWriteArraySet<>();

    public BukkitNametagManager() {
        Nametags.register(this);

        Events.subscribe(PlayerJoinEvent.class)
                .handler(e -> showAll(e.player()));

        Events.subscribe(PlayerQuitEvent.class)
                .handler(e -> removeNametag(e.player()));

        Events.subscribe(ShelfShutdownEvent.class)
                .handler(e -> nametagSettings.keySet().forEach(this::removeNametag));

        Bookshelf.scheduler().asyncRepeating(() ->
                        nametagSettings.keySet().forEach(this::refresh),
                50, TimeUnit.MILLISECONDS);
    }

    @Override
    public void changeNametag(ShelfPlayer player, String prefix, String suffix) {
        if (prefix == null) prefix = "";
        if (suffix == null) suffix = "";

        NametagSettings nametagSettings = new NametagSettings(prefix, suffix);
        this.nametagSettings.put(player, nametagSettings);
        refresh(player);
    }

    @Override
    public void changePrefix(ShelfPlayer player, String prefix) {
        NametagSettings previous = nametagSettings.get(player);
        if (previous != null) {
            changeNametag(player, prefix, previous.suffix());
            return;
        }
        changeNametag(player, prefix, "");
    }

    @Override
    public void changeSuffix(ShelfPlayer player, String suffix) {
        NametagSettings previous = nametagSettings.get(player);
        if (previous != null) {
            changeNametag(player, previous.prefix(), suffix);
            return;
        }
        changeNametag(player, "", suffix);
    }

    @Override
    public void removeNametag(ShelfPlayer player) {
        nametagSettings.remove(player);
        remove(player);
    }

    //

    private void refresh(ShelfPlayer player) {
        NametagSettings nametagSettings = this.nametagSettings.get(player);
        if (nametagSettings == null) {
            return;
        }

        String prefix = nametagSettings.prefix();
        if (prefix != null) {
            prefix = Placeholders.replace(player, prefix);
            prefix = TextColor.translate(prefix);
        }

        String suffix = nametagSettings.suffix();
        if (suffix != null) {
            suffix = Placeholders.replace(player, suffix);
            suffix = TextColor.translate(suffix);
        }

        apply(player, prefix, suffix);
    }

    private void apply(ShelfPlayer player, String prefix, String suffix) {
        if (prefix != null) {
            prefix = ChatColor.translateAlternateColorCodes('&', prefix);
        }
        if (suffix != null) {
            suffix = ChatColor.translateAlternateColorCodes('&', suffix);
        }

        // If player is already in the team -> ignore
        FakeTeam previous = fakeTeam(player);
        if (previous != null && previous.isSimilar(prefix, suffix)) {
            return;
        }

        // Remove from old team
        remove(player);
        if ((prefix == null || prefix.equals("")) && (suffix == null || suffix.equals(""))) {
            return;
        }

        FakeTeam team = fakeTeam(prefix, suffix);
        if (team != null) {
            // Team already exists
            team.addPlayer(player.name());
            return;
        }

        // Team doesn't exist
        team = new FakeTeam(UNIQUEID + (COUNTER++), prefix, suffix);
        team.showAll();
        team.addPlayer(player.name());

        fakeTeams.add(team);
    }

    private void remove(ShelfPlayer player) {
        FakeTeam team = fakeTeam(player);
        if (team == null) {
            return;
        }

        team.removePlayer(player.name());

        // team is empty -> delete
        if (team.members().size() == 0) {
            team.hide();
            fakeTeams.remove(team);
        }
    }

    //

    private FakeTeam fakeTeam(String prefix, String suffix) {
        return fakeTeams.stream().filter(t -> t.isSimilar(prefix, suffix))
                .findFirst().orElse(null);
    }

    private FakeTeam fakeTeam(ShelfPlayer player) {
        return fakeTeams.stream().filter(t -> t.members().contains(player.name()))
                .findFirst().orElse(null);
    }

    private void showAll(ShelfPlayer player) {
        Player bp = ((BukkitPlayer) player).handle();
        for (FakeTeam team : fakeTeams) {
            team.showFor(bp);
        }
    }

    // packets

}
