package me.clip.placeholderapi;

import com.google.common.collect.ImmutableSet;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.util.Set;

/**
 * This is certainly hard to understand, but it's fully optimized.
 */
public class PlaceholderReplacer {
    /**
     * Cached available color codes. Technically the uppercase of each letter can be used too, but no one really uses the uppercase ones.
     */
    private static final Set<Character> COLOR_CODES = ImmutableSet.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'k', 'l', 'm', 'o', 'r');

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
        StringBuilder builder = new StringBuilder(str.length());
        StringBuilder identifier = new StringBuilder(50);
        PlaceholderHook handler = null;

        // Stages:
        //   Stage -1: Look for the color code in the next character.
        //   Stage 0: No closures has been detected or the detected identifier is invalid. We're going forward appending normal string.
        //   Stage 1: The closure has been detected, looking for identifier...
        //   Stage 2: The identifier has been detected and the parameter has been found. Translating placeholder...
        int stage = 0;

        for (char ch : str.toCharArray()) {
            if (stage == -1 && COLOR_CODES.contains(ch)) {
                builder.append(ChatColor.COLOR_CHAR).append(ch);
                stage = 0;
                continue;
            }

            // Check if the placeholder ends or starts.
            if (ch == closure.end || ch == closure.start) {
                // If the placeholder ends.
                if (stage == 2) {
                    String parameter = identifier.toString();
                    String translated = handler.onRequest(player, parameter);

                    if (translated == null) builder.append(identifier);
                    else builder.append(translated);

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
