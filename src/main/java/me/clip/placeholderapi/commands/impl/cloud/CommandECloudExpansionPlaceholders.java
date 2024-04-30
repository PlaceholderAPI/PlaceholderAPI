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

package me.clip.placeholderapi.commands.impl.cloud;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class CommandECloudExpansionPlaceholders extends PlaceholderCommand {

  public CommandECloudExpansionPlaceholders() {
    super("placeholders");
  }

  @Override
  public void evaluate(@NotNull final PlaceholderAPIPlugin plugin,
      @NotNull final CommandSender sender, @NotNull final String alias,
      @NotNull @Unmodifiable final List<String> params) {
    if (params.isEmpty()) {
      Msg.msg(sender,
          "&cYou must specify the name of the expansion.");
      return;
    }

    final CloudExpansion expansion = plugin.getCloudExpansionManager()
        .findCloudExpansionByName(params.get(0)).orElse(null);
    if (expansion == null) {
      Msg.msg(sender,
          "&cThere is no expansion with the name: &f" + params.get(0));
      return;
    }

    final List<String> placeholders = expansion.getPlaceholders();
    if (placeholders == null || placeholders.isEmpty()) {
      Msg.msg(sender,
          "&cThe expansion specified does not have placeholders listed.");
      return;
    }

    final List<List<String>> partitions = Lists
        .partition(placeholders.stream().sorted().collect(Collectors.toList()), 10);

    Msg.msg(sender,
        "&6" + placeholders.size() + "&7 placeholders: &a",
        partitions.stream().map(partition -> String.join(", ", partition))
            .collect(Collectors.joining("\n")));

  }

  @Override
  public void complete(@NotNull final PlaceholderAPIPlugin plugin,
      @NotNull final CommandSender sender, @NotNull final String alias,
      @NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions) {
    if (params.size() > 1) {
      return;
    }

    final Stream<String> names = plugin.getCloudExpansionManager()
        .getCloudExpansions()
        .values()
        .stream()
        .map(CloudExpansion::getName)
        .map(name -> name.replace(' ', '_'));

    suggestByParameter(names, suggestions, params.isEmpty() ? null : params.get(0));
  }

}
