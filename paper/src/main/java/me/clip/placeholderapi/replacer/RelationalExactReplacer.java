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
