/*
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
 */
package me.clip.placeholderapi;

import com.google.common.collect.ImmutableSet;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.util.Set;

/**
 * This is certainly hard to understand and maintain, but it's fully optimized.
 * It's almost x5 times faster than the RegEx method for normal sized strings. This performance gap gets smaller
 * for smaller strings.
 *
 * @author Crypto Morin
 */
public class PlaceholderReplacer {
    /**
     * Cached available color codes. Technically the uppercase of each letter can be used too, but no one really uses the uppercase ones.
     */
    private static final Set<Character> COLOR_CODES = ImmutableSet.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'k', 'l', 'm', 'o', 'r', 'x');

    /**
     * Translates placeholders for a string using pure character loops.
     * Might cause problems in really rare conditions.
     *
     * @param player   the player to translate the string for.
     * @param str      the string to translate.
     * @param closure  the type of the placeholder closing points.
     * @param colorize if this message should be colorized as well.
     * @return a translated string.
     */
    public static String evaluatePlaceholders(OfflinePlayer player, String str, Closure closure, boolean colorize) {
        char[] chars = str.toCharArray();
        StringBuilder builder = new StringBuilder(chars.length);

        // This won't cause memory leaks. It's inside a method. And we want to use setLength instead of
        // creating a new string builder to use the maximum capacity and avoid initializing new objects.
        StringBuilder identifier = new StringBuilder(50);
        PlaceholderHook handler = null;

        // Stages:
        //   Stage -1: Look for the color code in the next character.
        //   Stage 0: No closures detected, or the detected identifier is invalid. We're going forward while appending the characters normally.
        //   Stage 1: The closure has been detected, looking for the placeholder identifier...
        //   Stage 2: Detected the identifier and the parameter. Translating the placeholder...
        int stage = 0;

        for (char ch : chars) {
            if (stage == -1 && COLOR_CODES.contains(ch)) {
                builder.append(ChatColor.COLOR_CHAR).append(ch);
                stage = 0;
                continue;
            }

            // Check if the placeholder starts or ends.
            if (ch == closure.start || ch == closure.end) {
                // If the placeholder ends.
                if (stage == 2) {
                    String parameter = identifier.toString();
                    String translated = handler.onRequest(player, parameter);

                    if (translated == null) {
                        String name = handler.isExpansion() ? ((PlaceholderExpansion) handler).getIdentifier() : "";
                        builder.append(closure.start).append(name).append('_').append(parameter).append(closure.end);
                    } else builder.append(translated);

                    identifier.setLength(0);
                    stage = 0;
                    continue;
                } else if (stage == 1) { // If it just started | Double closures | If it's still hasn't detected the indentifier, reset.
                    builder.append(closure.start).append(identifier);
                }

                identifier.setLength(0);
                stage = 1;
                continue;
            }

            // Placeholder identifier started.
            if (stage == 1) {
                // Compare the current character with the idenfitier's.
                // We reached the end of our identifier.
                if (ch == '_') {
                    handler = PlaceholderAPI.PLACEHOLDERS.get(identifier.toString());
                    if (handler == null) {
                        builder.append(closure.start).append(identifier).append('_');
                        stage = 0;
                    } else {
                        identifier.setLength(0);
                        stage = 2;
                    }
                    continue;
                }

                // Keep building the identifier name.
                identifier.append(ch);
                continue;
            }

            // Building the placeholder parameter.
            if (stage == 2) {
                identifier.append(ch);
                continue;
            }

            // Nothing placeholder related was found.
            if (colorize && ch == '&') {
                stage = -1;
                continue;
            }
            builder.append(ch);
        }

        if (identifier != null) {
            if (stage > 0) builder.append(closure.end);
            builder.append(identifier);
        }
        return builder.toString();
    }

    public enum Closure {
        PERCENT('%', '%'), BRACKETS('[', ']'), BRACES('{', '}');

        public char start, end;

        Closure(char start, char end) {
            this.start = start;
            this.end = end;
        }
    }
}
