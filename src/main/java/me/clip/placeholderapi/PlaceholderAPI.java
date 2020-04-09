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
   * Check if a specific placeholder identifier is currently registered
   *
   * @param identifier The identifier to check
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
   * @param placeholderHook Implementing class that contains the onPlaceholderRequest method which
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
   * Unregister a placeholder hook by identifier
   *
   * @param identifier The identifier for the placeholder hook to unregister
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
   * @return All registered placeholder identifiers
   */
  public static Set<String> getRegisteredIdentifiers() {
    return ImmutableSet.copyOf(placeholders.keySet());
  }
  
  /**
   * Get map of registered placeholders
   *
   * @return Copy of the internal placeholder map
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
   * Check if a String contains any PlaceholderAPI placeholders ({@literal %<identifier>_<params>%}).
   *
   * @param text String to check
   * @return true if String contains any registered placeholder identifiers, false otherwise
   */
  public static boolean containsPlaceholders(String text) {
    return text != null && PLACEHOLDER_PATTERN.matcher(text).find();
  }
  
  /**
   * Check if a String contains any PlaceholderAPI bracket placeholders ({@literal {<identifier>_<params>}}).
   *
   * @param text String to check
   * @return true if String contains any registered placeholder identifiers, false otherwise
   */
  public static boolean containsBracketPlaceholders(String text) {
    return text != null && BRACKET_PLACEHOLDER_PATTERN.matcher(text).find();
  }
  
  /**
   * Translates all placeholders into their corresponding values.
   * <br>The pattern of a valid placeholder is {@literal {<identifier>_<params>}}.
   *
   * @param player Player to parse the placeholders against
   * @param text List of Strings to set the placeholder values in
   * @return String containing all translated placeholders
   */
  public static List<String> setBracketPlaceholders(OfflinePlayer player, List<String> text) {
    return setPlaceholders(player, text, BRACKET_PLACEHOLDER_PATTERN, true);
  }
  
  /**
   * Translates all placeholders into their corresponding values.
   * <br>The pattern of a valid placeholder is {@literal {<identifier>_<params>}}.
   *
   * @param player Player to parse the placeholders against
   * @param text List of Strings to set the placeholder values in
   * @param colorize If color codes (&[0-1a-fk-o]) should be translated
   * @return String containing all translated placeholders
   */
  public static List<String> setBracketPlaceholders(OfflinePlayer player, List<String> text, boolean colorize) {
    return setPlaceholders(player, text, BRACKET_PLACEHOLDER_PATTERN, colorize);
  }
  
  /**
   * Translates all placeholders into their corresponding values.
   * <br>The pattern of a valid placeholder is {@literal %<identifier>_<params>%}.
   *
   * @param player Player to parse the placeholders against
   * @param text List of Strings to set the placeholder values in
   * @return String containing all translated placeholders
   */
  public static List<String> setPlaceholders(OfflinePlayer player, List<String> text) {
    return setPlaceholders(player, text, PLACEHOLDER_PATTERN, true);
  }
  
  /**
   * Translates all placeholders into their corresponding values.
   * <br>The pattern of a valid placeholder is {@literal %<identifier>_<params>%}.
   *
   * @param player Player to parse the placeholders against
   * @param text List of Strings to set the placeholder values in
   * @param colorize If color codes (&[0-1a-fk-o]) should be translated
   * @return String containing all translated placeholders
   */
  public static List<String> setPlaceholders(OfflinePlayer player, List<String> text, boolean colorize) {
    return setPlaceholders(player, text, PLACEHOLDER_PATTERN, colorize);
  }
  
  /**
   * Translates all placeholders into their corresponding values.
   * <br>You set the pattern yourself through this method.
   *
   * @param player Player to parse the placeholders against
   * @param text List of Strings to set the placeholder values in
   * @param pattern The pattern to match placeholders to. Capture group 1 must contain an underscore separating the 
   * identifier from the params
   * @return String containing all translated placeholders
   */
  public static List<String> setPlaceholders(OfflinePlayer player, List<String> text, Pattern pattern) {
    return setPlaceholders(player, text, pattern, true);
  }
  
  /**
   * Translates all placeholders into their corresponding values.
   * <br>You set the pattern yourself through this method.
   *
   * @param player Player to parse the placeholders against
   * @param text List of Strings to set the placeholder values in
   * @param pattern The pattern to match placeholders to. Capture group 1 must contain an underscore separating the 
   * identifier from the params
   * @param colorize If color codes (&[0-1a-fk-o]) should be translated
   * @return String containing all translated placeholders
   */
  public static List<String> setPlaceholders(OfflinePlayer player, List<String> text, Pattern pattern, boolean colorize) {
    if(text == null) {
      return null;
    }
    
    return text.stream().map(line -> setPlaceholders(player, line, pattern, colorize))
            .collect(Collectors.toList());
  }
  
  /**
   * Translates all placeholders into their corresponding values.
   * <br>The pattern of a valid placeholder is {@literal {<identifier>_<params>}}.
   *
   * @param player Player to parse the placeholders against
   * @param text Text to set the placeholder values in
   * @return String containing all translated placeholders
   */
  public static String setBracketPlaceholders(OfflinePlayer player, String text) {
    return setPlaceholders(player, text, BRACKET_PLACEHOLDER_PATTERN, true);
  }
  
  /**
   * Translates all placeholders into their corresponding values.
   * <br>The pattern of a valid placeholder is {@literal {<identifier>_<params>}}
   *
   * @param player Player to parse the placeholders against
   * @param text Text to set the placeholder values in
   * @param colorize If color codes (&[0-1a-fk-o]) should be translated
   * @return String containing all translated placeholders
   */
  public static String setBracketPlaceholders(OfflinePlayer player, String text, boolean colorize) {
    return setPlaceholders(player, text, BRACKET_PLACEHOLDER_PATTERN, colorize);
  }
  
  /**
   * Translates all placeholders into their corresponding values.
   * <br>The pattern of a valid placeholder is {@literal %<identifier>_<params>%}.
   *
   * @param player Player to parse the placeholders against
   * @param text Text to set the placeholder values in
   * @return String containing all translated placeholders
   */
  public static String setPlaceholders(OfflinePlayer player, String text) {
    return setPlaceholders(player, text, PLACEHOLDER_PATTERN);
  }
  
  /**
   * Translates all placeholders into their corresponding values.
   * <br>The pattern of a valid placeholder is {@literal %<identifier>_<params>%}.
   *
   * @param player Player to parse the placeholder against
   * @param text Text to parse the placeholders in
   * @param colorize If color codes (&[0-1a-fk-o]) should be translated
   * @return The text containing the parsed placeholders
   */
  public static String setPlaceholders(OfflinePlayer player, String text, boolean colorize) {
    return setPlaceholders(player, text, PLACEHOLDER_PATTERN, colorize);
  }
  
  /**
   * Translates all placeholders into their corresponding values.
   * <br>You set the pattern yourself through this method.
   *
   * @param player Player to parse the placeholders against
   * @param text Text to set the placeholder values in
   * @param pattern The pattern to match placeholders to. Capture group 1 must contain an underscore separating the 
   * identifier from the params
   * @return The text containing the parsed placeholders
   */
  public static String setPlaceholders(OfflinePlayer player, String text, Pattern pattern) {
    return setPlaceholders(player, text, pattern, true);
  }
  
  /**
   * Translates all placeholders into their corresponding values.
   * <br>You set the pattern yourself through this method.
   *
   * @param player Player to parse the placeholders against
   * @param text Text to set the placeholder values in
   * @param pattern The pattern to match placeholders to. Capture group 1 must contain an underscore separating the 
   * identifier from the params
   * @param colorize If color codes (&[0-1a-fk-o]) should be translated
   * @return The text containing the parsed placeholders
   */
  public static String setPlaceholders(OfflinePlayer player, String text, Pattern pattern, boolean colorize) {
    if (text == null) {
      return null;
    }
    
    if (placeholders.isEmpty()) {
      return colorize ? color(text) : text;
    }
    
    Matcher m = pattern.matcher(text);
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
    
    return colorize ? color(text) : text;
  }
  
  /**
   * Translate placeholders in the provided List based on the relation of the two provided players.
   * <br>The pattern of a valid placeholder is {@literal %rel_<identifier>_<param>%}.
   *
   * @param one Player to compare
   * @param two Player to compare
   * @param text text to parse the placeholder values to
   * @return The text containing the parsed relational placeholders
   */
  public static List<String> setRelationalPlaceholders(Player one, Player two, List<String> text) {
    return setRelationalPlaceholders(one, two, text, true);
  }
  
  /**
   * Translate placeholders in the provided list based on the relation of the two provided players.
   * <br>The pattern of a valid placeholder is {@literal %rel_<identifier>_<params>%}.
   *
   * @param one First player to compare
   * @param two Second player to compare
   * @param text Text to parse the placeholders in
   * @param colorize If color codes (&[0-1a-fk-o]) should be translated
   * @return The text containing the parsed relational placeholders
   */
  public static List<String> setRelationalPlaceholders(Player one, Player two, List<String> text, boolean colorize) {
    if(text == null) {
      return null;
    }
    
    return text.stream().map(line -> setRelationalPlaceholders(one, two, line, colorize))
            .collect(Collectors.toList());
  }
  
  /**
   * set relational placeholders in the text specified placeholders are matched with the pattern
   * %<rel_(identifier)_(params)>% when set with this method
   *
   * @param one First player to compare
   * @param two Second player to compare
   * @param text Text to parse the placeholders in
   * @return The text containing the parsed relational placeholders
   */
  public static String setRelationalPlaceholders(Player one, Player two, String text) {
    return setRelationalPlaceholders(one,two, text, true);
  }
  
  /**
   * set relational placeholders in the text specified placeholders are matched with the pattern
   * %<rel_(identifier)_(params)>% when set with this method
   *
   * @param one Player to compare
   * @param two Player to compare
   * @param text Text to parse the placeholders in
   * @param colorize If color codes (&[0-1a-fk-o]) should be translated
   * @return The text containing the parsed relational placeholders
   */
  public static String setRelationalPlaceholders(Player one, Player two, String text, boolean colorize) {
    if (text == null) {
      return null;
    }
    
    if (placeholders.isEmpty()) {
      return colorize ? color(text) : text;
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
    
    return colorize ? color(text) : text;
  }
  
  /**
   * Unregister ALL placeholder hooks that are currently registered
   */
  protected static void unregisterAll() {
    unregisterAllProvidedExpansions();
    placeholders.clear();
  }
  
  /**
   * Unregister all expansions provided by PlaceholderAPI
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
    ExpansionRegisterEvent ev = new ExpansionRegisterEvent(ex);
    Bukkit.getPluginManager().callEvent(ev);
    if (ev.isCancelled()) {
      return false;
    }
    
    return registerPlaceholderHook(ex.getIdentifier(), ex);
  }
  
  public static boolean unregisterExpansion(PlaceholderExpansion ex) {
    if (unregisterPlaceholderHook(ex.getIdentifier())) {
      Bukkit.getPluginManager().callEvent(new ExpansionUnregisterEvent(ex));
      return true;
    }
    
    return false;
  }
  
  /**
   * Gets the placeholder pattern for the default placeholders.
   *
   * @return The pattern for {@literal %<identifier>_<params>%}
   */
  public static Pattern getPlaceholderPattern() {
    return PLACEHOLDER_PATTERN;
  }
  
  /**
   * Gets the placeholder pattern for the bracket placeholders.
   *
   * @return The pattern for {@literal {<identifier>_<params>}}
   */
  public static Pattern getBracketPlaceholderPattern() {
    return BRACKET_PLACEHOLDER_PATTERN;
  }
  
  /**
   * Gets the placeholder pattern for the relational placeholders.
   *
   * @return The pattern for {@literal %rel_<identifier>_<params>%}
   */
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
  
  public static String setPlaceholders(Player player, String text) {
    return setPlaceholders(player, text, PLACEHOLDER_PATTERN, true);
  }
  
  public static String setPlaceholders(Player player, String text, boolean colorize) {
    return setPlaceholders(player, text, PLACEHOLDER_PATTERN, colorize);
  }
  
  public static List<String> setPlaceholders(Player player, List<String> text) {
    return setPlaceholders(player, text, PLACEHOLDER_PATTERN, true);
  }
  
  public static List<String> setPlaceholders(Player player, List<String> text, boolean colorize) {
    return setPlaceholders(player, text, PLACEHOLDER_PATTERN, colorize);
  }
  
  public static String setBracketPlaceholders(Player player, String text) {
    return setPlaceholders(player, text, BRACKET_PLACEHOLDER_PATTERN, true);
  }
  
  public static String setBracketPlaceholders(Player player, String text, boolean colorize) {
    return setPlaceholders(player, text, BRACKET_PLACEHOLDER_PATTERN, colorize);
  }
  
  public static List<String> setBracketPlaceholders(Player player, List<String> text) {
    return setPlaceholders(player, text, BRACKET_PLACEHOLDER_PATTERN, true);
  }
  
  public static List<String> setBracketPlaceholders(Player player, List<String> text, boolean colorize) {
    return setPlaceholders(player, text, BRACKET_PLACEHOLDER_PATTERN, colorize);
  }
}
