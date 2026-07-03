package me.clip.placeholderapi.expansion;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TypeHandler<T> {
    Class<T> typeClass();

    default boolean includeDerivatives() {
        return true;
    }

    @Nullable
    String onRequest(@NotNull final PlaceholderContext<T> context);
}
