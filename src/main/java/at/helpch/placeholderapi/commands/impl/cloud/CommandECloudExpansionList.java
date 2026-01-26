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
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;

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
        private static final Function<CloudExpansion, String> EXPANSION_LATEST_VERSION =
            CloudExpansion::getLatestVersion;
        @NotNull
        private static final Function<CloudExpansion, String> EXPANSION_CURRENT_VERSION =
            expansion -> PlaceholderAPIPlugin.instance().localExpansionManager()
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

    public static Message buildExpansionTitle(@NotNull final String target, final int page) {
        Message title;
        switch (target.toLowerCase(Locale.ROOT)) {
            case "all":
                title = Message.raw("All Expansions").color(Color.CYAN);
                break;
            case "installed":
                title = Message.raw("Installed Expansions").color(Color.CYAN);
                break;
            default:
                title = Message.raw("Expansions by ").color(Color.CYAN)
                        .insert(Message.raw(target).color(Color.WHITE));
                break;
        }

        if (page == -1) {
            return title.insert(Message.raw("\n"));
        }

        return title
                .insert(Message.raw(" Page").color(Color.CYAN))
                .insert(Message.raw(": ").color(Color.GRAY))
                .insert(Message.raw(String.valueOf(page)).color(Color.GREEN));
    }

    private static Message getMessage(@NotNull final List<CloudExpansion> expansions,
                                        final int page, final int limit, @NotNull final String target) {
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

    private static Message buildExpansionTable(@NotNull final List<CloudExpansion> expansions,
                                               final int startIndex,
                                               @NotNull final String versionTitle,
                                               @NotNull final Function<CloudExpansion, String> versionFunction) {
        final List<List<String>> rows = new ArrayList<>();
        rows.add(List.of("#", "Name", "Author", "Verified", versionTitle));

        int counter = startIndex;
        for (final CloudExpansion expansion : expansions) {
            rows.add(List.of(
                    counter++ + ".",
                    expansion.getName(),
                    expansion.getAuthor(),
                    expansion.getVersion().isVerified() ? "Y" : "N",
                    versionFunction.apply(expansion)
            ));
        }

        final int columnCount = rows.getFirst().size();
        final int[] widths = new int[columnCount];
        for (final List<String> row : rows) {
            for (int i = 0; i < columnCount; i++) {
                widths[i] = Math.max(widths[i], row.get(i).length());
            }
        }

        final List<Color> headerColors = List.of(
                Color.WHITE,
                new Color(85, 85, 255),
                new Color(85, 85, 255),
                new Color(85, 85, 255),
                new Color(85, 85, 255)
        );

        Message message = Message.empty();
        message = message.insert(buildTableRow(rows.getFirst(), headerColors, widths));
        message = message.insert(Message.raw("\n"));

        final int separatorLength = Arrays.stream(widths).sum() + (columnCount * 2);
        message = message.insert(Message.raw("-".repeat(separatorLength)).color(Color.DARK_GRAY));

        if (rows.size() > 1) {
            message = message.insert(Message.raw("\n"));
        }

        for (int rowIndex = 1; rowIndex < rows.size(); rowIndex++) {
            final List<String> row = rows.get(rowIndex);
            final CloudExpansion expansion = expansions.get(rowIndex - 1);

            final Color nameColor = expansion.shouldUpdate()
                    ? Color.YELLOW
                    : (expansion.hasExpansion() ? Color.GREEN : Color.GRAY);

            final List<Color> rowColors = List.of(
                    Color.DARK_GRAY,
                    nameColor,
                    Color.WHITE,
                    expansion.getVersion().isVerified() ? Color.GREEN : Color.RED,
                    Color.WHITE
            );

            message = message.insert(buildTableRow(row, rowColors, widths));
            if (rowIndex < rows.size() - 1) {
                message = message.insert(Message.raw("\n"));
            }
        }

        return message;
    }

    private static Message buildTableRow(@NotNull final List<String> columns,
                                         @NotNull final List<Color> colors,
                                         @NotNull final int[] widths) {
        Message row = Message.empty();

        for (int i = 0; i < columns.size(); i++) {
            final String padded = padRight(columns.get(i), widths[i] + 2);
            row = row.insert(Message.raw(padded).color(colors.get(i)));
        }

        return row;
    }

    @NotNull
    private static String padRight(@NotNull final String text, final int length) {
        if (text.length() >= length) {
            return text;
        }
        return text + " ".repeat(length - text.length());
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
                final Message message = buildExpansionTitle(params.get(0), -1)
                    .insert(buildExpansionTable(
                        expansions,
                        1,
                        installed ? "Version" : "Latest Version",
                        installed ? EXPANSION_CURRENT_VERSION : EXPANSION_LATEST_VERSION));

                sender.sendMessage(message);
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

        final List<CloudExpansion> values = getPage(expansions, page - 1);

        final Message title = buildExpansionTitle(params.get(0), page);

        if (!(sender instanceof Player)) {
                final Message message = title.insert(buildExpansionTable(
                    values,
                    ((page - 1) * PAGE_SIZE) + 1,
                    installed ? "Version" : "Latest Version",
                    installed ? EXPANSION_CURRENT_VERSION : EXPANSION_LATEST_VERSION));

                sender.sendMessage(message);
//            Msg.msg(sender, builder.toString());

            return;
        }

            sender.sendMessage(title);
//        Msg.msg(sender, builder.toString());

        final int limit = (int) Math.ceil((double) expansions.size() / PAGE_SIZE);


    final Message message = getMessage(values, page, limit, params.get(0));
    sender.sendMessage(message);
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
