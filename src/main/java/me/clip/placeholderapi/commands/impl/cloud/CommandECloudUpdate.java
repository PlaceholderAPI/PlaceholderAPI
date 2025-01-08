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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.util.Futures;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

/**
 * please don't flame me for this code, I will fix this shit later.
 */
public final class CommandECloudUpdate extends PlaceholderCommand {

  public CommandECloudUpdate() {
    super("update");
  }

  private static CompletableFuture<List<@Nullable Class<? extends PlaceholderExpansion>>> downloadAndDiscover(
      @NotNull final List<CloudExpansion> expansions, @NotNull final PlaceholderAPIPlugin plugin) {
    return expansions.stream()
        .map(expansion -> plugin.getCloudExpansionManager()
            .downloadExpansion(expansion, expansion.getVersion()))
        .map(future -> future.thenCompose(plugin.getLocalExpansionManager()::findExpansionInFile))
        .collect(Futures.collector());
  }

  @Override
  public void evaluate(@NotNull final PlaceholderAPIPlugin plugin,
      @NotNull final CommandSender sender, @NotNull final String alias,
      @NotNull @Unmodifiable final List<String> params) {
    if (params.isEmpty()) {
      Msg.msg(sender,
          "&cYou must define 'all' or the name of an expansion to update.");
      return;
    }

    final boolean multiple = params.get(0).equalsIgnoreCase("all");
    final List<CloudExpansion> expansions = new ArrayList<>();

    // gather target expansions
    if (multiple) {
      expansions.addAll(plugin.getCloudExpansionManager().getCloudExpansionsInstalled().values());
    } else {
      plugin.getCloudExpansionManager().findCloudExpansionByName(params.get(0))
          .ifPresent(expansions::add);
    }

    // remove the ones that are the latest version
    expansions.removeIf(expansion -> !expansion.shouldUpdate());

    if (expansions.isEmpty()) {
      Msg.msg(sender,
          "&cNo updates available for " + (!multiple ? "this expansion."
              : "your active expansions."));
      return;
    }

    Msg.msg(sender,
        "&aUpdating expansions: " + expansions.stream().map(CloudExpansion::getName)
            .collect(Collectors.joining("&7, &6", "&8[&6", "&8]&r")));

    Futures.onMainThread(plugin, downloadAndDiscover(expansions, plugin), (classes, exception) -> {
      if (exception != null) {
        Msg.msg(sender,
            "&cFailed to update expansions: &e" + exception.getMessage());
        return;
      }

      Msg.msg(sender,
          "&aSuccessfully downloaded updates, registering new versions.");

      final String message = classes.stream()
          .filter(Objects::nonNull)
          .map(plugin.getLocalExpansionManager()::register)
          .filter(Optional::isPresent)
          .map(Optional::get)
          .map(expansion -> "  &a" + expansion.getName() + " &f" + expansion.getVersion())
          .collect(Collectors.joining("\n"));

      Msg.msg(sender,
          "&7Registered expansions:", message);

    });
  }

  @Override
  public void complete(@NotNull final PlaceholderAPIPlugin plugin,
      @NotNull final CommandSender sender, @NotNull final String alias,
      @NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions) {
    if (params.size() > 1) {
      return;
    }

    final List<CloudExpansion> installed = Lists
        .newArrayList(plugin.getCloudExpansionManager().getCloudExpansionsInstalled().values());
    installed.removeIf(expansion -> !expansion.shouldUpdate());

    if (!installed.isEmpty() && (params.isEmpty() || "all"
        .startsWith(params.get(0).toLowerCase(Locale.ROOT)))) {
      suggestions.add("all");
    }

    suggestByParameter(
        installed.stream().map(CloudExpansion::getName).map(name -> name.replace(" ", "_")),
        suggestions, params.isEmpty() ? null : params.get(0));
  }

}
