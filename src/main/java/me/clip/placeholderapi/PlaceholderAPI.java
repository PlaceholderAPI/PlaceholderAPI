/*
 *
 * PlaceholderAPI
 * Copyright (C) 2018 Ryan McCarthy
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.clip.placeholderapi.events.PlaceholderHookUnloadEvent;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlaceholderAPI {
	
	private PlaceholderAPI() {
	}
	
	private final static Pattern PLACEHOLDER_PATTERN = Pattern.compile("[%]([^%]+)[%]");
	private final static Pattern BRACKET_PLACEHOLDER_PATTERN = Pattern.compile("[{]([^{}]+)[}]");
	private final static Pattern RELATIONAL_PLACEHOLDER_PATTERN = Pattern.compile("[%](rel_)([^%]+)[%]");
	private final static Map<String, PlaceholderHook> placeholders = new HashMap<>();
	
	/**
	 * unregister ALL placeholder hooks that are currently registered
	 */
	protected static void unregisterAll() {
		unregisterAllExpansions();
		placeholders.clear();
	}
	
	/**
	 * unregister all expansions
	 */
	public static void unregisterAllExpansions() {
		if (placeholders.isEmpty()) return;
		getPlaceholders().forEach((key, value) -> {
			if (value instanceof PlaceholderExpansion) {
				Bukkit.getPluginManager().callEvent(new PlaceholderHookUnloadEvent(key, value));
				unregisterPlaceholderHook(key);
			}
		});
	}
	
	/**
	 * check if a specific placeholder identifier is currently registered
	 * @param identifier to check
	 * @return true if identifier is already registered
	 */
	public static boolean isRegistered(String identifier) {
		return !placeholders.isEmpty() && getRegisteredIdentifiers().stream().filter(id -> id.equalsIgnoreCase(identifier)).findFirst().orElse(null) != null;
	}
	
	/**
	 * Register a new placeholder hook
	 * @param identifier Identifier of the placeholder -> "%(identifier)_(args...)%
	 * 
	 * @param placeholderHook implementing class that contains the onPlaceholderRequest method which is called when a value is needed for the specific placeholder
	 * @return true if the hook was successfully registered, false if there is already a hook registered for the specified identifier
	 */
	public static boolean registerPlaceholderHook(String identifier, PlaceholderHook placeholderHook) {
		Validate.notNull(identifier, "Identifier can not be null");
		Validate.notNull(placeholderHook, "Placeholderhook can not be null");
		if (isRegistered(identifier)) return false;
		//test
		placeholders.put(identifier.toLowerCase(), placeholderHook);
		return true;
	}
	
	/**
	 * unregister a placeholder hook by identifier
	 * @param identifier the identifier for the placeholder hook to unregister
	 * @return true if the placeholder hook was successfully unregistered, false if there was no placeholder hook registered for the identifier specified
	 */
	public static boolean unregisterPlaceholderHook(String identifier) {
		Validate.notNull(identifier, "Identifier can not be null");		
		return placeholders.remove(identifier.toLowerCase()) != null;
	}
	
	/**
	 * Get all registered placeholder identifiers
	 * @return all registered placeholder identifiers
	 */
	public static Set<String> getRegisteredIdentifiers() {
		if (placeholders.isEmpty()) return new HashSet<>();
		return new HashSet<>(placeholders.keySet());
	}

	/**
	 * Get map of registered placeholders 
	 * @return copy of the internal placeholder map
	 */
	public static Map<String, PlaceholderHook> getPlaceholders() {
		return new HashMap<>(placeholders);
	}
	
	/**
	 * check if a String contains any PlaceholderAPI placeholders
	 * @param text String to check 
	 * @return true if String contains any registered placeholder identifiers, false otherwise
	 */
	public static boolean containsPlaceholders(String text) {
		return text != null && PLACEHOLDER_PATTERN.matcher(text).find();
	}
	
	/**
	 * check if a String contains any PlaceholderAPI bracket placeholders
	 * @param text String to check 
	 * @return true if String contains any registered placeholder identifiers, false otherwise
	 */
	public static boolean containsBracketPlaceholders(String text) {
		return text != null && BRACKET_PLACEHOLDER_PATTERN.matcher(text).find();
	}
	
	/**
	 * set placeholders in the list<String> text provided
	 * placeholders are matched with the pattern {<placeholder>} when set with this method
	 * @param p Player to parse the placeholders for
	 * @param text text to set the placeholder values in
	 * @return modified list with all placeholders set to the corresponding values
	 */
	public static List<String> setBracketPlaceholders(Player p, List<String> text) {
		if (text == null) return null;
		List<String> temp = new ArrayList<>();
		text.forEach(line -> {
			temp.add(setBracketPlaceholders(p, line));
		});
		return temp;
	}
	
	/**
	 * set placeholders in the text specified
	 * placeholders are matched with the pattern {<placeholder>} when set with this method
	 * @param player Player to parse the placeholders for
	 * @param text text to parse the placeholder values to
	 * @return modified text with all placeholders set to the corresponding values
	 */
	public static String setBracketPlaceholders(Player player, String text) {
		if (text == null) return null;
		if (placeholders.isEmpty()) return colorize(text);
		Matcher placeholderMatcher = BRACKET_PLACEHOLDER_PATTERN.matcher(text);
		Map<String, PlaceholderHook> hooks = getPlaceholders();
		while (placeholderMatcher.find()) {
			String format = placeholderMatcher.group(1);
			int index = format.indexOf("_");
		    if (index == -1 || index >= format.length()) continue;
		    String identifier = format.substring(0, index).toLowerCase();
		    String params = format.substring(index+1);
		    if (hooks.containsKey(identifier)) {
		    	String value = hooks.get(identifier).onPlaceholderRequest(player, params);
				if (value != null) {
					text = text.replaceAll("\\{"+format+"\\}", Matcher.quoteReplacement(value));
				}
		    }
		}
		return colorize(text);
	}
	
	/**
	 * set placeholders in the list<String> text provided
	 * placeholders are matched with the pattern %(identifier)_(params)>% when set with this method
	 * @param p Player to parse the placeholders for
	 * @param text text to parse the placeholder values in
	 * @return modified list with all placeholders set to the corresponding values
	 */
	public static List<String> setPlaceholders(Player p, List<String> text) {
		if (text == null) return null;
		List<String> temp = new ArrayList<>();
		text.forEach(line -> {
			temp.add(setPlaceholders(p, line));
		});
		return temp;
	}
	
	/**
	 * set placeholders in the text specified
	 * placeholders are matched with the pattern %<(identifier)_(params)>% when set with this method
	 * @param player Player to parse the placeholders for
	 * @param text text to parse the placeholder values to
	 * @return text with all placeholders set to the corresponding values
	 */
	public static String setPlaceholders(Player player, String text) {
		if (text == null) return null;
		if (placeholders.isEmpty()) return colorize(text);
		Matcher m = PLACEHOLDER_PATTERN.matcher(text);
		Map<String, PlaceholderHook> hooks = getPlaceholders();
		while (m.find()) {
			String format = m.group(1);
			int index = format.indexOf("_");
		    if (index <= 0 || index >= format.length()) continue;
		    String identifier = format.substring(0, index).toLowerCase();
		    String params = format.substring(index+1);
		    if (hooks.containsKey(identifier)) {
		    	String value = hooks.get(identifier).onPlaceholderRequest(player, params);
				if (value != null) {
					text = text.replace("%"+format+"%", Matcher.quoteReplacement(value));
				}
		    }
		}
		return ChatColor.translateAlternateColorCodes('&', text);
	}
	
	/**
	 * set relational placeholders in the text specified
	 * placeholders are matched with the pattern %<rel_(identifier)_(params)>% when set with this method
	 * @param one Player to compare
	 * @param two Player to compare
	 * @param text text to parse the placeholder values to
	 * @return text with all relational placeholders set to the corresponding values
	 */
	public static List<String> setRelationalPlaceholders(Player one, Player two, List<String> text) {
		if (text == null) return null;
		List<String> temp = new ArrayList<>();
		text.forEach(line -> {
			temp.add(setRelationalPlaceholders(one, two, line));
		});
		return temp;
	}
	
	/**
	 * set relational placeholders in the text specified
	 * placeholders are matched with the pattern %<rel_(identifier)_(params)>% when set with this method
	 * @param one Player to compare
	 * @param two Player to compare
	 * @param text text to parse the placeholder values to
	 * @return text with all relational placeholders set to the corresponding values
	 */
	public static String setRelationalPlaceholders(Player one, Player two, String text) {
		if (text == null) return null;
		if (placeholders.isEmpty()) return colorize(text);
		Matcher m = RELATIONAL_PLACEHOLDER_PATTERN.matcher(text);
		Map<String, PlaceholderHook> hooks = getPlaceholders();
		while (m.find()) {
			String format = m.group(2);
			int index = format.indexOf("_");
		    if (index <= 0 || index >= format.length()) continue;
		    String identifier = format.substring(0, index).toLowerCase();
		    String params = format.substring(index+1);
		    
		    if (hooks.containsKey(identifier)) {
		    	
		    	if (!(hooks.get(identifier) instanceof Relational)) {
		    		continue;
		    	}
		    	
		    	Relational rel = (Relational) hooks.get(identifier);
		    	String value = rel.onPlaceholderRequest(one, two, params);
				
				if (value != null) {
					text = text.replace("%rel_"+format+"%", Matcher.quoteReplacement(value));
				}
		    }
		}
		return ChatColor.translateAlternateColorCodes('&', text);
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
	
	public static String colorize(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
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
}
