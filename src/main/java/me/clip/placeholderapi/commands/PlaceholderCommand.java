/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2024 PlaceholderAPI Team
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

package me.clip.placeholderapi.commands;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public abstract class PlaceholderCommand {

  @NotNull
  private final String label;
  @NotNull
  private final Set<String> alias;

  @Nullable
  private String permission;


  protected PlaceholderCommand(@NotNull final String label, @NotNull final String... alias) {
    this.label = label;
    this.alias = Sets.newHashSet(alias);

    setPermission("placeholderapi." + label);
  }

  @NotNull
  public static Stream<PlaceholderCommand> filterByPermission(@NotNull final CommandSender sender,
      @NotNull final Stream<PlaceholderCommand> commands) {
    return commands.filter(
        target -> target.getPermission() == null || sender.hasPermission(target.getPermission()));
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
    return ImmutableSet.copyOf(alias);
  }

  @NotNull
  @Unmodifiable
  public final Set<String> getLabels() {
    return ImmutableSet.<String>builder().add(label).addAll(alias).build();
  }

  @Nullable
  public final String getPermission() {
    return permission;
  }

  public void setPermission(@NotNull final String permission) {
    this.permission = permission;
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
