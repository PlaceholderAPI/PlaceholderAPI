/*
 *
 * PlaceholderAPI
 * Copyright (C) 2019 Ryan McCarthy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *
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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class PlaceholderAPI
{

	private static final Replacer REPLACER_PERCENT = new CharsReplacer(Closure.PERCENT);
	private static final Replacer REPLACER_BRACKET = new CharsReplacer(Closure.BRACKET);


	@Deprecated
	private static final Pattern PLACEHOLDER_PATTERN            = Pattern.compile("[%]([^%]+)[%]");
	@Deprecated
	private static final Pattern BRACKET_PLACEHOLDER_PATTERN    = Pattern.compile("[{]([^{}]+)[}]");
	@Deprecated
	private static final Pattern RELATIONAL_PLACEHOLDER_PATTERN = Pattern.compile("[%](rel_)([^%]+)[%]");


	private PlaceholderAPI()
	{
	}


	// === Current API ===

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
	 * Get map of registered placeholders
	 *
	 * @return Copy of the internal placeholder map
	 */
	@NotNull
	@Deprecated
	public static Map<String, PlaceholderHook> getPlaceholders()
	{
		throw new UnsupportedOperationException("PlaceholderAPI no longer provides a view of the placeholder's map!\n" +
												"Use: PlaceholderAPIPlugin.getInstance().getLocalExpansionManager().findExpansionByIdentifier(String)");
	}


	// === Deprecated API ===

	/**
	 * Translates all placeholders into their corresponding values.
	 * <br>You set the pattern yourself through this method.
	 *
	 * @param player   Player to parse the placeholders against
	 * @param text     Text to set the placeholder values in
	 * @param pattern  The pattern to match placeholders to. Capture group 1 must contain an underscore separating the
	 *                 identifier from the params
	 * @param colorize If color codes (&[0-1a-fk-o]) should be translated
	 * @return The text containing the parsed placeholders
	 * @deprecated Please use {@link #setPlaceholders(OfflinePlayer, String)} instead
	 */
	@NotNull
	@Deprecated
	public static String setPlaceholders(@Nullable final OfflinePlayer player, @NotNull final String text, @NotNull final Pattern pattern, final boolean colorize)
	{
		return setPlaceholders(player, text);
	}

	/**
	 * Translates all placeholders into their corresponding values.
	 * <br>You set the pattern yourself through this method.
	 *
	 * @param player   Player to parse the placeholders against
	 * @param text     List of Strings to set the placeholder values in
	 * @param pattern  The pattern to match placeholders to. Capture group 1 must contain an underscore separating the
	 *                 identifier from the params
	 * @param colorize If color codes (&[0-1a-fk-o]) should be translated
	 * @return String containing all translated placeholders
	 * @deprecated Please use {@link #setPlaceholders(OfflinePlayer, List)} instead
	 */
	@NotNull
	@Deprecated
	public static List<String> setPlaceholders(@Nullable final OfflinePlayer player, @NotNull final List<String> text, @NotNull final Pattern pattern, final boolean colorize)
	{
		return setPlaceholders(player, text);
	}

	@Deprecated
	public static Set<PlaceholderExpansion> getExpansions()
	{
		Set<PlaceholderExpansion> set = getPlaceholders().values().stream()
														 .filter(PlaceholderExpansion.class::isInstance).map(PlaceholderExpansion.class::cast)
														 .collect(Collectors.toCollection(HashSet::new));

		return ImmutableSet.copyOf(set);
	}

	/**
	 * Check if a String contains any PlaceholderAPI placeholders ({@literal %<identifier>_<params>%}).
	 *
	 * @param text String to check
	 * @return true if String contains any registered placeholder identifiers, false otherwise
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public static boolean containsPlaceholders(String text)
	{
		return text != null && PLACEHOLDER_PATTERN.matcher(text).find();
	}

	/**
	 * Check if a String contains any PlaceholderAPI bracket placeholders ({@literal {<identifier>_<params>}}).
	 *
	 * @param text String to check
	 * @return true if String contains any registered placeholder identifiers, false otherwise
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public static boolean containsBracketPlaceholders(String text)
	{
		return text != null && BRACKET_PLACEHOLDER_PATTERN.matcher(text).find();
	}

	/**
	 * Translates all placeholders into their corresponding values.
	 * <br>The pattern of a valid placeholder is {@literal {<identifier>_<params>}}.
	 *
	 * @param player   Player to parse the placeholders against
	 * @param text     List of Strings to set the placeholder values in
	 * @param colorize If color codes (&[0-1a-fk-o]) should be translated
	 * @return String containing all translated placeholders
	 * @deprecated Use {@link #setPlaceholders(OfflinePlayer, List)} instead.
	 */
	@Deprecated
	public static List<String> setBracketPlaceholders(OfflinePlayer player, List<String> text, boolean colorize)
	{
		return setPlaceholders(player, text, BRACKET_PLACEHOLDER_PATTERN, colorize);
	}

	/**
	 * Translates all placeholders into their corresponding values.
	 * <br>The pattern of a valid placeholder is {@literal %<identifier>_<params>%}.
	 *
	 * @param player   Player to parse the placeholders against
	 * @param text     List of Strings to set the placeholder values in
	 * @param colorize If color codes (&[0-1a-fk-o]) should be translated
	 * @return String containing all translated placeholders
	 * @deprecated Use {@link #setPlaceholders(OfflinePlayer, List)} instead.
	 */
	@Deprecated
	public static List<String> setPlaceholders(OfflinePlayer player, List<String> text, boolean colorize)
	{
		return setPlaceholders(player, text, PLACEHOLDER_PATTERN, colorize);
	}

	/**
	 * Translates all placeholders into their corresponding values.
	 * <br>You set the pattern yourself through this method.
	 *
	 * @param player  Player to parse the placeholders against
	 * @param text    List of Strings to set the placeholder values in
	 * @param pattern The pattern to match placeholders to. Capture group 1 must contain an underscore separating the
	 *                identifier from the params
	 * @return String containing all translated placeholders
	 * @deprecated Use {@link #setPlaceholders(OfflinePlayer, List)} instead.
	 */
	@Deprecated
	public static List<String> setPlaceholders(OfflinePlayer player, List<String> text, Pattern pattern)
	{
		return setPlaceholders(player, text, pattern, true);
	}


	/**
	 * Translates all placeholders into their corresponding values.
	 * <br>The pattern of a valid placeholder is {@literal {<identifier>_<params>}}
	 *
	 * @param player   Player to parse the placeholders against
	 * @param text     Text to set the placeholder values in
	 * @param colorize If color codes (&[0-1a-fk-o]) should be translated
	 * @return String containing all translated placeholders
	 * @deprecated Use {@link #setPlaceholders(OfflinePlayer, String)} instead.
	 */
	@Deprecated
	public static String setBracketPlaceholders(OfflinePlayer player, String text, boolean colorize)
	{
		return setPlaceholders(player, text, BRACKET_PLACEHOLDER_PATTERN, colorize);
	}

	/**
	 * Translates all placeholders into their corresponding values.
	 * <br>The pattern of a valid placeholder is {@literal %<identifier>_<params>%}.
	 *
	 * @param player   Player to parse the placeholder against
	 * @param text     Text to parse the placeholders in
	 * @param colorize If color codes (&[0-1a-fk-o]) should be translated
	 * @return The text containing the parsed placeholders
	 * @deprecated Use {@link #setPlaceholders(OfflinePlayer, String)} instead.
	 */
	@Deprecated
	public static String setPlaceholders(OfflinePlayer player, String text, boolean colorize)
	{
		return setPlaceholders(player, text, PLACEHOLDER_PATTERN, colorize);
	}

	/**
	 * Translates all placeholders into their corresponding values.
	 * <br>You set the pattern yourself through this method.
	 *
	 * @param player  Player to parse the placeholders against
	 * @param text    Text to set the placeholder values in
	 * @param pattern The pattern to match placeholders to. Capture group 1 must contain an underscore separating the
	 *                identifier from the params
	 * @return The text containing the parsed placeholders
	 * @deprecated Use {@link #setPlaceholders(OfflinePlayer, String)} instead.
	 */
	@Deprecated
	public static String setPlaceholders(OfflinePlayer player, String text, Pattern pattern)
	{
		return setPlaceholders(player, text, pattern, true);
	}

	/**
	 * Translate placeholders in the provided List based on the relation of the two provided players.
	 * <br>The pattern of a valid placeholder is {@literal %rel_<identifier>_<param>%}.
	 *
	 * @param one  Player to compare
	 * @param two  Player to compare
	 * @param text text to parse the placeholder values to
	 * @return The text containing the parsed relational placeholders
	 * @deprecated Use {@link #setPlaceholders(OfflinePlayer, List)} instead.
	 */
	@Deprecated
	public static List<String> setRelationalPlaceholders(Player one, Player two, List<String> text)
	{
		return setRelationalPlaceholders(one, two, text, true);
	}

	/**
	 * Translate placeholders in the provided list based on the relation of the two provided players.
	 * <br>The pattern of a valid placeholder is {@literal %rel_<identifier>_<params>%}.
	 *
	 * @param one      First player to compare
	 * @param two      Second player to compare
	 * @param text     Text to parse the placeholders in
	 * @param colorize If color codes (&[0-1a-fk-o]) should be translated
	 * @return The text containing the parsed relational placeholders
	 * @deprecated Use {@link #setPlaceholders(OfflinePlayer, List)} instead.
	 */
	@Deprecated
	public static List<String> setRelationalPlaceholders(Player one, Player two, List<String> text, boolean colorize)
	{
		if (text == null)
		{
			return null;
		}

		return text.stream()
				   .map(line -> setRelationalPlaceholders(one, two, line, colorize))
				   .collect(Collectors.toList());
	}

	/**
	 * set relational placeholders in the text specified placeholders are matched with the pattern
	 * %<rel_(identifier)_(params)>% when set with this method
	 *
	 * @param one  First player to compare
	 * @param two  Second player to compare
	 * @param text Text to parse the placeholders in
	 * @return The text containing the parsed relational placeholders
	 * @deprecated Use {@link #setPlaceholders(OfflinePlayer, String)} instead.
	 */
	@Deprecated
	public static String setRelationalPlaceholders(Player one, Player two, String text)
	{
		return setRelationalPlaceholders(one, two, text, true);
	}

	/**
	 * set relational placeholders in the text specified placeholders are matched with the pattern
	 * %<rel_(identifier)_(params)>% when set with this method
	 *
	 * @param one      Player to compare
	 * @param two      Player to compare
	 * @param text     Text to parse the placeholders in
	 * @param colorize If color codes (&[0-1a-fk-o]) should be translated
	 * @return The text containing the parsed relational placeholders
	 * @deprecated Use {@link #setPlaceholders(OfflinePlayer, String)} instead.
	 */
	@Deprecated
	@SuppressWarnings("DuplicatedCode")
	public static String setRelationalPlaceholders(Player one, Player two, String text, boolean colorize)
	{
		if (text == null)
		{
			return null;
		}

		if (PlaceholderAPIPlugin.getInstance().getLocalExpansionManager().getExpansionsCount() == 0)
		{
			return colorize ? Msg.color(text) : text;
		}

		final Matcher                      matcher = RELATIONAL_PLACEHOLDER_PATTERN.matcher(text);
		final Map<String, PlaceholderHook> hooks   = getPlaceholders();

		while (matcher.find())
		{
			final String format = matcher.group(2);
			final int    index  = format.indexOf("_");

			if (index <= 0 || index >= format.length())
			{
				continue;
			}

			String                identifier = format.substring(0, index).toLowerCase();
			String                params     = format.substring(index + 1);
			final PlaceholderHook hook       = hooks.get(identifier);

			if (!(hook instanceof Relational))
			{
				continue;
			}

			final String value = ((Relational) hook).onPlaceholderRequest(one, two, params);

			if (value != null)
			{
				text = text.replaceAll(Pattern.quote(matcher.group()), Matcher.quoteReplacement(value));
			}
		}

		return colorize ? Msg.color(text) : text;
	}

	/**
	 * Gets the placeholder pattern for the default placeholders.
	 *
	 * @return The pattern for {@literal %<identifier>_<params>%}
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public static Pattern getPlaceholderPattern()
	{
		return PLACEHOLDER_PATTERN;
	}

	/**
	 * Gets the placeholder pattern for the bracket placeholders.
	 *
	 * @return The pattern for {@literal {<identifier>_<params>}}
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public static Pattern getBracketPlaceholderPattern()
	{
		return BRACKET_PLACEHOLDER_PATTERN;
	}

	/**
	 * Gets the placeholder pattern for the relational placeholders.
	 *
	 * @return The pattern for {@literal %rel_<identifier>_<params>%}
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public static Pattern getRelationalPlaceholderPattern()
	{
		return RELATIONAL_PLACEHOLDER_PATTERN;
	}

	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public static Set<String> getRegisteredPlaceholderPlugins()
	{
		return getRegisteredIdentifiers();
	}

	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public static Set<String> getExternalPlaceholderPlugins()
	{
		return null;
	}

	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public static String setPlaceholders(Player player, String text)
	{
		return setPlaceholders(((OfflinePlayer) player), text);
	}

	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public static String setPlaceholders(Player player, String text, boolean colorize)
	{
		return setPlaceholders(player, text, PLACEHOLDER_PATTERN, colorize);
	}

	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public static List<String> setPlaceholders(Player player, List<String> text)
	{
		return setPlaceholders(player, text, PLACEHOLDER_PATTERN, true);
	}

	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public static List<String> setPlaceholders(Player player, List<String> text, boolean colorize)
	{
		return setPlaceholders(player, text, PLACEHOLDER_PATTERN, colorize);
	}

	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public static String setBracketPlaceholders(Player player, String text)
	{
		return setPlaceholders(player, text, BRACKET_PLACEHOLDER_PATTERN, true);
	}

	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public static String setBracketPlaceholders(Player player, String text, boolean colorize)
	{
		return setPlaceholders(player, text, BRACKET_PLACEHOLDER_PATTERN, colorize);
	}

	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public static List<String> setBracketPlaceholders(Player player, List<String> text)
	{
		return setPlaceholders(player, text, BRACKET_PLACEHOLDER_PATTERN, true);
	}

	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public static List<String> setBracketPlaceholders(Player player, List<String> text, boolean colorize)
	{
		return setPlaceholders(player, text, BRACKET_PLACEHOLDER_PATTERN, colorize);
	}

}
