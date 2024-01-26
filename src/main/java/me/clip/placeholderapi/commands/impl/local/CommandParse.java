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

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public final class CommandParse extends PlaceholderCommand {

  public CommandParse() {
    super("parse", "bcparse", "parserel", "cmdparse");
  }


  @Override
  public void evaluate(@NotNull final PlaceholderAPIPlugin plugin,
      @NotNull final CommandSender sender, @NotNull final String alias,
      @NotNull @Unmodifiable final List<String> params) {
    switch (alias.toLowerCase(Locale.ROOT)) {
      case "parserel":
        evaluateParseRelation(sender, params);
        break;
      case "parse":
        evaluateParseSingular(sender, params, false, false);
        break;
      case "bcparse":
        evaluateParseSingular(sender, params, true, false);
        break;
      case "cmdparse":
        evaluateParseSingular(sender, params, false, true);
        break;
    }
  }

  @Override
  public void complete(@NotNull final PlaceholderAPIPlugin plugin,
      @NotNull final CommandSender sender, @NotNull final String alias,
      @NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions) {
    switch (alias.toLowerCase(Locale.ROOT)) {
      case "parserel":
        completeParseRelation(params, suggestions);
        break;
      case "parse":
      case "bcparse":
      case "cmdparse":
        completeParseSingular(sender, params, suggestions);
        break;
    }
  }


  private void evaluateParseSingular(@NotNull final CommandSender sender,
      @NotNull @Unmodifiable final List<String> params, final boolean broadcast,
      final boolean command) {
    if (params.size() < 2) {
      Msg.msg(sender,
          "&cYou must supply a target, and a message: &b/papi " + (broadcast ? "bcparse" : "parse")
              + " &7{target} &a{message}");
      return;
    }

    OfflinePlayer player;

    if ("me".equalsIgnoreCase(params.get(0))) {
      if (!(sender instanceof Player)) {
        Msg.msg(sender, "&cYou must be a player to use &7me&c as a target!");
        return;
      }

      player = ((Player) sender);
    } else if ("--null".equalsIgnoreCase(params.get(0))) {
      player = null;
    } else {
      final OfflinePlayer target = resolvePlayer(params.get(0));
      if (target == null) {
        Msg.msg(sender, "&cFailed to find player: &7" + params.get(0));
        return;
      }

      player = target;
    }

    final String message = PlaceholderAPI
        .setPlaceholders(player, String.join(" ", params.subList(1, params.size())));

    if (command) {
      Bukkit.dispatchCommand(sender, message);
      return;
    }

    if (broadcast) {
      Bukkit.broadcastMessage(message);
    } else {
      sender.sendMessage(message);
    }
  }

  private void evaluateParseRelation(@NotNull final CommandSender sender,
      @NotNull @Unmodifiable final List<String> params) {
    if (params.size() < 3) {
      Msg.msg(sender,
          "&cYou must supply two targets, and a message: &b/papi parserel &7{target one} {target two} &a{message}");
      return;
    }

    final OfflinePlayer targetOne = resolvePlayer(params.get(0));
    if (targetOne == null || !targetOne.isOnline()) {
      Msg.msg(sender, "&cFailed to find player: &f" + params.get(0));
      return;
    }

    final OfflinePlayer targetTwo = resolvePlayer(params.get(1));
    if (targetTwo == null || !targetTwo.isOnline()) {
      Msg.msg(sender, "&cFailed to find player: &f" + params.get(1));
      return;
    }

    final String message = PlaceholderAPI
        .setRelationalPlaceholders(((Player) targetOne), ((Player) targetTwo),
            String.join(" ", params.subList(2, params.size())));
    
    sender.sendMessage(message);
  }


  private void completeParseSingular(@NotNull final CommandSender sender,
      @NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions) {
    if (params.size() <= 1) {
      if (sender instanceof Player && (params.isEmpty() || "me"
          .startsWith(params.get(0).toLowerCase(Locale.ROOT)))) {
        suggestions.add("me");
      }
      
      if ("--null".startsWith(params.get(0).toLowerCase(Locale.ROOT))) {
        suggestions.add("--null");
      }
      
      final Stream<String> names = Bukkit.getOnlinePlayers().stream().map(Player::getName);
      suggestByParameter(names, suggestions, params.isEmpty() ? null : params.get(0));

      return;
    }

    final String name = params.get(params.size() - 1);
    if (!name.startsWith("%") || name.endsWith("%")) {
      return;
    }

    final int index = name.indexOf('_');
    if (index == -1) {
      return; // no arguments supplied yet
    }

    final PlaceholderExpansion expansion = PlaceholderAPIPlugin.getInstance()
        .getLocalExpansionManager().findExpansionByIdentifier(name.substring(1, index))
        .orElse(null);
    if (expansion == null) {
      return;
    }

    final Set<String> possible = new HashSet<>(expansion.getPlaceholders());

    PlaceholderAPIPlugin.getInstance()
        .getCloudExpansionManager()
        .findCloudExpansionByName(expansion.getName())
        .ifPresent(cloud -> possible.addAll(cloud.getPlaceholders()));

    suggestByParameter(possible.stream(), suggestions, params.get(params.size() - 1));
  }

  private void completeParseRelation(@NotNull @Unmodifiable final List<String> params,
      @NotNull final List<String> suggestions) {
    if (params.size() > 2) {
      return;
    }

    final Stream<String> names = Bukkit.getOnlinePlayers().stream().map(Player::getName);
    suggestByParameter(names, suggestions, params.isEmpty() ? null : params.get(params.size() - 1));
  }


  @Nullable
  private OfflinePlayer resolvePlayer(@NotNull final String name) {
    OfflinePlayer target = Bukkit.getPlayerExact(name);
    
    if (target == null) {
      // Not the best option, but Spigot doesn't offer a good replacement (as usual)
      target = Bukkit.getOfflinePlayer(name);
      
      return target.hasPlayedBefore() ? target : null;
    }

    return target;

  }

}
