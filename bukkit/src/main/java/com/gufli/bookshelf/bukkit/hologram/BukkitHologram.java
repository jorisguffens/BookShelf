package com.gufli.bookshelf.bukkit.hologram;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.gufli.bookshelf.api.color.TextColor;
import com.gufli.bookshelf.api.entity.ShelfPlayer;
import com.gufli.bookshelf.api.location.ShelfLocation;
import com.gufli.bookshelf.api.placeholders.Placeholders;
import com.gufli.bookshelf.bukkit.BukkitShelf;
import com.gufli.bookshelf.bukkit.api.entity.BukkitPlayer;
import com.gufli.bookshelf.bukkit.api.hologram.Hologram;
import com.gufli.bookshelf.bukkit.api.location.LocationConverter;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BukkitHologram {

    private final Hologram template;
    private final ShelfPlayer player;

    private ShelfLocation previousLocation;
    private List<String> previousLines;

    private final Map<Integer, ArmorStand> armorStands = new HashMap<>();

    private final PacketListener packetListener = new PacketAdapter(BukkitShelf.plugin(), PacketType.Play.Server.SPAWN_ENTITY_LIVING) {
        @Override
        public void onPacketSending(PacketEvent event) {
            int id = event.getPacket().getIntegers().read(0);
            if ( armorStands.containsKey(id) && event.getPlayer() != ((BukkitPlayer) player).handle() ) {
                event.setCancelled(true);
            }
        }
    };

    public BukkitHologram(ShelfPlayer player, Hologram template) {
        this.player = player;
        this.template = template;

        ProtocolLibrary.getProtocolManager().addPacketListener(packetListener);
    }

    public void destroy() {
        ProtocolLibrary.getProtocolManager().removePacketListener(packetListener);
        destroyInternal();
    }

    private void destroyInternal() {
        armorStands.values().forEach(Entity::remove);
        armorStands.clear();
    }

    public void update() {

        if ( !template.location().equals(previousLocation) ) {
            previousLocation = template.location();
            destroyInternal();
        }

        // only reformat when the contents changed
        if (!template.lines().equals(previousLines)) {
            this.previousLines = new ArrayList<>(template.lines());
            for (int i = 0; i < previousLines.size(); i++) {
                String line = template.lines().get(i);
                line = ChatColor.translateAlternateColorCodes('&', line);
                line = StringEscapeUtils.unescapeJava(line);
                previousLines.set(i, line);
            }
        }

        List<String> contents = new ArrayList<>(previousLines);

        // update placeholders
        for (int i = 0; i < contents.size(); i++) {
            contents.set(i, Placeholders.replace(player, contents.get(i)));
        }

        // remove old armor stands
        destroyInternal();

        // spawn new armorstands
        double spacing = 0.3D;
        for ( int i = 0; i < contents.size(); i++ ) {
            String line = contents.get(i);
            Location loc = LocationConverter.convert(previousLocation.add(0, i * spacing - 1.15d, 0));


            ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
            as.setGravity(false);
            as.setVisible(false);
            as.setCustomName(TextColor.WHITE + line);
            as.setCustomNameVisible(true);
            as.setSmall(true);

            armorStands.put(as.getEntityId(), as);
        }
    }

}
