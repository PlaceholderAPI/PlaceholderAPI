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
import java.util.Optional;
import java.util.stream.Stream;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class CommandECloudExpansionInfo extends PlaceholderCommand {

  public CommandECloudExpansionInfo() {
    super("info");
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

    final StringBuilder builder = new StringBuilder();

    builder.append("&bExpansion: &f")
        .append(expansion.shouldUpdate() ? "&e" : "&a")
        .append(expansion.getName())
        .append('\n')
        .append("&bAuthor: &f")
        .append(expansion.getAuthor())
        .append('\n')
        .append("&bVerified: ")
        .append(expansion.isVerified() ? "&a&l✔" : "&c&l❌")
        .append('\n');

    if (params.size() < 2) {
      builder.append("&bLatest Version: &f")
          .append(expansion.getLatestVersion())
          .append('\n')
          .append("&bReleased: &f")
          .append(expansion.getTimeSinceLastUpdate())
          .append(" ago")
          .append('\n')
          .append("&bRelease Notes: &f")
          .append(expansion.getVersion().getReleaseNotes())
          .append('\n');
    } else {
      final CloudExpansion.Version version = expansion.getVersion(params.get(1));
      if (version == null) {
        Msg.msg(sender,
            "&cCould not find specified version: &f" + params.get(1),
            "&aVersions: &f" + expansion.getAvailableVersions());
        return;
      }

      builder.append("&bVersion: &f")
          .append(version.getVersion())
          .append('\n')
          .append("&bRelease Notes: &f")
          .append(version.getReleaseNotes())
          .append('\n')
          .append("&bDownload URL: &f")
          .append(version.getUrl())
          .append('\n');
    }

    Msg.msg(sender, builder.toString());
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
