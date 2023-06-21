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

package me.clip.placeholderapi.commands.impl.cloud;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class CommandECloudDownload extends PlaceholderCommand {

  public CommandECloudDownload() {
    super("download");
  }

  private boolean isBlockedExpansion(String name) {
    String env = System.getenv("PAPI_BLOCKED_EXPANSIONS");
    if (env == null) {
      return false;
    }

    return Arrays.stream(env.split(","))
            .anyMatch(s -> s.equalsIgnoreCase(name));
  }

  @Override
  public void evaluate(@NotNull final PlaceholderAPIPlugin plugin,
      @NotNull final CommandSender sender, @NotNull final String alias,
      @NotNull @Unmodifiable final List<String> params) {
    if (params.isEmpty()) {
      Msg.msg(sender,
          "&cYou must supply the name of an expansion.");
      return;
    }

    if (isBlockedExpansion(params.get(0))) {
      Msg.msg(sender,
          "&cThis expansion can't be downloaded.");
      return;
    }

    final CloudExpansion expansion = plugin.getCloudExpansionManager()
        .findCloudExpansionByName(params.get(0)).orElse(null);
    if (expansion == null) {
      Msg.msg(sender,
          "&cFailed to find an expansion named: &f" + params.get(0));
      return;
    }

    if (!expansion.isVerified() && !plugin.getPlaceholderAPIConfig().cloudAllowUnverifiedExpansions()) {
      Msg.msg(sender, "&cThe expansion '&f" + params.get(0) + "&c' is not verified and can only be downloaded manually from &fhttps://placeholderapi.com/ecloud");
      return;
    }

    final CloudExpansion.Version version;
    if (params.size() < 2) {
      version = expansion.getVersion(expansion.getLatestVersion());
      if (version == null) {
        Msg.msg(sender,
            "&cCould not find latest version for expansion.");
        return;
      }
    } else {
      version = expansion.getVersion(params.get(1));
      if (version == null) {
        Msg.msg(sender,
            "&cCould not find specified version: &f" + params.get(1),
            "&7Available versions: &f" + expansion.getAvailableVersions());
        return;
      }
    }

    plugin.getCloudExpansionManager().downloadExpansion(expansion, version)
        .whenComplete((file, exception) -> {
          if (exception != null) {
            Msg.msg(sender,
                "&cFailed to download expansion: &f" + exception.getMessage());
            return;
          }

          Msg.msg(sender,
              "&aSuccessfully downloaded expansion &f" + expansion.getName() + " [" + version
                  .getVersion() + "] &ato file: &f" + file.getName(),
              "&aMake sure to type &f/papi reload &ato enable your new expansion!");

          plugin.getCloudExpansionManager().load();
        });
  }

  @Override
  public void complete(@NotNull final PlaceholderAPIPlugin plugin,
      @NotNull final CommandSender sender, @NotNull final String alias,
      @NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions) {
    if (params.size() > 2) {
      return;
    }

    if (params.size() <= 1) {
      final Stream<String> names = plugin.getCloudExpansionManager().getCloudExpansions().values()
          .stream().map(CloudExpansion::getName).map(name -> name.replace(' ', '_'));
      suggestByParameter(names, suggestions, params.isEmpty() ? null : params.get(0));
      return;
    }

    final Optional<CloudExpansion> expansion = plugin.getCloudExpansionManager()
        .findCloudExpansionByName(params.get(0));
    if (!expansion.isPresent()) {
      return;
    }

    suggestByParameter(expansion.get().getAvailableVersions().stream(), suggestions, params.get(1));
  }

}
