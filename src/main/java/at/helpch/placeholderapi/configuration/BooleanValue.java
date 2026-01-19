package at.helpch.placeholderapi.configuration;

import org.jetbrains.annotations.NotNull;

public record BooleanValue(@NotNull String trueValue, @NotNull String falseValue) {
}
