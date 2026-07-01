package me.clip.placeholderapi.expansion;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PlaceholderContext<T> {
    @Nullable private final Player player;
    @NotNull private final T type;
    @NotNull private final String args;

    public PlaceholderContext(@Nullable final Player player, @NotNull final T type,
                              @NotNull final String args) {
        this.player = player;
        this.type = type;
        this.args = args;
    }

    @Nullable
    public Player player() {
        return player;
    }

    @NotNull
    public T type() {
        return type;
    }

    @NotNull
    public String args() {
        return args;
    }
}
