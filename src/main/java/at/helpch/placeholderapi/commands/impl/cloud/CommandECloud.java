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
import at.helpch.placeholderapi.commands.PlaceholderCommand;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.awt.*;
import java.util.*;
import java.util.List;

public final class CommandECloud extends PlaceholderCommand {

    @Unmodifiable
    private static final List<PlaceholderCommand> COMMANDS = List
            .of(new CommandECloudClear(),
                    new CommandECloudStatus(),
                    new CommandECloudUpdate(),
                    new CommandECloudRefresh(),
                    new CommandECloudDownload(),
                    new CommandECloudExpansionInfo(),
                    new CommandECloudExpansionList(),
                    new CommandECloudExpansionPlaceholders());

    static {
        COMMANDS
                .forEach(command -> command.setPermission("placeholderapi.ecloud." + command.getLabel()));
    }

    @NotNull
    @Unmodifiable
    private final Map<String, PlaceholderCommand> commands;


    public CommandECloud() {
        super("ecloud");

        final Map<String, PlaceholderCommand> commands = new HashMap<>();

        for (final PlaceholderCommand command : COMMANDS) {
            command.getLabels().forEach(label -> commands.put(label, command));
        }

        this.commands = commands;
    }


    @Override
    public void evaluate(@NotNull final PlaceholderAPIPlugin plugin,
                         @NotNull final CommandSender sender, @NotNull final String alias,
                         @NotNull @Unmodifiable final List<String> params) {
        if (params.isEmpty()) {
            Message message = Message.empty()
                    .insert(Message.raw("PlaceholderAPI ").color(Color.CYAN).bold(true))
                    .insert(Message.raw("- ").color(Color.DARK_GRAY))
                    .insert(Message.raw("eCloud Help Menu ").color(Color.GRAY))
                    .insert(Message.raw("-\n").color(Color.DARK_GRAY));

            final List<String[]> commands = List.of(
                    new String[]{"ecloud status", "View status of the eCloud"},
                    new String[]{"ecloud list <all/{author}/installed> {page}", "List all/author specific available expansions"},
                    new String[]{"ecloud info <expansion name> {version}", "View information about a specific expansion available on the eCloud"},
                    new String[]{"ecloud placeholders <expansion name>", "View placeholders for an expansion"},
                    new String[]{"ecloud download <expansion name> {version}", "Download an expansion from the eCloud"},
                    new String[]{"ecloud update <expansion name/all>", "Update a specific/all installed expansions"},
                    new String[]{"ecloud refresh", "Fetch the most up to date list of expansions available."},
                    new String[]{"ecloud clear", "Clear the expansion cloud cache."}
            );

            for (String[] command : commands) {
                message = message.insert(Message.raw("\n/papi ").color(Color.CYAN))
                        .insert(Message.raw(command[0]).color(Color.WHITE))
                        .insert(Message.raw("\n  " + command[1]).color(Color.GRAY));
            }

            sender.sendMessage(message);

//            Msg.msg(sender,
//                    "&b&lPlaceholderAPI &8- &7eCloud Help Menu &8- ",
//                    " ",
//                    "&b/papi &fecloud status",
//                    "  &7&oView status of the eCloud",
//                    "&b/papi &fecloud list <all/{author}/installed> {page}",
//                    "  &7&oList all/author specific available expansions",
//                    "&b/papi &fecloud info <expansion name> {version}",
//                    "  &7&oView information about a specific expansion available on the eCloud",
//                    "&b/papi &fecloud placeholders <expansion name>",
//                    "  &7&oView placeholders for an expansion",
//                    "&b/papi &fecloud download <expansion name> {version}",
//                    "  &7&oDownload an expansion from the eCloud",
//                    "&b/papi &fecloud update <expansion name/all>",
//                    "  &7&oUpdate a specific/all installed expansions",
//                    "&b/papi &fecloud refresh",
//                    "  &7&oFetch the most up to date list of expansions available.",
//                    "&b/papi &fecloud clear",
//                    "  &7&oClear the expansion cloud cache.");

            return;
        }

        final String search = params.get(0).toLowerCase(Locale.ROOT);
        final PlaceholderCommand target = commands.get(search);

        if (target == null) {
            sender.sendMessage(Message.raw("Unknown command ").color(Color.RED).insert(Message.raw("ecloud " + search).color(Color.GRAY)));
//            Msg.msg(sender, "&cUnknown command &7ecloud " + search);
            return;
        }

        final String permission = target.getPermission();
        if (permission != null && !permission.isEmpty() && !sender.hasPermission(permission)) {
            sender.sendMessage(Message.raw("You do not have permission to do this!").color(Color.RED));
//            Msg.msg(sender, "&cYou do not have permission to do this!");
            return;
        }

        if (!plugin.configManager().config().cloudEnabled()) {
            sender.sendMessage(Message.raw("The eCloud Manager is not enabled! To enable it, set 'cloud_enabled' to true and reload the plugin."));
//            Msg.msg(sender, "&cThe eCloud Manager is not enabled! To enable it, set 'cloud_enabled' to true and reload the plugin.");
            return;
        }

        if (!target.getLabel().equalsIgnoreCase("refresh") && plugin.cloudExpansionManager().isEmpty()) {
            sender.sendMessage(Message.raw("There is no available data from the eCloud. Please try running ").color(Color.RED)
                    .insert(Message.raw("/papi ecloud refresh").color(Color.WHITE))
                    .insert(Message.raw(" If this does not resolve the issue, the eCloud may be blocked by your firewall, server host or service provider.\n\n").color(Color.RED))
                    .insert(Message.raw("More information: ").color(Color.RED))
                    .insert(Message.raw("https://placeholderapi.com/ecloud-blocked").color(Color.WHITE).bold(true).italic(true).link("https://placeholderapi.com/ecloud-blocked")));
//            Msg.msg(sender, "&cThere is no available data from the eCloud. Please try running &f/papi ecloud refresh&c. If this does not resolve the issue, the eCloud may be blocked by your firewall, server host, or service provider.\n\nMore information: &fhttps://placeholderapi.com/ecloud-blocked");
            return;
        }

        target.evaluate(plugin, sender, search, params.subList(1, params.size()));
    }

//    @Override
//    public void complete(@NotNull final PlaceholderAPIPlugin plugin,
//                         @NotNull final CommandSender sender, @NotNull final String alias,
//                         @NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions) {
//        if (params.size() <= 1) {
//            final Stream<String> targets = filterByPermission(sender, commands.values().stream())
//                    .map(PlaceholderCommand::getLabels).flatMap(Collection::stream);
//            suggestByParameter(targets, suggestions, params.isEmpty() ? null : params.get(0));
//
//            return; // send sub commands
//        }
//
//        final String search = params.get(0).toLowerCase(Locale.ROOT);
//        final PlaceholderCommand target = commands.get(search);
//
//        if (target == null) {
//            return;
//        }
//
//        target.complete(plugin, sender, search, params.subList(1, params.size()), suggestions);
//    }

}
