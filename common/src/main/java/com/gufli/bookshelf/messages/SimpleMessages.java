/*
 * This file is part of KingdomCraft.
 *
 * KingdomCraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KingdomCraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with KingdomCraft. If not, see <https://www.gnu.org/licenses/>.
 */

package com.gufli.bookshelf.messages;

import com.gufli.bookshelf.api.config.Configuration;
import com.gufli.bookshelf.api.entity.ShelfCommandSender;
import com.gufli.bookshelf.api.messages.Messages;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.text.StringEscapeUtils.unescapeJava;


public class SimpleMessages implements Messages {

	private String prefix;
	private final Map<String, String> messages = new HashMap<>();

	public SimpleMessages() {}

	public SimpleMessages(Configuration... configs) {
		load(configs);
	}

	public void load(Configuration... configs) {
		for ( Configuration config : configs ) {
			for ( String key : config.getKeys(false) ) {
				messages.put(key, config.getString(key));
			}
		}
		setPrefix(messages.getOrDefault("prefix", ""));
	}

	@Override
	public final String getPrefix() {
		return prefix;
	}

	@Override
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	protected final boolean isEmpty(String name) {
		return messages.containsKey(name) && messages.get(name).trim().equals("");
	}

	@Override
	public String getMessage(String name) {
		if ( name == null ) {
			return null;
		}
		String msg = messages.get(name);
		if ( msg == null ) {
			return null;
		}
		return unescapeJava(msg);
	}

	@Override
	public String getMessage(String name, String... placeholders) {
		String message = getMessage(name);
		if ( message == null ) return null;

		for ( int i = 0; i < placeholders.length; i++ ) {
			message = message.replace("{" + i + "}",
					placeholders[i] == null ? "" : placeholders[i]);
		}

		return message;
	}

	@Override
	public void send(ShelfCommandSender sender, String name, String... placeholders) {
		if ( isEmpty(name) ) return;
		sender.sendMessage(prefix + getMessage(name, placeholders));
	}

}
