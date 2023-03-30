/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2021 PlaceholderAPI Team
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

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.manager.LocalExpansionManager;
import me.clip.placeholderapi.util.Futures;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class CommandExpansionRegister extends PlaceholderCommand {

  public CommandExpansionRegister() {
    super("register");
  }

  @Override
  public void evaluate(@NotNull final PlaceholderAPIPlugin plugin,
      @NotNull final CommandSender sender, @NotNull final String alias,
      @NotNull @Unmodifiable final List<String> params) {
    if (params.size() < 1) {
      Msg.msg(sender,
          "&cYou must specify the name of an expansion file.");
      return;
    }

    final LocalExpansionManager manager = plugin.getLocalExpansionManager();

    final File file = new File(manager.getExpansionsFolder(), params.get(0));
    if (!file.exists() || !file.getParentFile().equals(manager.getExpansionsFolder())) {
      Msg.msg(sender,
          "&cThe file &f" + file.getName() + "&c doesn't exist!");
      return;
    }

    Futures.onMainThread(plugin, manager.findExpansionInFile(file), (clazz, exception) -> {
      if (exception != null) {
        Msg.msg(sender,
            "&cFailed to find expansion in file: &f" + file);

        plugin.getLogger()
            .log(Level.WARNING, "failed to find expansion in file: " + file, exception);
        return;
      }

      if (clazz == null) {
        Msg.msg(sender,
            "&cNo expansion class found in file: &f" + file);
        return;
      }

      final Optional<PlaceholderExpansion> expansion = manager.register(clazz);
      if (!expansion.isPresent()) {
        Msg.msg(sender,
            "&cFailed to register expansion from &f" + params.get(0));
        return;
      }

      Msg.msg(sender,
          "&aSuccessfully registered expansion: &f" + expansion.get().getName());

    });
  }

  @Override
  public void complete(@NotNull final PlaceholderAPIPlugin plugin,
      @NotNull final CommandSender sender, @NotNull final String alias,
      @NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions) {
    if (params.size() > 1) {
      return;
    }

    final String[] fileNames = plugin.getLocalExpansionManager().getExpansionsFolder()
        .list((dir, name) -> name.endsWith(".jar"));
    if (fileNames == null || fileNames.length == 0) {
      return;
    }

    suggestByParameter(Arrays.stream(fileNames), suggestions,
        params.isEmpty() ? null : params.get(0));
  }

}
