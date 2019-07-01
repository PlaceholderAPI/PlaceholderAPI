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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import me.clip.placeholderapi.events.ExpansionRegisterEvent;
import me.clip.placeholderapi.events.ExpansionUnregisterEvent;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static me.clip.placeholderapi.util.Msg.color;

public class PlaceholderAPI {

  private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("[%]([^%]+)[%]");
  private static final Pattern BRACKET_PLACEHOLDER_PATTERN = Pattern.compile("[{]([^{}]+)[}]");
  private static final Pattern RELATIONAL_PLACEHOLDER_PATTERN = Pattern.compile("[%](rel_)([^%]+)[%]");
  private static final Map<String, PlaceholderHook> placeholders = new HashMap<>();

  private PlaceholderAPI() {
  }

  /**
   * check if a specific placeholder identifier is currently registered
   *
   * @param identifier to check
   * @return true if identifier is already registered
   */
  public static boolean isRegistered(String identifier) {
    return getRegisteredIdentifiers().stream().filter(id -> id.equalsIgnoreCase(identifier))
        .findFirst().orElse(null) != null;
  }

  /**
   * Register a new placeholder hook
   *
   * @param identifier Identifier of the placeholder -> "%(identifier)_(args...)%
   * @param placeholderHook implementing class that contains the onPlaceholderRequest method which
   * is called when a value is needed for the specific placeholder
   * @return true if the hook was successfully registered, false if there is already a hook
   * registered for the specified identifier
   */
  public static boolean registerPlaceholderHook(String identifier, PlaceholderHook placeholderHook) {
    Validate.notNull(identifier, "Identifier can not be null");
    Validate.notNull(placeholderHook, "Placeholderhook can not be null");

    if (isRegistered(identifier)) {
      return false;
    }

    placeholders.put(identifier.toLowerCase(), placeholderHook);

    return true;
  }

  /**
   * unregister a placeholder hook by identifier
   *
   * @param identifier the identifier for the placeholder hook to unregister
   * @return true if the placeholder hook was successfully unregistered, false if there was no
   * placeholder hook registered for the identifier specified
   */
  public static boolean unregisterPlaceholderHook(String identifier) {
    Validate.notNull(identifier, "Identifier can not be null");
    return placeholders.remove(identifier.toLowerCase()) != null;
  }

  /**
   * Get all registered placeholder identifiers
   *
   * @return all registered placeholder identifiers
   */
  public static Set<String> getRegisteredIdentifiers() {
    return ImmutableSet.copyOf(placeholders.keySet());
  }

  /**
   * Get map of registered placeholders
   *
   * @return copy of the internal placeholder map
   */
  public static Map<String, PlaceholderHook> getPlaceholders() {
    return ImmutableMap.copyOf(placeholders);
  }

  public static Set<PlaceholderExpansion> getExpansions() {
    Set<PlaceholderExpansion> set = getPlaceholders().values().stream()
        .filter(PlaceholderExpansion.class::isInstance).map(PlaceholderExpansion.class::cast)
        .collect(Collectors.toCollection(HashSet::new));

    return ImmutableSet.copyOf(set);
  }

  /**
   * check if a String contains any PlaceholderAPI placeholders
   *
   * @param text String to check
   * @return true if String contains any registered placeholder identifiers, false otherwise
   */
  public static boolean containsPlaceholders(String text) {
    return text != null && PLACEHOLDER_PATTERN.matcher(text).find();
  }

  /**
   * check if a String contains any PlaceholderAPI bracket placeholders
   *
   * @param text String to check
   * @return true if String contains any registered placeholder identifiers, false otherwise
   */
  public static boolean containsBracketPlaceholders(String text) {
    return text != null && BRACKET_PLACEHOLDER_PATTERN.matcher(text).find();
  }

  /**
   * set placeholders in the list<String> text provided placeholders are matched with the pattern
   * {<placeholder>} when set with this method
   *
   * @param p Player to parse the placeholders for
   * @param text text to set the placeholder values in
   * @return modified list with all placeholders set to the corresponding values
   */
  public static List<String> setBracketPlaceholders(OfflinePlayer p, List<String> text) {
    return setPlaceholders(p, text, BRACKET_PLACEHOLDER_PATTERN);
  }

