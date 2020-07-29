/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2020 PlaceholderAPI Team
 *
 * PlaceholderAPI free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlaceholderAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package me.clip.placeholderapi;

import com.google.common.collect.ImmutableSet;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import me.clip.placeholderapi.replacer.CharsReplacer;
import me.clip.placeholderapi.replacer.Replacer;
import me.clip.placeholderapi.replacer.Replacer.Closure;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class PlaceholderAPI
{

	private static final Replacer REPLACER_PERCENT = new CharsReplacer(Closure.PERCENT);
	private static final Replacer REPLACER_BRACKET = new CharsReplacer(Closure.BRACKET);


	private static final Pattern PERCENT_PLACEHOLDER_PATTERN = Pattern.compile("%((?<identifier>[a-zA-Z0-9]+)_)(?<parameters>[^%]+)%");
	private static final Pattern BRACKET_PLACEHOLDER_PATTERN = Pattern.compile("\\{((?<identifier>[a-zA-Z0-9]+)_)(?<parameters>[^{}]+)}");
	private static final Pattern RELATED_PLACEHOLDER_PATTERN = Pattern.compile("%rel_((?<identifier>[a-zA-Z0-9]+)_)(?<parameters>[^%]+)%");


	private PlaceholderAPI()
	{
	}


	/**
	 * Translates all placeholders into their corresponding values.
	 * <br>The pattern of a valid placeholder is {@literal %<identifier>_<params>%}.
	 *
	 * @param player Player to parse the placeholders against
	 * @param text   Text to set the placeholder values in
	 * @return String containing all translated placeholders
	 */
	@NotNull
	public static String setPlaceholders(@Nullable final OfflinePlayer player, @NotNull final String text)
	{
		return REPLACER_PERCENT.apply(text, player, PlaceholderAPIPlugin.getInstance().getLocalExpansionManager()::getExpansion);
	}

	/**
	 * Translates all placeholders into their corresponding values.
	 * <br>The pattern of a valid placeholder is {@literal %<identifier>_<params>%}.
	 *
	 * @param player Player to parse the placeholders against
	 * @param text   List of Strings to set the placeholder values in
	 * @return String containing all translated placeholders
	 */
	@NotNull
	public static List<String> setPlaceholders(@Nullable final OfflinePlayer player, @NotNull final List<@NotNull String> text)
	{
		return text.stream().map(line -> setPlaceholders(player, line)).collect(Collectors.toList());
	}


	/**
	 * Translates all placeholders into their corresponding values.
	 * <br>The pattern of a valid placeholder is {@literal {<identifier>_<params>}}.
	 *
	 * @param player Player to parse the placeholders against
	 * @param text   Text to set the placeholder values in
	 * @return String containing all translated placeholders
	 */
	@NotNull
	public static String setBracketPlaceholders(@Nullable final OfflinePlayer player, @NotNull final String text)
	{
		return REPLACER_BRACKET.apply(text, player, PlaceholderAPIPlugin.getInstance().getLocalExpansionManager()::getExpansion);
	}

	/**
	 * Translates all placeholders into their corresponding values.
	 * <br>The pattern of a valid placeholder is {@literal {<identifier>_<params>}}.
	 *
	 * @param player Player to parse the placeholders against
	 * @param text   List of Strings to set the placeholder values in
	 * @return String containing all translated placeholders
	 */
	@NotNull
	public static List<String> setBracketPlaceholders(@Nullable final OfflinePlayer player, @NotNull final List<@NotNull String> text)
	{
		return text.stream().map(line -> setBracketPlaceholders(player, line)).collect(Collectors.toList());
	}


	/**
	 * set relational placeholders in the text specified placeholders are matched with the pattern
	 * {@literal %<rel_(identifier)_(params)>%} when set with this method
	 *
	 * @param one  First player to compare
	 * @param two  Second player to compare
	 * @param text Text to parse the placeholders in
	 * @return The text containing the parsed relational placeholders
	 */
	@NotNull
	public static String setRelationalPlaceholders(@Nullable final Player one, @Nullable final Player two, @NotNull String text)
	{
		if (PlaceholderAPIPlugin.getInstance().getLocalExpansionManager().getExpansionsCount() == 0)
		{
			return Msg.color(text);
		}

		final Matcher matcher = RELATED_PLACEHOLDER_PATTERN.matcher(text);
		if (!matcher.find())
		{
			return text;
		}

		final StringBuffer builder = new StringBuffer();

		do
		{
			final String identifier = matcher.group("identifier");
			final String parameters = matcher.group("parameters");

			final PlaceholderExpansion expansion = PlaceholderAPIPlugin.getInstance().getLocalExpansionManager().getExpansion(identifier);
			if (!(expansion instanceof Relational))
			{
				continue;
			}

			final String requested = ((Relational) expansion).onPlaceholderRequest(one, two, parameters);
			matcher.appendReplacement(builder, requested != null ? requested : matcher.group(0));
		}
		while (matcher.find());

		return Msg.color(text);
	}

	/**
	 * Translate placeholders in the provided List based on the relation of the two provided players.
	 * <br>The pattern of a valid placeholder is {@literal %rel_<identifier>_<param>%}.
	 *
	 * @param one  Player to compare
	 * @param two  Player to compare
	 * @param text text to parse the placeholder values to
	 * @return The text containing the parsed relational placeholders
	 */
	@NotNull
	public static List<String> setRelationalPlaceholders(@Nullable final Player one, @Nullable final Player two, @NotNull final List<String> text)
	{
		return text.stream().map(line -> setRelationalPlaceholders(one, two, line)).collect(Collectors.toList());
	}


	/**
	 * Check if a specific placeholder identifier is currently registered
	 *
	 * @param identifier The identifier to check
	 * @return true if identifier is already registered
	 */
	public static boolean isRegistered(@NotNull final String identifier)
	{
		return PlaceholderAPIPlugin.getInstance().getLocalExpansionManager().findExpansionByIdentifier(identifier).isPresent();
	}


	/**
	 * Get all registered placeholder identifiers
	 *
	 * @return All registered placeholder identifiers
	 */
	@NotNull
	public static Set<String> getRegisteredIdentifiers()
	{
		return ImmutableSet.copyOf(PlaceholderAPIPlugin.getInstance().getLocalExpansionManager().getIdentifiers());
	}


	/**
	 * Check if a String contains any PlaceholderAPI placeholders ({@literal %<identifier>_<params>%}).
	 *
	 * @param text String to check
	 * @return true if String contains any registered placeholder identifiers, false otherwise
	 */
	public static boolean containsPlaceholders(@NotNull final String text)
	{
		return PERCENT_PLACEHOLDER_PATTERN.matcher(text).find();
	}

	/**
	 * Check if a String contains any PlaceholderAPI bracket placeholders ({@literal {<identifier>_<params>}}).
	 *
	 * @param text String to check
	 * @return true if String contains any registered placeholder identifiers, false otherwise
	 */
	public static boolean containsBracketPlaceholders(@NotNull final String text)
	{
		return BRACKET_PLACEHOLDER_PATTERN.matcher(text).find();
	}

	/**
	 * Check if a String contains any PlaceholderAPI relational placeholders ({@literal %rel_<identifier>_<param>%}).
	 *
	 * @param text String to check
	 * @return true if String contains any registered placeholder identifiers, false otherwise
	 */
	public static boolean containsRelationalPlaceholders(@NotNull final String text)
	{
		return RELATED_PLACEHOLDER_PATTERN.matcher(text).find();
	}

}
