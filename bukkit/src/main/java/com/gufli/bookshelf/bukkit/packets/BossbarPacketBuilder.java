/**
 * PacketWrapper - ProtocolLib wrappers for Minecraft packets
 * Copyright (C) dmulloy2 <http://dmulloy2.net>
 * Copyright (C) Kristian S. Strangeland
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.gufli.bookshelf.bukkit.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.AbstractStructure;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.gufli.bookshelf.bukkit.api.bossbar.Bossbar;
import org.bukkit.boss.BarColor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class BossbarPacketBuilder {

    public static final PacketType TYPE = PacketType.Play.Server.BOSS;

    private static Class<?> bossBattleClass;
    private static Constructor<?> bossBattleConstructor;
    private static Constructor<?> bossBattleActionConstructor;

    private static Object solidStyle;

    private static EnumWrappers.EnumConverter<BarColor> colorConverter;

    static {
        try {
            bossBattleClass = Class.forName("net.minecraft.world.BossBattle");
            Class<?> barColorClass = Class.forName(bossBattleClass.getName() + "$BarColor");
            Class<?> barStyleClass = Class.forName(bossBattleClass.getName() + "$BarStyle");
            bossBattleConstructor = bossBattleClass.getConstructor(UUID.class,
                    MinecraftReflection.getIChatBaseComponentClass(),
                    barColorClass, barStyleClass);
            for (Class<?> cls : bossBattleClass.getDeclaredClasses()) {
                if (cls.isEnum() || cls.isInterface()) {
                    continue;
                }

                try {
                    bossBattleActionConstructor = cls.getConstructor(bossBattleClass);
                    break;
                } catch (NoSuchMethodException ignored) {
                }
            }
            solidStyle = barStyleClass.getEnumConstants()[0];

            colorConverter = new EnumWrappers.EnumConverter<>(barColorClass, BarColor.class);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    //

    private static AbstractStructure createBossBattle(UUID id, Bossbar bossbar) {
        try {
            Object bossBattle = bossBattleConstructor.newInstance(
                    id,
                    WrappedChatComponent.fromText(bossbar.text()).getHandle(),
                    colorConverter.getGeneric(bossbar.color()),
                    solidStyle
            );
            StructureModifier<Object> modifier = new StructureModifier<>(bossBattleClass).withTarget(bossBattle);
            return new AbstractStructure(bossBattle, modifier) {
            };
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AbstractPacket create(UUID id, Bossbar bossbar) {
        AbstractStructure bossBattle = createBossBattle(id, bossbar);
        bossBattle.getFloat().write(0, bossbar.progress());

        try {
            Object action = bossBattleActionConstructor.newInstance(bossBattle);
            PacketContainer packet = new PacketContainer(TYPE);
            packet.getUUIDs().write(0, id);
            packet.getModifier().write(1, action);
            return new AbstractPacket(packet);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AbstractPacket remove(UUID id) {
        try {
            Object action = TYPE.getPacketClass().getDeclaredField("f").get(null);
            PacketContainer packet = new PacketContainer(TYPE);
            packet.getUUIDs().write(0, id);
            packet.getModifier().write(1, action);
            return new AbstractPacket(packet);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

}
