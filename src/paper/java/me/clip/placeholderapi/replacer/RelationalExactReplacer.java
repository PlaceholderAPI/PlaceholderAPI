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

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.regex.Pattern;

public final class RelationalExactReplacer {
    private static final Pattern DELIMITER = Pattern.compile("_");

    @NotNull
    public String apply(@NotNull String text, @Nullable final Player player1,
                        @Nullable final Player player2, @NotNull final Function<String,
                        @Nullable PlaceholderExpansion> lookup) {
        final String[] parts = DELIMITER.split(text);
        final PlaceholderExpansion expansion;

        if (parts.length == 0) {
            expansion = lookup.apply(text);
        } else {
            expansion = lookup.apply(parts[0]);
        }

        if (expansion == null) {
            return "%rel_" + text + '%';
        }

        if (!(expansion instanceof Relational)) {
            return "%rel_" + text + '%';
        }

        final String params;

        if (text.endsWith("_")) {
            params = "";
        } else {
            params = text.substring(text.indexOf('_') + 1);
        }

        final String result = ((Relational) expansion).onPlaceholderRequest(player1, player2, params);

        if (result == null) {
            return "%rel_" + text + '%';
        }

        return result;
    }
}
