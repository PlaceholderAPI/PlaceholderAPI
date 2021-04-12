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

package me.clip.placeholderapi.util;

import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class PasteUtil {

  @NotNull
  private static final OkHttpClient CLIENT = new OkHttpClient();

  @NotNull
  private static final String URL = "https://paste.helpch.at/";

  @NotNull
  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter
      .ofLocalizedDateTime(FormatStyle.LONG)
      .withLocale(Locale.US)
      .withZone(ZoneId.of("UTC"));

  public static CompletableFuture<String> postDump(@NotNull final PlaceholderAPIPlugin plugin) {
    final RequestBody requestBody = RequestBody.create(generateDump(plugin), null);
    
    return post(requestBody);
  }
  
  public static CompletableFuture<String> postLogs(@NotNull final PlaceholderAPIPlugin plugin) {
    final RequestBody requestBody = RequestBody.create(generateLogDump(plugin), null);
    
    return post(requestBody);
  }
  
  private static CompletableFuture<String> post(@NotNull final RequestBody requestBody) {
    return CompletableFuture.supplyAsync(() -> {
      final Request request = new Request.Builder()
          .url(URL + "documents")
          .post(requestBody)
          .addHeader("Content-Type", "text/plain; charset=utf-8")
          .build();

      try (Response response = CLIENT.newCall(request).execute()){
        if (!response.isSuccessful()) {
          throw new IOException(URL + " unavailable");
        }

        ResponseBody body = response.body();
        
        if (body == null) {
          throw new IOException("Received Body was null");
        }
        
        String json = body.string();
        
        if(json.isEmpty()) {
          throw new IOException("Received Body was empty");
        }
        
        return URL + JsonParser.parseString(json).getAsJsonObject().get("key").getAsString();
      } catch (IOException ex) {
        throw new CompletionException(ex);
      }
    });
  }

  private static String generateDump(PlaceholderAPIPlugin plugin) {
    final StringBuilder builder = getHeader(plugin);

    builder.append("Expansions Registered:");

    final List<PlaceholderExpansion> expansions = plugin.getLocalExpansionManager()
        .getExpansions()
        .stream()
        .sorted(Comparator.comparing(PlaceholderExpansion::getIdentifier))
        .sorted(Comparator.comparing(PlaceholderExpansion::getAuthor))
        .collect(Collectors.toList());

    int size = 0;

    for (final String name : expansions.stream().map(PlaceholderExpansion::getIdentifier)
        .collect(Collectors.toList())) {
      if (name.length() > size) {
        size = name.length();
      }
    }

    for (final PlaceholderExpansion expansion : expansions) {
      builder.append("\n  ")
          .append(String.format("%-" + size + "s", expansion.getIdentifier()))
          .append(" [Author: ")
          .append(expansion.getAuthor())
          .append(", Version: ")
          .append(expansion.getVersion())
          .append("]");
    }

    builder.append("\n\n");

    builder.append("Expansions Directory:");

    final String[] jars = plugin.getLocalExpansionManager()
        .getExpansionsFolder()
        .list(((dir, name) -> name.toLowerCase().endsWith(".jar")));

    for (final String jar : jars) {
      builder.append("\n  ")
          .append(jar);
    }

    builder.append("\n\n");

    builder.append("Server Info: ")
        .append(plugin.getServer().getBukkitVersion())
        .append("/")
        .append(plugin.getServer().getVersion())
        .append("\n\n");

    builder.append("Plugin Info:");

    List<Plugin> plugins = Arrays.stream(plugin.getServer().getPluginManager().getPlugins())
        .sorted(Comparator.comparing(Plugin::getName))
        .collect(Collectors.toList());

    size = 0;

    for (final String name : plugins.stream().map(Plugin::getName)
        .collect(Collectors.toList())) {
      if (name.length() > size) {
        size = name.length();
      }
    }

    for (final Plugin other : plugins) {
      builder.append("\n  ")
          .append(String.format("%-" + size + "s", other.getName()))
          .append(" [Version: ")
          .append(other.getDescription().getVersion())
          .append("]");
    }

    return builder.toString();
  }
  
  private static String generateLogDump(@NotNull final PlaceholderAPIPlugin plugin) {
    final StringBuilder builder = getHeader(plugin);
    
    builder.append("========== START OF LOG ==========")
        .append("\n");
    
    try {
      FileReader reader = new FileReader("logs/latest.log");
      BufferedReader br = new BufferedReader(reader);
      boolean done = false;
      while (!done) {
        String line = br.readLine();
        if (line == null) {
          done = true;
        }
        
        if (line != null) {
          builder.append("\n")
              .append(line);
        }
      }
    } catch (IOException ex) {
      return ex.getMessage();
    }
    
    return builder.toString();
  }

  private static StringBuilder getHeader(@NotNull final PlaceholderAPIPlugin plugin) {
    final StringBuilder builder = new StringBuilder();

    builder.append("Generated: ")
        .append(DATE_FORMAT.format(Instant.now()))
        .append("\n\n");

    builder.append("PlaceholderAPI: ")
        .append(plugin.getDescription().getVersion())
        .append("\n\n");

    return builder;
  }
}
