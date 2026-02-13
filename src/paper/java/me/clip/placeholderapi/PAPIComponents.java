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

package me.clip.placeholderapi;

import me.clip.placeholderapi.replacer.ComponentReplacer;
import me.clip.placeholderapi.replacer.ExactReplacer;
import me.clip.placeholderapi.replacer.RelationalExactReplacer;
import me.clip.placeholderapi.replacer.Replacer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static me.clip.placeholderapi.PlaceholderAPI.RELATIONAL_PLACEHOLDER_PATTERN;

public final class PAPIComponents {
    private static final Replacer PERCENT_EXACT_REPLACER = new ExactReplacer('%', '%');
    private static final Replacer BRACKET_EXACT_REPLACER = new ExactReplacer('{', '}');
    private static final RelationalExactReplacer RELATIONAL_EXACT_REPLACER = new RelationalExactReplacer();

    /**
     * Translates all placeholders into their corresponding values.
     * <br>The pattern of a valid placeholder is {@literal %<identifier>_<params>%}.
     *
     * @param player    Player to parse the placeholders against
     * @param component Component to set the placeholder values in
     * @return Component containing all translated placeholders
     */
    @NotNull
    public static Component setPlaceholders(final OfflinePlayer player, @NotNull final Component component) {
        return setPlaceholders(player, component, null);
    }

    /**
     * Translates all placeholders into their corresponding values.
     * <br>The pattern of a valid placeholder is {@literal %<identifier>_<params>%}.
     *
     * @param player     Player to parse the placeholders against
     * @param component  Component to set the placeholder values in
     * @param serializer Optional function to serialize parsed placeholder values into ComponentLike
     * @return Component containing all translated placeholders
     */
    @NotNull
    public static Component setPlaceholders(final OfflinePlayer player, @NotNull final Component component, @Nullable Function<String, ComponentLike> serializer) {
        if (PlaceholderAPIPlugin.getInstance().getPlaceholderAPIConfig().useAdventureProvidedReplacer()) {
            return component.replaceText(config -> config.match(PlaceholderAPI.PLACEHOLDER_PATTERN).replacement((result, builder) -> {
                String parsed = PERCENT_EXACT_REPLACER.apply(result.group(), player, PlaceholderAPIPlugin.getInstance().getLocalExpansionManager()::getExpansion);
                return serializer == null ? builder.content(parsed) : serializer.apply(parsed);
            }));
        }

        return ComponentReplacer.replace(component, str -> PlaceholderAPI.setPlaceholders(player, str), serializer == null ? null : s -> serializer.apply(s).asComponent());
    }

    /**
     * Translates all placeholders into their corresponding values.
     * <br>The pattern of a valid placeholder is {@literal %<identifier>_<params>%}.
     *
     * @param player     Player to parse the placeholders against
     * @param components List of Components to set the placeholder values in
     * @return List of Components containing all translated placeholders
     */
    @NotNull
    public static List<Component> setPlaceholders(final OfflinePlayer player, @NotNull final List<Component> components) {
        return setPlaceholders(player, components, null);
    }

    /**
     * Translates all placeholders into their corresponding values.
     * <br>The pattern of a valid placeholder is {@literal %<identifier>_<params>%}.
     *
     * @param player     Player to parse the placeholders against
     * @param components List of Components to set the placeholder values in
     * @param serializer Optional function to serialize parsed placeholder values into ComponentLike
     * @return List of Components containing all translated placeholders
     */
    @NotNull
    public static List<Component> setPlaceholders(final OfflinePlayer player, @NotNull final List<Component> components, @Nullable Function<String, ComponentLike> serializer) {
        return components.stream().map(component -> setPlaceholders(player, component, serializer == null ? null : s -> serializer.apply(s).asComponent())).collect(Collectors.toList());
    }

    /**
     * Translates all placeholders into their corresponding values.
     * <br>The pattern of a valid placeholder is {@literal %<identifier>_<params>%}.
     *
     * @param player    Player to parse the placeholders against
     * @param component Component to set the placeholder values in
     * @return Component containing all translated placeholders
     */
    @NotNull
    public static Component setPlaceholders(final Player player, @NotNull final Component component) {
        return setPlaceholders(player, component, null);
    }

