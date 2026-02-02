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

package at.helpch.placeholderapi.commands;

import java.util.*;
import java.util.stream.Stream;

import at.helpch.placeholderapi.PlaceholderAPIPlugin;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public abstract class PlaceholderCommand {

    @NotNull
    private final String label;
    @NotNull
    private final Set<String> alias;

    private Set<String> permissions = new HashSet<>();


    protected PlaceholderCommand(@NotNull final String label, @NotNull final String... alias) {
        this.label = label;
        this.alias = Set.of(alias);

        setPermissions("placeholderapi.*");
    }

    @NotNull
    public static Stream<PlaceholderCommand> filterByPermission(@NotNull final CommandSender sender,
                                                                @NotNull final Stream<PlaceholderCommand> commands) {
        return commands.filter(target -> target.getPermissions().stream().anyMatch(sender::hasPermission));
    }

    public static void suggestByParameter(@NotNull final Stream<String> possible,
                                          @NotNull final List<String> suggestions, @Nullable final String parameter) {
        if (parameter == null) {
            possible.forEach(suggestions::add);
        } else {
            possible.filter(suggestion -> suggestion.toLowerCase(Locale.ROOT).startsWith(parameter.toLowerCase(Locale.ROOT)))
                    .forEach(suggestions::add);
        }
    }

    @NotNull
    public final String getLabel() {
        return label;
    }

    @NotNull
    @Unmodifiable
    public final Set<String> getAlias() {
        return Set.copyOf(alias);
    }

    @NotNull
    @Unmodifiable
    public final Set<String> getLabels() {
        final Set<String> set = new HashSet<>();
        set.add(label);
        set.addAll(alias);
        return set;
    }

    @NotNull
    public final Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(@NotNull final String @NotNull ... permissions) {
        this.permissions.addAll(Arrays.asList(permissions));
    }

    public void evaluate(@NotNull final PlaceholderAPIPlugin plugin,
                         @NotNull final CommandSender sender, @NotNull final String alias,
                         @NotNull @Unmodifiable final List<String> params) {

    }

    public void complete(@NotNull final PlaceholderAPIPlugin plugin,
                         @NotNull final CommandSender sender, @NotNull final String alias,
                         @NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions) {

    }
}
