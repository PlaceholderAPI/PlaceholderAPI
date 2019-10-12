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
package me.clip.placeholderapi.nukkit;

import cn.nukkit.IPlayer;
import cn.nukkit.OfflinePlayer;
import cn.nukkit.Server;
import com.google.common.collect.ImmutableSet;
import me.clip.placeholderapi.nukkit.event.ExpansionRegisterEvent;
import me.clip.placeholderapi.nukkit.event.ExpansionUnregisterEvent;
import me.clip.placeholderapi.nukkit.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.nukkit.expansion.Relational;
import org.apache.commons.lang3.Validate;

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

    public static boolean isRegistered(String identifier) {
        return getRegisteredIdentifiers().stream().filter(id -> id.equalsIgnoreCase(identifier)).findFirst().orElse(null) != null;
    }

    public static boolean registerPlaceholderHook(String identifier, PlaceholderHook placeholderHook) {
        Validate.notNull(identifier, "Identifier cannot be null");
        Validate.notNull(placeholderHook, "PlaceholderHook cannot be null");
        if (isRegistered(identifier)) {
            return false;
        }

        placeholders.put(identifier.toLowerCase(), placeholderHook);
        return true;
    }

    public static boolean unregisterPlaceholderHook(String identifier) {
        Validate.notNull(identifier, "Identifier can not be null");
        return placeholders.remove(identifier.toLowerCase()) != null;
    }

    public static Set<String> getRegisteredIdentifiers() {
        return ImmutableSet.copyOf(placeholders.keySet());
    }

    public static Map<String, PlaceholderHook> getPlaceholders() {
        return placeholders;
    }

    public static Set<PlaceholderExpansion> getExpansions() {
        Set<PlaceholderExpansion> set = getPlaceholders().values().stream()
                .filter(PlaceholderExpansion.class::isInstance).map(PlaceholderExpansion.class::cast)
                .collect(Collectors.toCollection(HashSet::new));
        return ImmutableSet.copyOf(set);
    }

    public static boolean containsPlaceholders(String text) {
        return text != null && PLACEHOLDER_PATTERN.matcher(text).find();
    }

    public static boolean containsBracketPlaceholders(String text) {
        return text != null && BRACKET_PLACEHOLDER_PATTERN.matcher(text).find();
    }

    public static List<String> setBracketPlaceholders(OfflinePlayer p, List<String> text) {
        return setPlaceholders(p, text, BRACKET_PLACEHOLDER_PATTERN);
    }

    public static List<String> setPlaceholders(OfflinePlayer p, List<String> text) {
        return setPlaceholders(p, text, PLACEHOLDER_PATTERN);
    }

    public static List<String> setPlaceholders(OfflinePlayer p, List<String> text, Pattern pattern) {
        if (text == null) {
            return null;
        }

        return text.stream().map(line -> setPlaceholders(p, line, pattern)).collect(Collectors.toList());
    }

    public static String setBracketPlaceholders(OfflinePlayer player, String text) {
        return setPlaceholders(player, text, BRACKET_PLACEHOLDER_PATTERN);
    }

    public static String setPlaceholders(OfflinePlayer player, String text) {
        return setPlaceholders(player, text, PLACEHOLDER_PATTERN);
    }

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

    public static List<String> setRelationalPlaceholders(IPlayer one, IPlayer two, List<String> text) {
        if (text == null) {
            return null;
        }

        return text.stream().map(line -> setRelationalPlaceholders(one, two, line)).collect(Collectors.toList());
    }

    public static String setRelationalPlaceholders(IPlayer one, IPlayer two, String text) {
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

    protected static void unregisterAll() {
        unregisterAllProvidedExpansions();
        placeholders.clear();
    }

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
        Server.getInstance().getPluginManager().callEvent(ev);
        if (ev.isCancelled()) {
            return false;
        }
        return registerPlaceholderHook(ex.getIdentifier(), ex);
    }

    public static boolean unregisterExpansion(PlaceholderExpansion ex) {
        if (unregisterPlaceholderHook(ex.getIdentifier())) {
            Server.getInstance().getPluginManager().callEvent(new ExpansionUnregisterEvent(ex));
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

    public static String setPlaceholders(IPlayer p, String text) {
        return setPlaceholders((OfflinePlayer) p, text, PLACEHOLDER_PATTERN);
    }

    public static List<String> setPlaceholders(IPlayer p, List<String> text) {
        return setPlaceholders((OfflinePlayer) p, text, PLACEHOLDER_PATTERN);
    }

    public static String setBracketPlaceholders(IPlayer p, String text) {
        return setPlaceholders((OfflinePlayer) p, text, BRACKET_PLACEHOLDER_PATTERN);
    }

    public static List<String> setBracketPlaceholders(IPlayer p, List<String> text) {
        return setPlaceholders((OfflinePlayer) p, text, BRACKET_PLACEHOLDER_PATTERN);
    }
}