    /**
     * Translates all placeholders into their corresponding values.
     * <br>The pattern of a valid placeholder is {@literal %<identifier>_<params>%}.
     *
     * @param player     Player to parse the placeholders against
     * @param component  Component to set the placeholder values in
     * @param serializer Optional function to serialize parsed placeholder values into ComponentLike
     * @return Component containing all translated placeholders
     */
    @NotNull
    public static Component setPlaceholders(final Player player, @NotNull final Component component, @Nullable Function<String, ComponentLike> serializer) {
        return setPlaceholders((OfflinePlayer) player, component, serializer);
    }

    /**
     * Translates all placeholders into their corresponding values.
     * <br>The pattern of a valid placeholder is {@literal %<identifier>_<params>%}.
     *
     * @param player     Player to parse the placeholders against
     * @param components List of Components to set the placeholder values in
     * @return List of components containing all translated placeholders
     */
    @NotNull
    public static List<Component> setPlaceholders(final Player player, @NotNull final List<Component> components) {
        return setPlaceholders(player, components, null);
    }

    /**
     * Translates all placeholders into their corresponding values.
     * <br>The pattern of a valid placeholder is {@literal %<identifier>_<params>%}.
     *
     * @param player     Player to parse the placeholders against
     * @param components List of Components to set the placeholder values in
     * @param serializer Optional function to serialize parsed placeholder values into ComponentLike
     * @return List of components containing all translated placeholders
     */
    @NotNull
    public static List<Component> setPlaceholders(final Player player, @NotNull final List<Component> components, @Nullable Function<String, ComponentLike> serializer) {
        return setPlaceholders((OfflinePlayer) player, components, serializer);
    }

    /**
     * Translates all placeholders into their corresponding values.
     * <br>The pattern of a valid placeholder is {@literal {<identifier>_<params>}}.
     *
     * @param player    Player to parse the placeholders against
     * @param component Component to set the placeholder values in
     * @return Component containing all translated placeholders
     */
    @NotNull
    public static Component setBracketPlaceholders(final OfflinePlayer player, @NotNull final Component component) {
        return setBracketPlaceholders(player, component, null);
    }

    /**
     * Translates all placeholders into their corresponding values.
     * <br>The pattern of a valid placeholder is {@literal {<identifier>_<params>}}.
     *
     * @param player     Player to parse the placeholders against
     * @param component  Component to set the placeholder values in
     * @param serializer Optional function to serialize parsed placeholder values into ComponentLike
     * @return Component containing all translated placeholders
     */
    @NotNull
    public static Component setBracketPlaceholders(final OfflinePlayer player, @NotNull final Component component, @Nullable Function<String, ComponentLike> serializer) {
        if (PlaceholderAPIPlugin.getInstance().getPlaceholderAPIConfig().useAdventureReplacer()) {
            return component.replaceText(config -> config.match(PlaceholderAPI.BRACKET_PLACEHOLDER_PATTERN).replacement((result, builder) ->
                    builder.content(BRACKET_EXACT_REPLACER.apply(result.group(), player, PlaceholderAPIPlugin.getInstance().getLocalExpansionManager()::getExpansion))));
        }

        return ComponentReplacer.replace(component, str -> PlaceholderAPI.setBracketPlaceholders(player, str), serializer == null ? null : s -> serializer.apply(s).asComponent());
    }

    /**
     * Translates all placeholders into their corresponding values.
     * <br>The pattern of a valid placeholder is {@literal {<identifier>_<params>}}.
     *
     * @param player     Player to parse the placeholders against
     * @param components List of Components to set the placeholder values in
     * @return List of Components containing all translated placeholders
     */
    @NotNull
    public static List<Component> setBracketPlaceholders(final OfflinePlayer player, @NotNull final List<Component> components) {
        return setBracketPlaceholders(player, components, null);
    }

