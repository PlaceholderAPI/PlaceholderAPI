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

package me.clip.placeholderapi.replacer;

import java.util.Locale;
import java.util.function.Function;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CharsReplacer implements Replacer {

    @NotNull
    private final Closure closure;

    public CharsReplacer(@NotNull final Closure closure) {
        this.closure = closure;
    }

    /**
     * Translates placeholders within the provided text using a high-performance
     * character-scanning approach.
     * * <p>The method identifies placeholders delimited by the defined {@link Closure}
     * (e.g., %identifier_params% or {identifier_params}). If a placeholder is
     * successfully identified, the provided lookup function is used to fetch the
     * corresponding {@link PlaceholderExpansion}.</p>
     *
     * @param text   The raw text containing potential placeholders to be replaced.
     * @param player The {@link OfflinePlayer} to contextually parse the placeholders against.
     * May be {@code null} if no player context is available.
     * @param lookup A function that maps a lowercase identifier string to a registered
     * {@link PlaceholderExpansion}.
     * @return A string with all valid placeholders replaced by their respective values.
     * Returns the original text if no placeholders are found.
     */
    @NotNull
    @Override
    public String apply(@NotNull final String text, @Nullable final OfflinePlayer player,
                        @NotNull final Function<String, @Nullable PlaceholderExpansion> lookup) {
        final char head = closure.head;
        int startPlaceholder = text.indexOf(head);

        if (startPlaceholder == -1) {
            return text;
        }

        final int length = text.length();
        final StringBuilder builder = new StringBuilder(length + (length >> 3));
        int cursor = 0;

        final char tail = closure.tail;

        loop: do {
            // Append plain text preceding the placeholder
            if (startPlaceholder > cursor) {
                builder.append(text, cursor, startPlaceholder);
            }

            final int endPlaceholder = text.indexOf(tail, startPlaceholder + 1);

            if (endPlaceholder == -1) {
                builder.append(text, startPlaceholder, length);
                return builder.toString();
            }

            int underscoreIndex = -1;

            for (int i = startPlaceholder + 1; i < endPlaceholder; i++) {
                final char current = text.charAt(i);

                if (current == ' ') {
                    // Invalid placeholder (contains space).
                    // Treat the opening symbol as literal text and search for the next one.
                    builder.append(head);
                    cursor = startPlaceholder + 1;
                    startPlaceholder = text.indexOf(head, cursor);

                    // Safety check: If no more placeholders exist, break to finalize
                    if (startPlaceholder == -1) {
                        break loop;
                    }
                    continue loop;
                }

                if (current == '_' && underscoreIndex == -1) {
                    underscoreIndex = i;
                }
            }

            if (underscoreIndex == -1) {
                builder.append(text, startPlaceholder, endPlaceholder + 1);
                cursor = endPlaceholder + 1;
                startPlaceholder = text.indexOf(head, cursor);
                continue;
            }

            String identifier = text.substring(startPlaceholder + 1, underscoreIndex);
            String parameters = "";

            if (underscoreIndex + 1 < endPlaceholder) {
                parameters = text.substring(underscoreIndex + 1, endPlaceholder);
            }

            final PlaceholderExpansion expansion = lookup.apply(identifier.toLowerCase(Locale.ROOT));
            String replacement = null;

            if (expansion != null) {
                replacement = expansion.onRequest(player, parameters);
            }

            if (replacement != null) {
                builder.append(replacement);
            } else {
                // Fallback: Restore original placeholder format
                builder.append(head).append(identifier);
                builder.append('_').append(parameters);
                builder.append(tail);
            }

            cursor = endPlaceholder + 1;
            startPlaceholder = text.indexOf(head, cursor);

        } while (startPlaceholder != -1);

        if (cursor < length) {
            builder.append(text, cursor, length);
        }

        return builder.toString();
    }

}