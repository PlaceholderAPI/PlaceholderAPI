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

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import java.text.SimpleDateFormat;
import java.util.*;
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
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;

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
    switch (target.toLowerCase(Locale.ROOT)) {
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
    switch (target.toLowerCase(Locale.ROOT)) {
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
        .append("&r");
  }

  private static Component getMessage(@NotNull final List<CloudExpansion> expansions,
      final int page, final int limit, @NotNull final String target) {
    final SimpleDateFormat format = PlaceholderAPIPlugin.getDateFormat();

    final TextComponent.Builder message = text();

    for (int index = 0; index < expansions.size(); index++) {
      final CloudExpansion expansion = expansions.get(index);
      final TextComponent.Builder line = text();

      final int expansionNumber = index + ((page - 1) * PAGE_SIZE) + 1;
      line.append(text(expansionNumber + ". ", DARK_GRAY));

      final NamedTextColor expansionColour;

      if (expansion.shouldUpdate()) {
        expansionColour = GOLD;
      } else {
        if (expansion.hasExpansion()) {
          expansionColour = GREEN;
        } else {
          expansionColour = GRAY;
        }
      }

      line.append(text(expansion.getName(), expansionColour));

      line.clickEvent(ClickEvent.suggestCommand("/papi ecloud download " + expansion.getName()));

      final TextComponent.Builder hoverText = text("Click to download this expansion!", AQUA)
              .append(newline()).append(newline())
              .append(text("Author: ", AQUA)).append(text(expansion.getAuthor(), WHITE))
              .append(newline())
              .append(text("Verified: ", AQUA)).append(text(expansion.isVerified() ? "✔" : "❌", expansion.isVerified() ? GREEN : RED, TextDecoration.BOLD))
              .append(newline())
              .append(text("Released: ", AQUA)).append(text(format.format(expansion.getLastUpdate()), WHITE))
              .toBuilder();

      Optional.ofNullable(expansion.getDescription())
              .filter(description -> !description.isEmpty())
              .ifPresent(description -> hoverText.append(newline()).append(newline())
                      .append(text(description.replace("\r", "").trim(), WHITE))
              );

      line.hoverEvent(HoverEvent.showText(hoverText.build()));

      if (index != expansions.size() - 1) {
        line.append(newline());
      }

      message.append(line.build());
    }

    if (limit > 1) {
      message.append(newline());

      final TextComponent.Builder left = text("◀", page > 1 ? GRAY : DARK_GRAY).toBuilder();

      if (page > 1) {
        left.clickEvent(ClickEvent.runCommand("/papi ecloud list " + target + " " + (page - 1)));
      }

      final TextComponent.Builder right = text("▶", page < limit ? GRAY : DARK_GRAY).toBuilder();

      if (page < limit) {
        right.clickEvent(ClickEvent.runCommand("/papi ecloud list " + target + " " + (page + 1)));
      }

      message.append(left, text(" " + page + " ", GREEN), right);
    }

    return message.build();
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
    plugin.getAdventure().player((Player) sender).sendMessage(message);
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