    /**
     * Translates all placeholders into their corresponding values.
     * <br>The pattern of a valid placeholder is {@literal {<identifier>_<params>}}.
     *
     * @param player     Player to parse the placeholders against
     * @param components List of Components to set the placeholder values in
     * @param serializer Optional function to serialize parsed placeholder values into ComponentLike
     * @return List of Components containing all translated placeholders
     */
    @NotNull
    public static List<Component> setBracketPlaceholders(final OfflinePlayer player, @NotNull final List<Component> components, @Nullable Function<String, ComponentLike> serializer) {
        return components.stream().map(component -> setBracketPlaceholders(player, component, serializer)).collect(Collectors.toList());
    }

    /**
     * Translates all placeholders into their corresponding values.
     * <br>The pattern of a valid placeholder is {@literal {<identifier>_<params>}}.
     *
     * @param player    Player to parse the placeholders against
     * @param component Component to set the placeholder values in
     * @return Component containing all translated placeholders
     */
    @NotNull
    public static Component setBracketPlaceholders(final Player player, @NotNull final Component component) {
        return setBracketPlaceholders(player, component, null);
    }

    /**
     * Translates all placeholders into their corresponding values.
     * <br>The pattern of a valid placeholder is {@literal {<identifier>_<params>}}.
     *
     * @param player     Player to parse the placeholders against
     * @param component  Component to set the placeholder values in
     * @param serializer Optional function to serialize parsed placeholder values into ComponentLike
     * @return Component containing all translated placeholders
     */
    @NotNull
    public static Component setBracketPlaceholders(final Player player, @NotNull final Component component, @Nullable Function<String, ComponentLike> serializer) {
        return setBracketPlaceholders((OfflinePlayer) player, component, serializer);
    }

    /**
     * Translates all placeholders into their corresponding values.
     * <br>The pattern of a valid placeholder is {@literal {<identifier>_<params>}}.
     *
     * @param player     Player to parse the placeholders against
     * @param components List of Components to set the placeholder values in
     * @return List of Components containing all translated placeholders
     */
    @NotNull
    public static List<Component> setBracketPlaceholders(final Player player, @NotNull final List<Component> components) {
        return setBracketPlaceholders(player, components, null);
    }

    /**
     * Translates all placeholders into their corresponding values.
     * <br>The pattern of a valid placeholder is {@literal {<identifier>_<params>}}.
     *
     * @param player     Player to parse the placeholders against
     * @param components List of Components to set the placeholder values in
     * @param serializer Optional function to serialize parsed placeholder values into ComponentLike
     * @return List of Components containing all translated placeholders
     */
    @NotNull
    public static List<Component> setBracketPlaceholders(final Player player, @NotNull final List<Component> components, @Nullable Function<String, ComponentLike> serializer) {
        return setBracketPlaceholders((OfflinePlayer) player, components, serializer);
    }

    /**
     * set relational placeholders in the text specified placeholders are matched with the pattern
     * {@literal %<rel_(identifier)_(params)>%} when set with this method
     *
     * @param one       First player to compare
     * @param two       Second player to compare
     * @param component Component to parse the placeholders in
     * @return The Component containing the parsed relational placeholders
     */
    public static Component setRelationalPlaceholders(Player one, Player two, Component component) {
        //todo: custom replacer
        return component.replaceText(config -> config.match(RELATIONAL_PLACEHOLDER_PATTERN).replacement((result, builder) ->
                builder.content(RELATIONAL_EXACT_REPLACER.apply(result.group(2), one, two, PlaceholderAPIPlugin.getInstance().getLocalExpansionManager()::getExpansion))));
    }

    /**
     * Translate placeholders in the provided List based on the relation of the two provided players.
     * <br>The pattern of a valid placeholder is {@literal %rel_<identifier>_<param>%}.
     *
     * @param one        Player to compare
     * @param two        Player to compare
     * @param components List of Components to parse the placeholder values to
     * @return The List of Components containing the parsed relational placeholders
     */
    public static List<Component> setRelationalPlaceholders(Player one, Player two, List<Component> components) {
        return components.stream().map(line -> setRelationalPlaceholders(one, two, line))
                .collect(Collectors.toList());
    }
}