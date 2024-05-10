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

import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.logging.Level;
import java.util.stream.Collectors;

public final class CommandDump extends PlaceholderCommand {

  @NotNull
  private static final String URL = "https://paste.helpch.at/";

  @NotNull
  private static final Gson gson = new Gson();

  @NotNull
  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter
      .ofLocalizedDateTime(FormatStyle.LONG)
      .withLocale(Locale.US)
      .withZone(ZoneId.of("UTC"));


  public CommandDump() {
    super("dump");
  }

  @Override
  public void evaluate(@NotNull final PlaceholderAPIPlugin plugin,
      @NotNull final CommandSender sender, @NotNull final String alias,
      @NotNull @Unmodifiable final List<String> params) {
    postDump(makeDump(plugin)).whenComplete((key, exception) -> {
      if (exception != null) {
        plugin.getLogger().log(Level.WARNING, "failed to post dump details", exception);

        Msg.msg(sender,
            "&cFailed to post dump details, check console.");
        return;
      }

      Msg.msg(sender,
          "&aSuccessfully posted dump: " + URL + key);
    });
  }

  @NotNull
  private CompletableFuture<String> postDump(@NotNull final String dump) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        final HttpURLConnection connection = ((HttpURLConnection) new URL(URL + "documents")
            .openConnection());
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
        connection.setDoOutput(true);

        connection.connect();

        try (final OutputStream stream = connection.getOutputStream()) {
          stream.write(dump.getBytes(StandardCharsets.UTF_8));
        }

        try (final InputStream stream = connection.getInputStream()) {
          final String json = CharStreams.toString(new InputStreamReader(stream, StandardCharsets.UTF_8));
          return gson.fromJson(json, JsonObject.class).get("key").getAsString();
        }
      } catch (final IOException ex) {
        throw new CompletionException(ex);
      }
    });
  }

  @NotNull
  private String makeDump(@NotNull final PlaceholderAPIPlugin plugin) {
    final StringBuilder builder = new StringBuilder();

    builder.append("Generated: ")
        .append(DATE_FORMAT.format(Instant.now()))
        .append("\n\n");

    builder.append("PlaceholderAPI: ")
        .append(plugin.getDescription().getVersion())
        .append("\n\n");

    builder.append("Expansions Registered:")
        .append('\n');

    final List<PlaceholderExpansion> expansions = plugin.getLocalExpansionManager()
        .getExpansions()
        .stream()
        .sorted(
            Comparator.comparing(PlaceholderExpansion::getIdentifier)
                      .thenComparing(PlaceholderExpansion::getAuthor)
        )
        .collect(Collectors.toList());

    int size = expansions.stream().map(e -> e.getIdentifier().length())
        .max(Integer::compareTo)
        .orElse(0);

    for (final PlaceholderExpansion expansion : expansions) {
      builder.append("  ")
          .append(String.format("%-" + size + "s", expansion.getIdentifier()))
          .append(" [Author: ")
          .append(expansion.getAuthor())
          .append(", Version: ")
          .append(expansion.getVersion())
          .append("]\n");

    }

    builder.append('\n');

    builder.append("Expansions Directory:")
        .append('\n');

    final String[] jars = plugin.getLocalExpansionManager()
        .getExpansionsFolder()
        .list((dir, name) -> name.toLowerCase(Locale.ROOT).endsWith(".jar"));


    if (jars == null) {
      builder.append("  ¨[Warning]: Could not load jar files from expansions folder.");
    } else {
      for (final String jar : jars) {
        builder.append("  ")
            .append(jar)
            .append('\n');
      }
    }

    builder.append('\n');

    builder.append("Server Info: ")
        .append(plugin.getServer().getBukkitVersion())
        .append('/')
        .append(plugin.getServer().getVersion())
        .append("\n");

    builder.append("Java Version: ")
        .append(System.getProperty("java.version"))
        .append("\n\n");

    builder.append("Plugin Info:")
        .append('\n');

    List<Plugin> plugins = Arrays.stream(plugin.getServer().getPluginManager().getPlugins())
        .sorted(Comparator.comparing(Plugin::getName))
        .collect(Collectors.toList());
    
    size = plugins.stream().map(pl -> pl.getName().length())
        .max(Integer::compareTo)
        .orElse(0);

    for (final Plugin other : plugins) {
      builder.append("  ")
          .append(String.format("%-" + size + "s", other.getName()))
          .append(" [Authors: [")
          .append(String.join(", ", other.getDescription().getAuthors()))
          .append("], Version: ")
          .append(other.getDescription().getVersion())
          .append("]")
          .append("\n");
    }

    return builder.toString();
  }
}