  /**
   * set placeholders in the list<String> text provided placeholders are matched with the pattern
   * %(identifier)_(params)>% when set with this method
   *
   * @param p Player to parse the placeholders for
   * @param text text to parse the placeholder values in
   * @return modified list with all placeholders set to the corresponding values
   */
  public static List<String> setPlaceholders(OfflinePlayer p, List<String> text) {
    return setPlaceholders(p, text, PLACEHOLDER_PATTERN);
  }


  /**
   * set placeholders in the list<String> text provided placeholders are matched with the pattern
   * %(identifier)_(params)>% when set with this method
   *
   * @param p Player to parse the placeholders for
   * @param text text to parse the placeholder values in
   * @return modified list with all placeholders set to the corresponding values
   */
  public static List<String> setPlaceholders(OfflinePlayer p, List<String> text, Pattern pattern) {
    if (text == null) {
      return null;
    }

    return text.stream().map(line -> setPlaceholders(p, line, pattern))
        .collect(Collectors.toList());
  }

  /**
   * set placeholders in the text specified placeholders are matched with the pattern
   * {<placeholder>} when set with this method
   *
   * @param player Player to parse the placeholders for
   * @param text text to parse the placeholder values to
   * @return modified text with all placeholders set to the corresponding values
   */
  public static String setBracketPlaceholders(OfflinePlayer player, String text) {
    return setPlaceholders(player, text, BRACKET_PLACEHOLDER_PATTERN);
  }

  /**
   * set placeholders in the text specified placeholders are matched with the pattern
   * %<(identifier)_(params)>% when set with this method
   *
   * @param player Player to parse the placeholders for
   * @param text text to parse the placeholder values to
   * @return text with all placeholders set to the corresponding values
   */
  public static String setPlaceholders(OfflinePlayer player, String text) {
    return setPlaceholders(player, text, PLACEHOLDER_PATTERN);
  }

  /**
   * set placeholders in the text specified placeholders are matched with the pattern
   * %<(identifier)_(params)>% when set with this method
   *
   * @param player Player to parse the placeholders for
   * @param text text to parse the placeholder values to
   * @param placeholderPattern the pattern to match placeholders to. Capture group 1 must contain an
   * underscore separating the identifier from the params
   * @return text with all placeholders set to the corresponding values
   */
  public static String setPlaceholders(OfflinePlayer player, String text, Pattern placeholderPattern) {
    if (text == null) {
      return null;
    }

    if (placeholders.isEmpty()) {
      return color(text);
    }

    Matcher m = placeholderPattern.matcher(text);
    Map<String, PlaceholderHook> hooks = getPlaceholders();

    while (m.find()) {
      String format = m.group(1);
      int index = format.indexOf("_");

      if (index <= 0 || index >= format.length()) {
        continue;
      }

      String identifier = format.substring(0, index).toLowerCase();
      String params = format.substring(index + 1);

      if (hooks.containsKey(identifier)) {
        String value = hooks.get(identifier).onRequest(player, params);
        if (value != null) {
          text = text.replaceAll(Pattern.quote(m.group()), Matcher.quoteReplacement(value));
        }
      }
    }

    return color(text);
  }

  /**
   * set relational placeholders in the text specified placeholders are matched with the pattern
   * %<rel_(identifier)_(params)>% when set with this method
   *
   * @param one Player to compare
   * @param two Player to compare
   * @param text text to parse the placeholder values to
   * @return text with all relational placeholders set to the corresponding values
   */
  public static List<String> setRelationalPlaceholders(Player one, Player two, List<String> text) {
    if (text == null) {
      return null;
    }

    return text.stream().map(line -> setRelationalPlaceholders(one, two, line))
        .collect(Collectors.toList());
  }

