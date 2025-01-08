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

package me.clip.placeholderapi.commands.impl.local;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class CommandList extends PlaceholderCommand {

  public CommandList() {
    super("list");
  }


  @Override
  public void evaluate(@NotNull final PlaceholderAPIPlugin plugin,
      @NotNull final CommandSender sender, @NotNull final String alias,
      @NotNull @Unmodifiable final List<String> params) {
    final Set<String> identifiers = PlaceholderAPI.getRegisteredIdentifiers();
    if (identifiers.isEmpty()) {
      Msg.msg(sender, "&cThere are no placeholder hooks active!");
      return;
    }

    final List<List<String>> partitions = Lists
        .partition(identifiers.stream().sorted().collect(Collectors.toList()), 10);

    Msg.msg(sender,
        "&7A total of &f" + identifiers.size() + "&7 placeholder hook(s) are active: &a",
        partitions.stream().map(partition -> String.join("&7, &a", partition))
            .collect(Collectors.joining("\n")));
  }

}
