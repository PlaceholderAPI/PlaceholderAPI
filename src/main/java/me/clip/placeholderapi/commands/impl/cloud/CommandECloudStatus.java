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

import java.util.List;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.expansion.manager.CloudExpansionManager;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class CommandECloudStatus extends PlaceholderCommand {

  public CommandECloudStatus() {
    super("status");
  }

  @Override
  public void evaluate(@NotNull final PlaceholderAPIPlugin plugin,
      @NotNull final CommandSender sender, @NotNull final String alias,
      @NotNull @Unmodifiable final List<String> params) {
    final CloudExpansionManager manager = plugin.getCloudExpansionManager();

    final int updateCount = manager.getCloudUpdateCount();
    final int authorCount = manager.getCloudExpansionAuthorCount();
    final int expansionCount = manager.getCloudExpansions().size();

    final StringBuilder builder = new StringBuilder();

    builder.append("&bThere are &a").append(expansionCount)
        .append("&b expansions available on the eCloud.").append('\n');
    builder.append("&7A total of &f").append(authorCount)
        .append("&7 authors have contributed to the eCloud.").append('\n');

    if (updateCount > 0) {
      builder.append("&eYou have &f").append(updateCount)
          .append(updateCount > 1 ? "&e expansions" : "&e expansion").append(" installed that ")
          .append(updateCount > 1 ? "have an" : "has an").append(" update available.");
    }

    Msg.msg(sender, builder.toString());
  }

}