  /**
   * set relational placeholders in the text specified placeholders are matched with the pattern
   * %<rel_(identifier)_(params)>% when set with this method
   *
   * @param one Player to compare
   * @param two Player to compare
   * @param text text to parse the placeholder values to
   * @return text with all relational placeholders set to the corresponding values
   */
  public static String setRelationalPlaceholders(Player one, Player two, String text) {
    if (text == null) {
      return null;
    }

    if (placeholders.isEmpty()) {
      return color(text);
    }

    Matcher m = RELATIONAL_PLACEHOLDER_PATTERN.matcher(text);
    Map<String, PlaceholderHook> hooks = getPlaceholders();

    while (m.find()) {
      String format = m.group(2);
      int index = format.indexOf("_");

      if (index <= 0 || index >= format.length()) {
        continue;
      }

      String identifier = format.substring(0, index).toLowerCase();
      String params = format.substring(index + 1);

      if (hooks.containsKey(identifier)) {
        if (!(hooks.get(identifier) instanceof Relational)) {
          continue;
        }

        Relational rel = (Relational) hooks.get(identifier);
        String value = rel.onPlaceholderRequest(one, two, params);

        if (value != null) {
          text = text.replaceAll(Pattern.quote(m.group()), Matcher.quoteReplacement(value));
        }
      }
    }

    return color(text);
  }

  /**
   * unregister ALL placeholder hooks that are currently registered
   */
  protected static void unregisterAll() {
    unregisterAllProvidedExpansions();
    placeholders.clear();
  }

  /**
   * unregister all expansions provided by PlaceholderAPI
   */
  public static void unregisterAllProvidedExpansions() {
    if (placeholders.isEmpty()) {
      return;
    }

    getPlaceholders().forEach((key, value) -> {
      if (value instanceof PlaceholderExpansion) {
        PlaceholderExpansion ex = (PlaceholderExpansion) value;

        if (!ex.persist()) {
          unregisterExpansion(ex);
        }
      }
    });
  }

  public static boolean registerExpansion(PlaceholderExpansion ex) {
    if (registerPlaceholderHook(ex.getIdentifier(), ex)) {
      Bukkit.getPluginManager().callEvent(new ExpansionRegisterEvent(ex));
      return true;
    }

    return false;
  }

  public static boolean unregisterExpansion(PlaceholderExpansion ex) {
    if (unregisterPlaceholderHook(ex.getIdentifier())) {
      Bukkit.getPluginManager().callEvent(new ExpansionUnregisterEvent(ex));
      return true;
    }

    return false;
  }

  public static Pattern getPlaceholderPattern() {
    return PLACEHOLDER_PATTERN;
  }

  public static Pattern getBracketPlaceholderPattern() {
    return BRACKET_PLACEHOLDER_PATTERN;
  }

  public static Pattern getRelationalPlaceholderPattern() {
    return RELATIONAL_PLACEHOLDER_PATTERN;
  }

  @Deprecated
  public static Set<String> getRegisteredPlaceholderPlugins() {
    return getRegisteredIdentifiers();
  }

  @Deprecated
  public static Set<String> getExternalPlaceholderPlugins() {
    return null;
  }

  @Deprecated
  public static boolean registerPlaceholderHook(Plugin plugin, PlaceholderHook placeholderHook) {
    return plugin != null && registerPlaceholderHook(plugin.getName(), placeholderHook);
  }

  @Deprecated
  public static boolean unregisterPlaceholderHook(Plugin plugin) {
    return plugin != null && unregisterPlaceholderHook(plugin.getName());
  }

  public static String setPlaceholders(Player p, String text) {
    return setPlaceholders(p, text, PLACEHOLDER_PATTERN);
  }

  public static List<String> setPlaceholders(Player p, List<String> text) {
    return setPlaceholders(p, text, PLACEHOLDER_PATTERN);
  }

  public static String setBracketPlaceholders(Player p, String text) {
    return setPlaceholders(p, text, BRACKET_PLACEHOLDER_PATTERN);
  }

  public static List<String> setBracketPlaceholders(Player p, List<String> text) {
    return setPlaceholders(p, text, BRACKET_PLACEHOLDER_PATTERN);
  }
}
