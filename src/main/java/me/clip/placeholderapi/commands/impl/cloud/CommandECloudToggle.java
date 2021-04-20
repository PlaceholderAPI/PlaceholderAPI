/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2021 PlaceholderAPI Team
 *
 * PlaceholderAPI is free software: you can redistribute it and/or modify
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
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class CommandECloudToggle extends PlaceholderCommand {

  public CommandECloudToggle() {
    super("toggle", "enable", "disable");
  }

  @Override
  public void evaluate(@NotNull final PlaceholderAPIPlugin plugin,
      @NotNull final CommandSender sender, @NotNull final String alias,
      @NotNull @Unmodifiable final List<String> params) {
    final boolean desiredState;
    final boolean currentState = plugin.getPlaceholderAPIConfig().isCloudEnabled();

    switch (alias.toLowerCase()) {
      case "enable":
        desiredState = true;
        break;
      case "disable":
        desiredState = false;
        break;
      default:
        desiredState = !currentState;
        break;
    }

    if (desiredState == currentState) {
      Msg.msg(sender, "&7The eCloud Manager is already " + (desiredState ? "enabled" : "disabled"));
      return;
    }

    plugin.getPlaceholderAPIConfig().setCloudEnabled(desiredState);

    if (desiredState) {
      plugin.getCloudExpansionManager().load();
    } else {
      plugin.getCloudExpansionManager().kill();
    }

    Msg.msg(sender, "&aThe eCloud Manager has been " + (desiredState ? "enabled" : "disabled"));
  }

}
