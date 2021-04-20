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

import com.google.common.io.CharStreams;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionException;
import java.util.logging.Level;
import java.util.stream.Collectors;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.libs.JSONMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
  private static final String URL = "https://paste.helpch.at/";
  @NotNull
  private static final String NO_URL = "No URL available";

  @NotNull
  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter
      .ofLocalizedDateTime(FormatStyle.LONG)
      .withLocale(Locale.US)
      .withZone(ZoneId.of("UTC"));
  
  public static void createDump(@NotNull final PlaceholderAPIPlugin plugin,
      @NotNull final CommandSender sender) {
    post(generateDump(plugin, sender.getName())).whenComplete((url, ex) -> {
      if (ex != null) {
        plugin.getLogger().log(Level.WARNING, "Failed to create Dump!", ex);
        
        Msg.msg(sender,
            "&cFailed to create Dump! Check console for details.");
        return;
      }
      
      Msg.msg(sender,
          "&aSuccessfully created Dump: &f" + url);
    });
  }
  
  public static void createReport(@NotNull final PlaceholderAPIPlugin plugin,
      @NotNull final CommandSender sender) {
    Map<String, String> keys = new HashMap<>();
    CompletableFuture<String> dump = post(generateDump(plugin, sender.getName()))
        .whenComplete((key, ex) -> {
          if (ex != null) {
            keys.put("dump", null);
            return;
          }
          
          keys.put("dump", key);
        });
    CompletableFuture<String> log = post(generateLogDump(plugin, sender.getName()))
        .whenComplete((key, ex) -> {
          if (ex != null) {
            keys.put("log", null);
            return;
          }
          
          keys.put("log", key);
        });
    
    CompletableFuture.allOf(dump, log).whenComplete((v, ex) -> {
      if (ex != null) {
        plugin.getLogger().log(Level.WARNING, "Couldn't create Report!", ex);
        
        Msg.msg(sender,
            "&cFailed to create Report URL, Check console for details.");
        return;
      }
      
      String dumpUrl = keys.get("dump");
      String logUrl = keys.get("log");
      
      String url = getGitHubURL(dumpUrl, logUrl);
      
      if (url == null) {
        if (dumpUrl == null) {
          dumpUrl = "&c&o" + NO_URL;
        }
        if (logUrl == null) {
          logUrl = "&c&o" + NO_URL;
        }
        
        Msg.msg(sender,
            "&cFailed to create GitHub URL. Please create an issue manually.",
            "&cAvailable Pastes:",
            "&7Dump: " + dumpUrl,
            "&7Logs: " + logUrl);
        return;
      }
      
      if (sender instanceof Player) {
        JSONMessage msg = JSONMessage
            .create("Successfully created GitHub URL! Click ")
            .color(ChatColor.GREEN)
            .then("HERE")
            .color(ChatColor.WHITE)
            .style(ChatColor.UNDERLINE)
            .openURL(url)
            .tooltip(
                JSONMessage.create("Click to open a new Issue on GitHub.")
                    .color(ChatColor.GRAY)
            )
            .then(" to open a new Issue on GitHub!")
            .color(ChatColor.GREEN);
        
        msg.send((Player) sender);
        return;
      }
      
      // We can't use Msg.msg here as it would turn query parameters into color codes.
      sender.sendMessage("§aSuccessfully created GitHub URL! URL: §f" + url);
    });
  }
  
  private static CompletableFuture<String> post(@NotNull final String data) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        final HttpURLConnection connection = ((HttpURLConnection) new URL(URL + "documents")
            .openConnection());
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
        connection.setDoOutput(true);
        
        connection.connect();
        
        try (final OutputStream stream = connection.getOutputStream()) {
          stream.write(data.getBytes(StandardCharsets.UTF_8));
        }
        
        try (final InputStream stream = connection.getInputStream()) {
          // noinspection UnstableApiUsage
          final String json = CharStreams
              .toString(new InputStreamReader(stream, StandardCharsets.UTF_8));
          
          return URL + JsonParser.parseString(json).getAsJsonObject().get("key").getAsString();
        }
      } catch (final IOException ex) {
        throw new CompletionException(ex);
      }
    });
  }

  private static String generateDump(@NotNull PlaceholderAPIPlugin plugin,
      @NotNull String name) {
    final StringBuilder builder = getHeader(plugin, name);

    builder.append("Expansions Registered:");

    final List<PlaceholderExpansion> expansions = plugin.getLocalExpansionManager()
        .getExpansions()
        .stream()
        .sorted(Comparator.comparing(PlaceholderExpansion::getIdentifier))
        .sorted(Comparator.comparing(PlaceholderExpansion::getAuthor))
        .collect(Collectors.toList());

    int size = 0;

    for (final String identifier : expansions.stream().map(PlaceholderExpansion::getIdentifier)
        .collect(Collectors.toList())) {
      if (identifier.length() > size) {
        size = identifier.length();
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
        .list(((dir, fileName) -> fileName.toLowerCase().endsWith(".jar")));

    if (jars != null && jars.length > 0) { // Check that there are actual jars to add
      for (final String jar : jars) {
        builder.append("\n  ")
            .append(jar);
      }
    } else {
      builder.append("\n  No jars available");
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

    for (final String pluginName : plugins.stream().map(Plugin::getName)
        .collect(Collectors.toList())) {
      if (pluginName.length() > size) {
        size = pluginName.length();
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
  
  private static String generateLogDump(@NotNull final PlaceholderAPIPlugin plugin,
      @NotNull String name) {
    final StringBuilder builder = getHeader(plugin, name);
    
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

  private static StringBuilder getHeader(@NotNull final PlaceholderAPIPlugin plugin,
      @NotNull String name) {
    final StringBuilder builder = new StringBuilder();

    builder.append("Generated: ")
        .append(DATE_FORMAT.format(Instant.now()))
        .append(" by ")
        .append(name)
        .append("\n\n");

    builder.append("PlaceholderAPI: ")
        .append(plugin.getDescription().getVersion())
        .append("\n\n");

    return builder;
  }
  
  private static String getGitHubURL(String dumpUrl, String logUrl) {
    if (dumpUrl == null) {
      dumpUrl = NO_URL;
    }
    if (logUrl == null) {
      logUrl = NO_URL;
    }
    
    try {
      return "https://github.com/PlaceholderAPI/PlaceholderAPI/issues/new" 
          + "?labels=Type%3A+Issue+%28Unconfirmed%29" 
          + "&template=bug_report.yml" 
          + "&dump=" + URLEncoder.encode(dumpUrl, "UTF-8")
          + "&console=" + URLEncoder.encode(logUrl, "UTF-8");
    } catch (final UnsupportedEncodingException ex) {
      return null;
    }
  }
}
