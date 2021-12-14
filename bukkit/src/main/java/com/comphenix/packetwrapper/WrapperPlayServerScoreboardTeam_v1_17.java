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
package com.comphenix.packetwrapper;

import com.comphenix.protocol.events.AbstractStructure;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.ChatColor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class WrapperPlayServerScoreboardTeam_v1_17 extends WrapperPlayServerScoreboardTeam {

	private static Class<?> scoreboardTeamClass;
	private static Constructor<?> scoreboardTeamConstructor;

	private static Constructor<?> innerScoreboardTeamConstructor;

	private AbstractStructure scoreboardTeam;

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

	public WrapperPlayServerScoreboardTeam_v1_17(String name) {
		super();
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
			Object ist = innerScoreboardTeamConstructor.newInstance(scoreboardTeam);
			handle.getModifier().withType(Optional.class).write(0, Optional.of(ist));
		} catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return handle;
	}

	/**
	 * Retrieve Team Display Name.
	 * <p>
	 * Notes: only if Mode = 0 or 2.
	 * 
	 * @return The current Team Display Name
	 */
	public WrappedChatComponent getDisplayName() {
		return scoreboardTeam.getChatComponents().read(0);
	}

	/**
	 * Set Team Display Name.
	 * 
	 * @param value - new value.
	 */
	public void setDisplayName(WrappedChatComponent value) {
		scoreboardTeam.getChatComponents().write(0, value);
	}

	/**
	 * Retrieve Team Prefix.
	 * <p>
	 * Notes: only if Mode = 0 or 2. Displayed before the players' name that are
	 * part of this team.
	 * 
	 * @return The current Team Prefix
	 */
	public WrappedChatComponent getPrefix() {
		return scoreboardTeam.getChatComponents().read(1);
	}

	/**
	 * Set Team Prefix.
	 * 
	 * @param value - new value.
	 */
	public void setPrefix(WrappedChatComponent value) {
		scoreboardTeam.getChatComponents().write(1, value);
	}

	/**
	 * Retrieve Team Suffix.
	 * <p>
	 * Notes: only if Mode = 0 or 2. Displayed after the players' name that are
	 * part of this team.
	 * 
	 * @return The current Team Suffix
	 */
	public WrappedChatComponent getSuffix() {
		return scoreboardTeam.getChatComponents().read(2);
	}

	/**
	 * Set Team Suffix.
	 * 
	 * @param value - new value.
	 */
	public void setSuffix(WrappedChatComponent value) {
		scoreboardTeam.getChatComponents().write(2, value);
	}

	/**
	 * Retrieve Name Tag Visibility.
	 * <p>
	 * Notes: only if Mode = 0 or 2. always, hideForOtherTeams, hideForOwnTeam,
	 * never.
	 * 
	 * @return The current Name Tag Visibility
	 */
	public String getNameTagVisibility() {
		return scoreboardTeam.getStrings().read(1);
	}

	/**
	 * Set Name Tag Visibility.
	 * 
	 * @param value - new value.
	 */
	public void setNameTagVisibility(String value) {
		scoreboardTeam.getStrings().write(1, value);
	}

	/**
	 * Retrieve Color.
	 * <p>
	 * Notes: only if Mode = 0 or 2. Same as Chat colors.
	 * 
	 * @return The current Color
	 */
	public ChatColor getColor() {
		return scoreboardTeam.getEnumModifier(ChatColor.class, MinecraftReflection.getMinecraftClass("EnumChatFormat")).read(0);
	}

	/**
	 * Set Color.
	 * 
	 * @param value - new value.
	 */
	public void setColor(ChatColor value) {
		scoreboardTeam.getEnumModifier(ChatColor.class, MinecraftReflection.getMinecraftClass("EnumChatFormat")).write(0, value);
	}

	/**
	 * Get the collision rule.
	 * Notes: only if Mode = 0 or 2. always, pushOtherTeams, pushOwnTeam, never.
	 * @return The current collision rule
	 */
	public String getCollisionRule() {
		return scoreboardTeam.getStrings().read(2);
	}

	/**
	 * Sets the collision rule.
	 * @param value - new value.
	 */
	public void setCollisionRule(String value) {
		scoreboardTeam.getStrings().write(2, value);
	}

	/**
	 * Retrieve pack option data. Pack data is calculated as follows:
	 * 
	 * <pre>
	 * <code>
	 * int data = 0;
	 * if (team.allowFriendlyFire()) {
	 *     data |= 1;
	 * }
	 * if (team.canSeeFriendlyInvisibles()) {
	 *     data |= 2;
	 * }
	 * </code>
	 * </pre>
	 * 
	 * @return The current pack option data
	 */
	public int getPackOptionData() {
		return scoreboardTeam.getIntegers().read(1);
	}

	/**
	 * Set pack option data.
	 * 
	 * @param value - new value
	 * @see #getPackOptionData()
	 */
	public void setPackOptionData(int value) {
		scoreboardTeam.getIntegers().write(1, value);
	}
}
