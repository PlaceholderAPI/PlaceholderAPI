package me.clip.placeholderapi.replacer;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.regex.Pattern;

public class ExactReplacer implements Replacer {
    private static final Pattern DELIMITER = Pattern.compile("_");

    @NotNull
    @Override
    public String apply(@NotNull String text, @Nullable final OfflinePlayer player,
                        @NotNull final Function<String, @Nullable PlaceholderExpansion> lookup) {
        text = text.substring(1, text.length() - 1);
        final String[] parts = DELIMITER.split(text);
        final PlaceholderExpansion expansion;

        if (parts.length == 0) {
            expansion = lookup.apply(text);
        } else {
            expansion = lookup.apply(parts[0]);
        }

        if (expansion == null) {
            return text;
        }

        final String params;

        if (text.endsWith("_")) {
            params = "";
        } else {
            params = text.substring(text.indexOf('_') + 1);
        }

        final String result = expansion.onRequest(player, params);

        if (result == null) {
            return text;
        }

        return result;
    }
}
