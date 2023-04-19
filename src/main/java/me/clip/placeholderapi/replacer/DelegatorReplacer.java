package me.clip.placeholderapi.replacer;

import me.clip.placeholderapi.events.PlaceholderRequestEvent;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class DelegatorReplacer implements Replacer {

    private final Replacer defaultReplacer;

    public DelegatorReplacer(Replacer defaultReplacer) {
        this.defaultReplacer = defaultReplacer;
    }


    @Override
    public @NotNull String apply(@NotNull String text, @Nullable OfflinePlayer player, @NotNull Function<String, @Nullable PlaceholderExpansion> lookup) {
        PlaceholderRequestEvent placeholderRequestEvent = new PlaceholderRequestEvent(text, player, defaultReplacer);
        Bukkit.getPluginManager().callEvent(placeholderRequestEvent);
        return placeholderRequestEvent.getOutput() == null ? defaultReplacer.apply(text, player, lookup)
                : placeholderRequestEvent.getOutput();
    }
}
