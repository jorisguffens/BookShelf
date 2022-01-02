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

package com.gufli.bookshelf.api.messages;

import com.gufli.bookshelf.api.config.Configuration;
import com.gufli.bookshelf.api.entity.ShelfCommandSender;
import com.gufli.bookshelf.api.entity.ShelfPlayer;

import java.util.HashMap;
import java.util.Map;

public class InternationalizedMessages implements Messages {

    private String defaultLanguage = null;
    private final Map<String, Messages> messages = new HashMap<>();

    public InternationalizedMessages() {}

    public void load(String language, Configuration... configs) {
		language = language.toLowerCase();
        messages.put(language, new SimpleMessages(configs));
        if (defaultLanguage == null) {
            defaultLanguage = language;
        }
    }

    public void changeDefaultLanguage(String language) {
        if (!messages.containsKey(language)) {
            throw new IllegalArgumentException("Cannot set default language to an unregistered language.");
        }
        this.defaultLanguage = language;
    }

	private Messages msgByLocale(String locale) {
		locale = locale.toLowerCase();
		if ( messages.containsKey(locale) ) {
			return msgByLanguage(locale);
		}
		return msgByLanguage(locale.split("[\\-_.]")[0]);
	}

    private Messages msgByLanguage(String language) {
        if ( language != null && messages.containsKey(language) ) {
			return messages.get(language);
		}
		if (defaultLanguage == null ) {
			throw new IllegalArgumentException("The default language is not setup.");
		}
        return messages.get(defaultLanguage);
    }

    public final String prefix(String language) {
        return msgByLanguage(language).prefix();
    }

    @Override
    public final String prefix() {
        return prefix(defaultLanguage);
    }

    @Override
    public void changePrefix(String prefix) {
        msgByLanguage(defaultLanguage).changePrefix(prefix);
    }

    public String get(String language, String name) {
        return msgByLanguage(language).get(name);
    }

    @Override
    public String get(String name) {
        return get(defaultLanguage, name);
    }

    public String get(String language, String name, String... placeholders) {
        return msgByLanguage(language).get(name, placeholders);
    }

    @Override
    public String get(String name, String... placeholders) {
        return get(defaultLanguage, name, placeholders);
    }

    @Override
    public void send(ShelfCommandSender sender, String name, String... placeholders) {
        if (sender instanceof ShelfPlayer player) {
            Messages msg = null;
            if ( player.has("LANGUAGE") ) {
                msg = messages.get(player.get("LANGUAGE", String.class));
            }
            if ( msg == null ) {
                msg = msgByLocale(player.locale());
            }
			msg.send(sender, name, placeholders);
            return;
        }

		msgByLanguage(defaultLanguage).send(sender, name, placeholders);
    }

}
