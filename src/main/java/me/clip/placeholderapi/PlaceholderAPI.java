/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2026 PlaceholderAPI Team
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

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import me.clip.placeholderapi.expansion.manager.LocalExpansionManager;
import me.clip.placeholderapi.replacer.CharsReplacer;
import me.clip.placeholderapi.replacer.Replacer;
import me.clip.placeholderapi.replacer.Replacer.Closure;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public final class PlaceholderAPI {

    private static final Replacer REPLACER_PERCENT = new CharsReplacer(Closure.PERCENT);
    private static final Replacer REPLACER_BRACKET = new CharsReplacer(Closure.BRACKET);

    static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("[%]([^%]+)[%]");
    static final Pattern BRACKET_PLACEHOLDER_PATTERN = Pattern.compile("[{]([^{}]+)[}]");
    static final Pattern RELATIONAL_PLACEHOLDER_PATTERN = Pattern
            .compile("[%](rel_)([^%]+)[%]");


    private PlaceholderAPI() {
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
    public static String setPlaceholders(final OfflinePlayer player,
                                         @NotNull final String text) {
        return REPLACER_PERCENT.apply(text, player,
                PlaceholderAPIPlugin.getInstance().getLocalExpansionManager()::getExpansion);
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
    public static List<String> setPlaceholders(final OfflinePlayer player,
                                               @NotNull final List<String> text) {
        final List<String> result = new ArrayList<>(text.size());
        for (final String line : text) {
            result.add(setPlaceholders(player, line));
        }
        return result;
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
    public static String setPlaceholders(final Player player, @NotNull String text) {
        return setPlaceholders(((OfflinePlayer) player), text);
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
    public static List<String> setPlaceholders(final Player player, @NotNull List<@NotNull String> text) {
        return setPlaceholders(((OfflinePlayer) player), text);
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
    public static String setBracketPlaceholders(final OfflinePlayer player,
                                                @NotNull final String text) {
        return REPLACER_BRACKET.apply(text, player,
                PlaceholderAPIPlugin.getInstance().getLocalExpansionManager()::getExpansion);
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
    public static List<@NotNull String> setBracketPlaceholders(final OfflinePlayer player,
                                                               @NotNull final List<@NotNull String> text) {
        final List<String> result = new ArrayList<>(text.size());
        for (final String line : text) {
            result.add(setBracketPlaceholders(player, line));
        }
        return result;
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
    public static String setBracketPlaceholders(Player player, @NotNull String text) {
        return setBracketPlaceholders((OfflinePlayer) player, text);
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
    public static List<String> setBracketPlaceholders(Player player, @NotNull List<String> text) {
        return setBracketPlaceholders((OfflinePlayer) player, text);
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
    public static String setRelationalPlaceholders(final Player one, final Player two, @NotNull String text) {
        final Matcher matcher = RELATIONAL_PLACEHOLDER_PATTERN.matcher(text);

        while (matcher.find()) {
            final String format = matcher.group(2);
            final int index = format.indexOf('_');

            if (index <= 0) {
                continue;
            }

            String identifier = format.substring(0, index).toLowerCase(Locale.ROOT);
            String params = format.substring(index + 1);
            final PlaceholderExpansion expansion = PlaceholderAPIPlugin.getInstance()
                    .getLocalExpansionManager().getExpansion(identifier);

            if (!(expansion instanceof Relational)) {
                continue;
            }

            final String value = ((Relational) expansion).onPlaceholderRequest(one, two, params);

            if (value != null) {
                text = text.replaceAll(Pattern.quote(matcher.group()), Matcher.quoteReplacement(value));
            }
        }

        return text;
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
    public static List<String> setRelationalPlaceholders(final Player one, final Player two, final @NotNull List<String> text) {
        final List<String> result = new ArrayList<>(text.size());
        for (final String line : text) {
            result.add(setRelationalPlaceholders(one, two, line));
        }
        return result;
    }

    /**
     * Check if a specific placeholder identifier is currently registered
     *
     * @param identifier The identifier to check
     * @return true if identifier is already registered
     */
    public static boolean isRegistered(@NotNull final String identifier) {
        return PlaceholderAPIPlugin.getInstance().getLocalExpansionManager()
                .findExpansionByIdentifier(identifier).isPresent();
    }

    /**
     * Get all registered placeholder identifiers
     *
     * @return A Set of type String containing the identifiers of all registered expansions.
     */
    @NotNull
    public static Set<String> getRegisteredIdentifiers() {
        return PlaceholderAPIPlugin.getInstance().getLocalExpansionManager().getIdentifiers();
    }

    /**
     * Get the normal placeholder pattern.
     *
     * @return Regex Pattern of {@literal [%]([^%]+)[%]}
     */
    public static Pattern getPlaceholderPattern() {
        return PLACEHOLDER_PATTERN;
    }

    /**
     * Get the bracket placeholder pattern.
     *
     * @return Regex Pattern of {@literal [{]([^{}]+)[}]}
     */
    public static Pattern getBracketPlaceholderPattern() {
        return BRACKET_PLACEHOLDER_PATTERN;
    }

    /**
     * Get the relational placeholder pattern.
     *
     * @return Regex Pattern of {@literal [%](rel_)([^%]+)[%]}
     */
    public static Pattern getRelationalPlaceholderPattern() {
        return RELATIONAL_PLACEHOLDER_PATTERN;
    }

    /**
     * Check if a String contains any PlaceholderAPI placeholders ({@literal
     * %<identifier>_<params>%}).
     *
     * @param text String to check
     * @return true if String contains any matches to the normal placeholder pattern, false otherwise
     */
    public static boolean containsPlaceholders(final String text) {
        if (text == null) {
            return false;
        }
        final int firstPercent = text.indexOf('%');
        if (firstPercent == -1) {
            return false;
        }
        return text.indexOf('%', firstPercent + 1) != -1;
    }

    /**
     * Check if a String contains any PlaceholderAPI bracket placeholders ({@literal
     * {<identifier>_<params>}}).
     *
     * @param text String to check
     * @return true if String contains any matches to the bracket placeholder pattern, false otherwise
     */
    public static boolean containsBracketPlaceholders(final String text) {
        if (text == null) {
            return false;
        }
        final int openBracket = text.indexOf('{');
        if (openBracket == -1) {
            return false;
        }
        return text.indexOf('}', openBracket + 1) != -1;
    }

    // === Deprecated API ===

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.13.0")
    public static boolean registerExpansion(PlaceholderExpansion expansion) {
        return expansion.register();
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.13.0")
    public static boolean unregisterExpansion(PlaceholderExpansion expansion) {
        return expansion.unregister();
    }

    /**
     * Get map of registered placeholders
     *
     * @return Map of registered placeholders
     * @deprecated Use {@link LocalExpansionManager#getExpansions()} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.13.0")
    public static Map<String, PlaceholderHook> getPlaceholders() {
        return PlaceholderAPIPlugin.getInstance().getLocalExpansionManager()
                .getExpansions().stream()
                .collect(Collectors.toMap(PlaceholderExpansion::getIdentifier, ex -> ex));
    }

    /**
     * @param plugin          The Plugin to register with this {@link PlaceholderHook}
     * @param placeholderHook The {@link PlaceholderHook} to register
     * @return always false
     * @deprecated Please use {@link PlaceholderExpansion} to
     * register placeholders instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.13.0")
    public static boolean registerPlaceholderHook(Plugin plugin, PlaceholderHook placeholderHook) {
        Msg.warn("Nag author(s) %s of plugin %s about their usage of the deprecated PlaceholderHook"
                        + " class! This class will be removed in v2.13.0!", plugin.getDescription().getAuthors(),
                plugin.getName());
        return false;
    }

    /**
     * @param identifier      The identifier to use for the {@link PlaceholderHook}
     * @param placeholderHook The {@link PlaceholderHook} to register
     * @return always false
     * @deprecated Please use {@link PlaceholderExpansion} to
     * register placeholders instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.13.0")
    public static boolean registerPlaceholderHook(String identifier,
                                                  PlaceholderHook placeholderHook) {
        Msg.warn("%s is attempting to register placeholders via deprecated PlaceholderHook class."
                + " This class is no longer supported and will be removed in v2.13.0!", identifier);
        return false;
    }

    /**
     * @param plugin The plugin to unregister
     * @return always false
     * @deprecated Please use {@link PlaceholderExpansion} to
     * unregister placeholders instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.13.0")
    public static boolean unregisterPlaceholderHook(Plugin plugin) {
        Msg.warn("Nag author(s) %s of plugin %s about their usage of the PlaceholderAPI class."
                + " This way of unregistering placeholders is no longer supported and will be removed"
                + " in v2.13.0!", plugin.getDescription().getAuthors(), plugin.getName());
        return false;
    }

    /**
     * @param identifier The identifier to unregister
     * @return always false
     * @deprecated Please use {@link PlaceholderExpansion} to
     * unregister placeholders instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.13.0")
    public static boolean unregisterPlaceholderHook(String identifier) {
        Msg.warn("%s is attempting to unregister placeholders via PlaceholderAPI class."
                + " This way of unregistering placeholders is no longer supported and will be removed"
                + " in v2.13.0!", identifier);
        return false;
    }

    /**
     * @return Set of registered identifiers
     * @deprecated Will be removed in a future release.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.13.0")
    public static Set<String> getRegisteredPlaceholderPlugins() {
        return getRegisteredIdentifiers();
    }

    /**
     * @return always null
     * @deprecated Will be removed in a future release.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.13.0")
    public static Set<String> getExternalPlaceholderPlugins() {
        return null;
    }

    /**
     * @param player   The offline player to parse the placeholders against
     * @param text     The text to parse
     * @param pattern  The Pattern to use
     * @param colorize If PlaceholderAPI should also parse color codes
     * @return String with the parsed placeholders
     * @deprecated Please use {@link #setPlaceholders(OfflinePlayer, String)} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.13.0")
    public static String setPlaceholders(OfflinePlayer player,
                                         String text, Pattern pattern, boolean colorize) {
        return setPlaceholders(player, text);
    }

    /**
     * @param player   The offline player to parse the placeholders against
     * @param text     The List of text to parse
     * @param pattern  The Pattern to use
     * @param colorize If PlaceholderAPI should also parse color codes
     * @return String with the parsed placeholders
     * @deprecated Please use {@link #setPlaceholders(OfflinePlayer, List)} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.13.0")
    public static List<String> setPlaceholders(OfflinePlayer player,
                                               List<String> text, Pattern pattern, boolean colorize) {
        return setPlaceholders(player, text);
    }

    /**
     * @param player   The offline player to parse the placeholders against
     * @param text     The List of text to parse
     * @param colorize If PlaceholderAPI should also parse color codes
     * @return String with the parsed placeholders
     * @deprecated Use {@link #setPlaceholders(OfflinePlayer, List)} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.13.0")
    public static List<String> setPlaceholders(OfflinePlayer player, List<String> text,
                                               boolean colorize) {
        return setPlaceholders(player, text);
    }

    /**
     * @param player  The offline player to parse the placeholders against
     * @param text    The List of text to parse
     * @param pattern The Pattern to use
     * @return String with the parsed placeholders
     * @deprecated Use {@link #setPlaceholders(OfflinePlayer, List)} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.13.0")
    public static List<String> setPlaceholders(OfflinePlayer player, List<String> text,
                                               Pattern pattern) {
        return setPlaceholders(player, text);
    }

    /**
     * @param player   The offline player to parse the placeholders against
     * @param text     The text to parse
     * @param colorize If PlaceholderAPI should also parse color codes
     * @return String with the parsed placeholders
     * @deprecated Will be removed in a future release.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.13.0")
    public static String setPlaceholders(Player player, String text, boolean colorize) {
        return setPlaceholders(player, text);
    }

    /**
     * @param player   The offline player to parse the placeholders against
     * @param text     The List of text to parse
     * @param colorize If PlaceholderAPI should also parse color codes
     * @return String with the parsed placeholders
     * @deprecated Will be removed in a future release.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.13.0")
    public static List<String> setPlaceholders(Player player, List<String> text, boolean colorize) {
        return setPlaceholders(player, text);
    }

    /**
     * @param player   The offline player to parse the placeholders against
     * @param text     The text to parse
     * @param colorize If PlaceholderAPI should also parse color codes
     * @return String with the parsed placeholders
     * @deprecated Use {@link #setPlaceholders(OfflinePlayer, String)} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.13.0")
    public static String setPlaceholders(OfflinePlayer player, String text, boolean colorize) {
        return setPlaceholders(player, text);
    }

    /**
     * @param player  The offline player to parse the placeholders against
     * @param text    The text to parse
     * @param pattern The Pattern to use
     * @return String with the parsed placeholders
     * @deprecated Use {@link #setPlaceholders(OfflinePlayer, String)} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.13.0")
    public static String setPlaceholders(OfflinePlayer player, String text, Pattern pattern) {
        return setPlaceholders(player, text);
    }

    /**
     * @param player   The offline player to parse the placeholders against
     * @param text     The List of text to parse
     * @param colorize If PlaceholderAPI should also parse color codes
     * @return String with the parsed placeholders
     * @deprecated Use {@link #setPlaceholders(OfflinePlayer, List)} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.13.0")
    public static List<String> setBracketPlaceholders(OfflinePlayer player, List<String> text,
                                                      boolean colorize) {
        return setBracketPlaceholders(player, text);
    }

    /**
     * @param player   The offline player to parse the placeholders against
     * @param text     The text to parse
     * @param colorize If PlaceholderAPI should also parse color codes
     * @return String with the parsed placeholders
     * @deprecated Use {@link #setPlaceholders(OfflinePlayer, String)} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.13.0")
    public static String setBracketPlaceholders(OfflinePlayer player, String text, boolean colorize) {
        return setBracketPlaceholders(player, text);
    }

    /**
     * @param player   The offline player to parse the placeholders against
     * @param text     The text to parse
     * @param colorize If PlaceholderAPI should also parse color codes
     * @return String with the parsed placeholders
     * @deprecated Will be removed in a future release.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.13.0")
    public static String setBracketPlaceholders(Player player, String text, boolean colorize) {
        return setBracketPlaceholders(player, text);
    }

    /**
     * @param player   The offline player to parse the placeholders against
     * @param text     The List of text to parse
     * @param colorize If PlaceholderAPI should also parse color codes
     * @return String with the parsed placeholders
     * @deprecated Will be removed in a future release.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.13.0")
    public static List<String> setBracketPlaceholders(Player player, List<String> text,
                                                      boolean colorize) {
        return setBracketPlaceholders(player, text);
    }

    /**
     * set relational placeholders in the text specified placeholders are matched with the pattern
     * {@literal %<rel_(identifier)_(params)>%} when set with this method
     *
     * @param one      Player to compare
     * @param two      Player to compare
     * @param text     Text to parse the placeholders in
     * @param colorize If color codes ({@literal &[0-1a-fk-o]}) should be translated
     * @return The text containing the parsed relational placeholders
     * @deprecated Use {@link #setPlaceholders(OfflinePlayer, String)} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.13.0")
    public static String setRelationalPlaceholders(Player one, Player two, String text,
                                                   boolean colorize) {
        return setRelationalPlaceholders(one, two, text);
    }

    /**
     * Translate placeholders in the provided list based on the relation of the two provided players.
     * <br>The pattern of a valid placeholder is {@literal %rel_<identifier>_<params>%}.
     *
     * @param one      First player to compare
     * @param two      Second player to compare
     * @param text     Text to parse the placeholders in
     * @param colorize If color codes ({@literal &[0-1a-fk-o]}) should be translated
     * @return The text containing the parsed relational placeholders
     * @deprecated Use {@link #setRelationalPlaceholders(Player, Player, List)} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.13.0")
    public static List<String> setRelationalPlaceholders(Player one, Player two, List<String> text,
                                                         boolean colorize) {
        return setRelationalPlaceholders(one, two, text);
    }
}
