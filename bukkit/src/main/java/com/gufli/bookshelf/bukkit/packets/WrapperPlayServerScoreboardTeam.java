/**
 * PacketWrapper - ProtocolLib wrappers for Minecraft packets
 * Copyright (C) dmulloy2 <http://dmulloy2.net>
 * Copyright (C) Kristian S. Strangeland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.gufli.bookshelf.bukkit.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.AbstractStructure;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.ChatColor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class WrapperPlayServerScoreboardTeam extends AbstractPacket {

	public static final PacketType TYPE =
			PacketType.Play.Server.SCOREBOARD_TEAM;

	private static Class<?> scoreboardTeamClass;
	private static Constructor<?> scoreboardTeamConstructor;

	private static Constructor<?> innerScoreboardTeamConstructor;

	static {
		try {
			Class<?> scoreboardClass = Class.forName("net.minecraft.world.scores.Scoreboard");
			scoreboardTeamClass = Class.forName("net.minecraft.world.scores.ScoreboardTeam");
			scoreboardTeamConstructor = scoreboardTeamClass.getConstructor(scoreboardClass, String.class);

			Class<?> innerScoreboardTeamClass = TYPE.getPacketClass().getDeclaredClasses()[0];
			innerScoreboardTeamConstructor = innerScoreboardTeamClass.getConstructor(scoreboardTeamClass);
		} catch (ClassNotFoundException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	//

	private AbstractStructure scoreboardTeam;

	public WrapperPlayServerScoreboardTeam(String name) {
		super(new PacketContainer(TYPE));
		handle.getStrings().write(0, name);
		handle.getIntegers().write(0, 0); // 0 = TEAM CREATED

		try {
			Object scoreboardTeam = scoreboardTeamConstructor.newInstance(null, name);
			StructureModifier<Object> modifier = new StructureModifier<>(scoreboardTeamClass).withTarget(scoreboardTeam);
			this.scoreboardTeam = new AbstractStructure(scoreboardTeam, modifier) {};
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public PacketContainer getHandle() {
		PacketContainer handle = super.getHandle();
		try {
			Object ist = innerScoreboardTeamConstructor.newInstance(scoreboardTeam.getHandle());
			handle.getModifier().withType(Optional.class).write(0, Optional.of(ist));
		} catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return handle;
	}

	public WrappedChatComponent getDisplayName() {
		return scoreboardTeam.getChatComponents().read(0);
	}

	public void setDisplayName(WrappedChatComponent value) {
		scoreboardTeam.getChatComponents().write(0, value);
	}

	public WrappedChatComponent getPrefix() {
		return scoreboardTeam.getChatComponents().read(1);
	}

	public void setPrefix(WrappedChatComponent value) {
		scoreboardTeam.getChatComponents().write(1, value);
	}

	public WrappedChatComponent getSuffix() {
		return scoreboardTeam.getChatComponents().read(2);
	}

	public void setSuffix(WrappedChatComponent value) {
		scoreboardTeam.getChatComponents().write(2, value);
	}

	public ChatColor getColor() {
		return scoreboardTeam.getEnumModifier(ChatColor.class, MinecraftReflection.getMinecraftClass("EnumChatFormat")).read(0);
	}

	public void setColor(ChatColor value) {
		scoreboardTeam.getEnumModifier(ChatColor.class, MinecraftReflection.getMinecraftClass("EnumChatFormat")).write(0, value);
	}
}

