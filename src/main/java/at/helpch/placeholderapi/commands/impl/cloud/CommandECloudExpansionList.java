/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2026 PlaceholderAPI Team
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

package at.helpch.placeholderapi.commands.impl.cloud;

import at.helpch.placeholderapi.PlaceholderAPIPlugin;
import at.helpch.placeholderapi.util.Format;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import at.helpch.placeholderapi.commands.PlaceholderCommand;
import at.helpch.placeholderapi.expansion.PlaceholderExpansion;
import at.helpch.placeholderapi.expansion.cloud.CloudExpansion;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.entity.entities.Player;
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
            expansion -> expansion.getVersion().isVerified() ? "&aY" : "&cN";
    @NotNull
    private static final Function<CloudExpansion, Object> EXPANSION_LATEST_VERSION =
            expansion -> "&f" + expansion.getLatestVersion();
    @NotNull
    private static final Function<CloudExpansion, Object> EXPANSION_CURRENT_VERSION =
            expansion -> "&f" + PlaceholderAPIPlugin.instance().localExpansionManager()
                    .findExpansionByName(expansion.getName()).map(PlaceholderExpansion::getVersion)
                    .orElse("Unknown");


    @Unmodifiable
    private static final Set<String> OPTIONS = Set.of("all", "installed");


    public CommandECloudExpansionList() {
        super("list");
    }

    @NotNull
    private static Collection<CloudExpansion> getExpansions(@NotNull final String target,
                                                            @NotNull final PlaceholderAPIPlugin plugin) {
        switch (target.toLowerCase(Locale.ROOT)) {
            case "all":
                return plugin.cloudExpansionManager().getCloudExpansions().values();
            case "installed":
                return plugin.cloudExpansionManager().getCloudExpansionsInstalled().values();
            default:
                return plugin.cloudExpansionManager().getCloudExpansionsByAuthor(target).values();
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

    private static Message getMessage(@NotNull final List<CloudExpansion> expansions,
                                        final int page, final int limit, @NotNull final String target) {
        final SimpleDateFormat format = new SimpleDateFormat(PlaceholderAPIPlugin.instance().configManager().config().dateFormat());

        Message message = Message.empty();

        for (int index = 0; index < expansions.size(); index++) {
            final CloudExpansion expansion = expansions.get(index);
            Message line = Message.empty();

            final int expansionNumber = index + ((page - 1) * PAGE_SIZE) + 1;
            line = line.insert(Message.raw(expansionNumber + ". ").color(Color.DARK_GRAY));

            final Color expansionColour;

            if (expansion.shouldUpdate()) {
                expansionColour = Color.YELLOW;
            } else {
                if (expansion.hasExpansion()) {
                    expansionColour = Color.GREEN;
                } else {
                    expansionColour = Color.GRAY;
                }
            }

            line = line.insert(Message.raw(expansion.getName()).color(expansionColour));

//            line = line.click(ClickEvent.suggestCommand("/papi ecloud download " + expansion.getName()));
//
//            final TextComponent.Builder hoverText = text("Click to download this expansion!", AQUA)
//                    .append(newline()).append(newline())
//                    .append(text("Author: ", AQUA)).append(text(expansion.getAuthor(), WHITE))
//                    .append(newline())
//                    .append(text("Verified: ", AQUA)).append(text(expansion.getVersion().isVerified() ? "✔" : "❌", expansion.getVersion().isVerified() ? GREEN : RED, TextDecoration.BOLD))
//                    .append(newline())
//                    .append(text("Released: ", AQUA)).append(text(format.format(expansion.getLastUpdate()), WHITE))
//                    .toBuilder();
//
//            Optional.ofNullable(expansion.getDescription())
//                    .filter(description -> !description.isEmpty())
//                    .ifPresent(description -> hoverText.append(newline()).append(newline())
//                            .append(text(description.replace("\r", "").trim(), WHITE))
//                    );

//            line.hoverEvent(HoverEvent.showText(hoverText.build()));

            if (index != expansions.size() - 1) {
                line.insert(Message.raw("\n"));
            }

            message = message.insert(line);
        }

        if (limit > 1) {
            message = message.insert("\n");

//            Message left = Message.raw("◀", page > 1 ? GRAY : DARK_GRAY).toBuilder();

//            if (page > 1) {
//                left.clickEvent(ClickEvent.runCommand("/papi ecloud list " + target + " " + (page - 1)));
//            }
//
//            final TextComponent.Builder right = text("▶", page < limit ? GRAY : DARK_GRAY).toBuilder();

//            if (page < limit) {
//                right.clickEvent(ClickEvent.runCommand("/papi ecloud list " + target + " " + (page + 1)));
//            }

            message = message.insert(Message.raw(" - " + page + " - ").color(Color.GREEN));
        }

        return message;
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



//        table.add(1, "&8" + Strings.repeat("-", table.get(0).length() - (rows.get(0).size() * 2)));
        table.add(1, "&8" + "-".repeat(table.get(0).length() - (rows.getFirst().size() * 2)));

        message.append(String.join("\n", table));
    }

    @Override
    public void evaluate(@NotNull final PlaceholderAPIPlugin plugin,
                         @NotNull final CommandSender sender, @NotNull final String alias,
                         @NotNull @Unmodifiable final List<String> params) {
        if (params.isEmpty()) {
            sender.sendMessage(Message.raw("You must specify an option. [all, {author}, installed]").color(Color.RED));
//            Msg.msg(sender,
//                    "&cYou must specify an option. [all, {author}, installed]");
            return;
        }

        final boolean installed = params.get(0).equalsIgnoreCase("installed");
        final List<CloudExpansion> expansions = new ArrayList<>(getExpansions(params.get(0), plugin));

        if (expansions.isEmpty()) {
            sender.sendMessage(Message.raw("No expansions available to list.").color(Color.RED));
//            Msg.msg(sender,
//                    "&cNo expansions available to list.");
            return;
        }

        expansions
                .sort(plugin.configManager().config().cloudSorting());

        if (!(sender instanceof Player) && params.size() < 2) {
            final StringBuilder builder = new StringBuilder();

            addExpansionTitle(builder, params.get(0), -1);
            addExpansionTable(expansions,
                    builder,
                    1,
                    installed ? "&9Version" : "&9Latest Version",
                    installed ? EXPANSION_CURRENT_VERSION : EXPANSION_LATEST_VERSION);

            sender.sendMessage(Message.raw(builder.toString()));
//            Msg.msg(sender, builder.toString());
            return;
        }

        final int page;

        if (params.size() < 2) {
            page = 1;
        } else {
            //noinspection UnstableApiUsage
            Integer parsed/* = Ints.tryParse(params.get(1))*/;

            try {
                parsed = Integer.parseInt(params.get(1));
            } catch (Exception e) {
                parsed = null;
            }

            if (parsed == null) {
                sender.sendMessage(Message.raw("Page number must be an integer.").color(Color.RED));
//                Msg.msg(sender,
//                        "&cPage number must be an integer.");
                return;
            }

            final int limit = (int) Math.ceil((double) expansions.size() / PAGE_SIZE);

            if (parsed < 1 || parsed > limit) {
                sender.sendMessage(Message.raw("Page number must be in the range [1.." + limit + "]").color(Color.RED)); //todo: not exact
//                Msg.msg(sender,
//                        "&cPage number must be in the range &8[&a1&7..&a" + limit + "&8]");
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

            sender.sendMessage(Message.raw(builder.toString()));
//            Msg.msg(sender, builder.toString());

            return;
        }

        sender.sendMessage(Message.raw(builder.toString()));
//        Msg.msg(sender, builder.toString());

        final int limit = (int) Math.ceil((double) expansions.size() / PAGE_SIZE);


//        final Component message = getMessage(values, page, limit, params.get(0));
//        plugin.getAdventure().player((Player) sender).sendMessage(message);
    }

//    @Override
//    public void complete(@NotNull final PlaceholderAPIPlugin plugin,
//                         @NotNull final CommandSender sender, @NotNull final String alias,
//                         @NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions) {
//        if (params.size() > 2) {
//            return;
//        }
//
//        if (params.size() <= 1) {
//            suggestByParameter(
//                    Sets.union(OPTIONS, plugin.getCloudExpansionManager().getCloudExpansionAuthors())
//                            .stream(), suggestions, params.isEmpty() ? null : params.get(0));
//            return;
//        }
//
//        suggestByParameter(IntStream.rangeClosed(1,
//                        (int) Math.ceil((double) getExpansions(params.get(0), plugin).size() / PAGE_SIZE))
//                .mapToObj(Objects::toString), suggestions, params.get(1));
//    }

}
