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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.impl.cloud.CommandECloud;
import me.clip.placeholderapi.commands.impl.local.CommandDump;
import me.clip.placeholderapi.commands.impl.local.CommandExpansionRegister;
import me.clip.placeholderapi.commands.impl.local.CommandExpansionUnregister;
import me.clip.placeholderapi.commands.impl.local.CommandHelp;
import me.clip.placeholderapi.commands.impl.local.CommandInfo;
import me.clip.placeholderapi.commands.impl.local.CommandList;
import me.clip.placeholderapi.commands.impl.local.CommandParse;
import me.clip.placeholderapi.commands.impl.local.CommandReload;
import me.clip.placeholderapi.commands.impl.local.CommandVersion;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class PlaceholderCommandRouter implements CommandExecutor, TabCompleter {

  @Unmodifiable
  private static final List<PlaceholderCommand> COMMANDS = ImmutableList.of(new CommandHelp(),
      new CommandInfo(),
      new CommandList(),
      new CommandDump(),
      new CommandECloud(),
      new CommandParse(),
      new CommandReload(),
      new CommandVersion(),
      new CommandExpansionRegister(),
      new CommandExpansionUnregister());


  @NotNull
  private final PlaceholderAPIPlugin plugin;
  @NotNull
  @Unmodifiable
  private final Map<String, PlaceholderCommand> commands;


  public PlaceholderCommandRouter(@NotNull final PlaceholderAPIPlugin plugin) {
    this.plugin = plugin;

    final ImmutableMap.Builder<String, PlaceholderCommand> commands = ImmutableMap.builder();

    for (final PlaceholderCommand command : COMMANDS) {
      command.getLabels().forEach(label -> commands.put(label, command));
    }

    this.commands = commands.build();
  }


  @Override
  public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command,
      @NotNull final String alias, @NotNull final String[] args) {
    if (args.length == 0) {
      final PlaceholderCommand fallback = commands.get("version");
      if (fallback != null) {
        fallback.evaluate(plugin, sender, "", Collections.emptyList());
      }

      return true;
    }

    final String search = args[0].toLowerCase(Locale.ROOT);
    final PlaceholderCommand target = commands.get(search);

    if (target == null) {
      Msg.msg(sender, "&cUnknown command &7" + search);
      return true;
    }

    final String permission = target.getPermission();
    if (permission != null && !permission.isEmpty() && !sender.hasPermission(permission)) {
      Msg.msg(sender, "&cYou do not have permission to do this!");
      return true;
    }

    target
        .evaluate(plugin, sender, search, Arrays.asList(Arrays.copyOfRange(args, 1, args.length)));

    return true;
  }

  @Override
  public List<String> onTabComplete(@NotNull final CommandSender sender,
      @NotNull final Command command, @NotNull final String alias, @NotNull final String[] args) {
    final List<String> suggestions = new ArrayList<>();

    if (args.length > 1) {
      final PlaceholderCommand target = this.commands.get(args[0].toLowerCase(Locale.ROOT));

      if (target != null) {
        target.complete(plugin, sender, args[0].toLowerCase(Locale.ROOT),
            Arrays.asList(Arrays.copyOfRange(args, 1, args.length)), suggestions);
      }

      return suggestions;
    }

    final Stream<String> targets = PlaceholderCommand
        .filterByPermission(sender, commands.values().stream()).map(PlaceholderCommand::getLabels)
        .flatMap(Collection::stream);
    PlaceholderCommand.suggestByParameter(targets, suggestions, args.length == 0 ? null : args[0]);

    return suggestions;
  }

}
