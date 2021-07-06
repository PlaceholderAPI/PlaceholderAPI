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

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.configuration.ExpansionSort;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.util.Format;
import me.clip.placeholderapi.util.Msg;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class CommandECloudExpansionList extends PlaceholderCommand {

  private static final int PAGE_SIZE = 10;

  @NotNull
  private static final Function<CloudExpansion, Object> EXPANSION_NAME =
      expansion -> (expansion.shouldUpdate() ? "&6" : expansion.hasExpansion() ? "&a" : "&7")
          + expansion.getName();
  @NotNull
  private static final Function<CloudExpansion, Object> EXPANSION_AUTHOR =
      expansion -> "&f" + expansion.getAuthor();
  @NotNull
  private static final Function<CloudExpansion, Object> EXPANSION_VERIFIED =
      expansion -> expansion.isVerified() ? "&aY" : "&cN";
  @NotNull
  private static final Function<CloudExpansion, Object> EXPANSION_LATEST_VERSION =
      expansion -> "&f" + expansion.getLatestVersion();
  @NotNull
  private static final Function<CloudExpansion, Object> EXPANSION_CURRENT_VERSION =
      expansion -> "&f" + PlaceholderAPIPlugin.getInstance().getLocalExpansionManager()
          .findExpansionByName(expansion.getName()).map(PlaceholderExpansion::getVersion)
          .orElse("Unknown");


  @Unmodifiable
  private static final Set<String> OPTIONS = ImmutableSet.of("all", "installed");


  public CommandECloudExpansionList() {
    super("list");
  }

  @NotNull
  private static Collection<CloudExpansion> getExpansions(@NotNull final String target,
      @NotNull final PlaceholderAPIPlugin plugin) {
    switch (target.toLowerCase()) {
      case "all":
        return plugin.getCloudExpansionManager().getCloudExpansions().values();
      case "installed":
        return plugin.getCloudExpansionManager().getCloudExpansionsInstalled().values();
      default:
        return plugin.getCloudExpansionManager().getCloudExpansionsByAuthor(target).values();
    }
  }

  @NotNull
  private static List<CloudExpansion> getPage(@NotNull final List<CloudExpansion> expansions,
      final int page) {
    final int head = (page * PAGE_SIZE);
    final int tail = Math.min(expansions.size(), head + PAGE_SIZE);

    if (expansions.size() < head) {
      return Collections.emptyList();
    }

    return expansions.subList(head, tail);
  }

  public static void addExpansionTitle(@NotNull final StringBuilder builder,
      @NotNull final String target, final int page) {
    switch (target.toLowerCase()) {
      case "all":
        builder.append("&bAll Expansions");
        break;
      case "installed":
        builder.append("&bInstalled Expansions");
        break;
      default:
        builder.append("&bExpansions by &f")
            .append(target);
        break;
    }

    if (page == -1) {
      builder.append('\n');
      return;
    }

    builder.append(" &bPage&7: &a")
        .append(page)
        .append("&r")
        .append('\n');
  }

  private static Component getMessage(@NotNull final List<CloudExpansion> expansions,
                                                           final int page, final int limit, @NotNull final String target) {
    final SimpleDateFormat format = PlaceholderAPIPlugin.getDateFormat();

    // Overall being send
    final StringBuilder sb = new StringBuilder();

    for (int index = 0; index < expansions.size(); index++) {
      final CloudExpansion expansion = expansions.get(index);

      // Hover desc per expansion
      final StringBuilder desc = new StringBuilder();

      desc.append("<blue>Click to download this expansion!")
          .append('\n')
          .append('\n')
          .append("<blue>Author: <white>")
          .append(expansion.getAuthor())
          .append('\n')
          .append("<blue>Verified: ")
          .append(expansion.isVerified() ? "<green><bold✔</bold></green>" : "<red><bold>❌</bold></red>")
          .append('\n')
          .append("<blue>Latest Version: <white>")
          .append(expansion.getLatestVersion())
          .append('\n')
          .append("<blue>Released: <white>")
          .append(format.format(expansion.getLastUpdate()));

      final String description = expansion.getDescription();
      if (description != null && !description.isEmpty()) {
        desc.append('\n')
            .append('\n')
            .append("<white>")
            .append(description.replace("\r", "").trim());
      }

      StringBuilder content = new StringBuilder();

      content.append("<dark_gray>")
              .append(index + ((page - 1) * PAGE_SIZE) + 1)
              .append(".</dark_gray> ")
              .append(expansion.shouldUpdate() ? "<gold>" : expansion.hasExpansion() ? "<green>" : "<gray>")
              .append(expansion.getName());

//      message.append(Component.text(Msg.color(
//          "&8" + (index + ((page - 1) * PAGE_SIZE) + 1) + ".&r " + (expansion.shouldUpdate() ? "&6"
//              : expansion.hasExpansion() ? "&a" : "&7") + expansion.getName()));

      sb.append("<click:suggest_command:/papi ecloud download ")
              .append(expansion.getName())
              .append("><hover:show_text:'")
              .append(desc)
              .append("'>")
              .append(content)
              .append("</hover>");

     // message.tooltip(Msg.color(tooltip.toString()));
     // message.suggestCommand("/papi ecloud download " + expansion.getName());

      if (index < expansions.size() - 1) {
        sb.append("\n");
      }

      //tooltip.setLength(0);
    }

    if (limit > 1) {
    //  message.newline();
      sb.append("\n");

    //  message.then("◀").color(page > 1 ? ChatColor.GRAY : ChatColor.DARK_GRAY);
      sb.append(page > 1 ? "<gray>" : "<dark_gray>").append("◀");
      if (page > 1) {
     //   message.runCommand("/papi ecloud list " + target + " " + (page - 1));
      }

     // message.then(" " + page + " ").color(ChatColor.GREEN);
      sb.append("<green> " + page + " </green>");

    //  message.then("▶").color(page < limit ? ChatColor.GRAY : ChatColor.DARK_GRAY);
      sb.append(page < limit ? "<gray>" : "<dark_gray>").append("▶");
      if (page < limit) {
    //    message.runCommand("/papi ecloud list " + target + " " + (page + 1));
      }
    }

    return MiniMessage.get().parse(sb.toString());
  }

  private static void addExpansionTable(@NotNull final List<CloudExpansion> expansions,
      @NotNull final StringBuilder message, final int startIndex,
      @NotNull final String versionTitle,
      @NotNull final Function<CloudExpansion, Object> versionFunction) {
    final Map<String, Function<CloudExpansion, Object>> functions = new LinkedHashMap<>();

    final AtomicInteger counter = new AtomicInteger(startIndex);
    functions.put("&f", expansion -> "&8" + counter.getAndIncrement() + ".");

    functions.put("&9Name", EXPANSION_NAME);
    functions.put("&9Author", EXPANSION_AUTHOR);
    functions.put("&9Verified", EXPANSION_VERIFIED);
    functions.put(versionTitle, versionFunction);

    final List<List<String>> rows = new ArrayList<>();

    rows.add(0, new ArrayList<>(functions.keySet()));

    for (final CloudExpansion expansion : expansions) {
      rows.add(functions.values().stream().map(function -> function.apply(expansion))
          .map(Objects::toString).collect(Collectors.toList()));
    }

    final List<String> table = Format.tablify(Format.Align.LEFT, rows)
        .orElse(Collections.emptyList());
    if (table.isEmpty()) {
      return;
    }

    table.add(1, "&8" + Strings.repeat("-", table.get(0).length() - (rows.get(0).size() * 2)));

    message.append(String.join("\n", table));
  }

  @Override
  public void evaluate(@NotNull final PlaceholderAPIPlugin plugin,
      @NotNull final CommandSender sender, @NotNull final String alias,
      @NotNull @Unmodifiable final List<String> params) {
    if (params.isEmpty()) {
      Msg.msg(sender,
          "&cYou must specify an option. [all, {author}, installed]");
      return;
    }

    final boolean installed = params.get(0).equalsIgnoreCase("installed");
    final List<CloudExpansion> expansions = Lists
        .newArrayList(getExpansions(params.get(0), plugin));

    if (expansions.isEmpty()) {
      Msg.msg(sender,
          "&cNo expansions available to list.");
      return;
    }

    expansions
        .sort(plugin.getPlaceholderAPIConfig().getExpansionSort().orElse(ExpansionSort.LATEST));

    if (!(sender instanceof Player) && params.size() < 2) {
      final StringBuilder builder = new StringBuilder();

      addExpansionTitle(builder, params.get(0), -1);
      addExpansionTable(expansions,
          builder,
          1,
          installed ? "&9Version" : "&9Latest Version",
          installed ? EXPANSION_CURRENT_VERSION : EXPANSION_LATEST_VERSION);

      Msg.msg(sender, builder.toString());
      return;
    }

    final int page;

    if (params.size() < 2) {
      page = 1;
    } else {
      //noinspection UnstableApiUsage
      final Integer parsed = Ints.tryParse(params.get(1));
      if (parsed == null) {
        Msg.msg(sender,
            "&cPage number must be an integer.");
        return;
      }

      final int limit = (int) Math.ceil((double) expansions.size() / PAGE_SIZE);

      if (parsed < 1 || parsed > limit) {
        Msg.msg(sender,
            "&cPage number must be in the range &8[&a1&7..&a" + limit + "&8]");
        return;
      }

      page = parsed;
    }

    final StringBuilder builder = new StringBuilder();
    final List<CloudExpansion> values = getPage(expansions, page - 1);

    addExpansionTitle(builder, params.get(0), page);

    if (!(sender instanceof Player)) {
      addExpansionTable(values,
          builder,
          ((page - 1) * PAGE_SIZE) + 1,
          installed ? "&9Version" : "&9Latest Version",
          installed ? EXPANSION_CURRENT_VERSION : EXPANSION_LATEST_VERSION);

      Msg.msg(sender, builder.toString());

      return;
    }

    Msg.msg(sender, builder.toString());

    final int limit = (int) Math.ceil((double) expansions.size() / PAGE_SIZE);

    final Component message = getMessage(values, page, limit, params.get(0));
    plugin.adventure().player((Player) sender).sendMessage(message);
    //message.send(((Player) sender));
  }

  @Override
  public void complete(@NotNull final PlaceholderAPIPlugin plugin,
      @NotNull final CommandSender sender, @NotNull final String alias,
      @NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions) {
    if (params.size() > 2) {
      return;
    }

    if (params.size() <= 1) {
      suggestByParameter(
          Sets.union(OPTIONS, plugin.getCloudExpansionManager().getCloudExpansionAuthors())
              .stream(), suggestions, params.isEmpty() ? null : params.get(0));
      return;
    }

    suggestByParameter(IntStream.rangeClosed(1,
        (int) Math.ceil((double) getExpansions(params.get(0), plugin).size() / PAGE_SIZE))
        .mapToObj(Objects::toString), suggestions, params.get(1));
  }

}
