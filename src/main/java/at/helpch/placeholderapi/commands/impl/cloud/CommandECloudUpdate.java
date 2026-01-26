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

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import at.helpch.placeholderapi.PlaceholderAPIPlugin;
import at.helpch.placeholderapi.commands.PlaceholderCommand;
import at.helpch.placeholderapi.expansion.PlaceholderExpansion;
import at.helpch.placeholderapi.expansion.cloud.CloudExpansion;
import at.helpch.placeholderapi.util.Futures;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

/**
 * please don't flame me for this code, I will fix this shit later.
 */
public final class CommandECloudUpdate extends PlaceholderCommand {

    public CommandECloudUpdate() {
        super("update");
    }

    private static CompletableFuture<List<@Nullable Class<? extends PlaceholderExpansion>>> downloadAndDiscover(
            @NotNull final List<CloudExpansion> expansions, @NotNull final PlaceholderAPIPlugin plugin) {
        return expansions.stream()
                .map(expansion -> plugin.cloudExpansionManager()
                        .downloadExpansion(expansion, expansion.getVersion()))
                .map(future -> future.thenCompose(plugin.localExpansionManager()::findExpansionInFile))
                .collect(Futures.collector());
    }

    @Override
    public void evaluate(@NotNull final PlaceholderAPIPlugin plugin,
                         @NotNull final CommandSender sender, @NotNull final String alias,
                         @NotNull @Unmodifiable final List<String> params) {
        if (params.isEmpty()) {
            sender.sendMessage(Message.raw("You must define 'all' or the name of an expansion to update.").color(Color.RED));
//            Msg.msg(sender,
//                    "&cYou must define 'all' or the name of an expansion to update.");
            return;
        }

        final boolean multiple = params.get(0).equalsIgnoreCase("all");
        final List<CloudExpansion> expansions = new ArrayList<>();

        // gather target expansions
        if (multiple) {
            expansions.addAll(plugin.cloudExpansionManager().getCloudExpansionsInstalled().values());
        } else {
            plugin.cloudExpansionManager().findCloudExpansionByName(params.get(0))
                    .ifPresent(expansions::add);
        }

        // remove the ones that are the latest version
        expansions.removeIf(expansion -> !expansion.shouldUpdate());

        if (expansions.isEmpty()) {
            sender.sendMessage(Message.raw("No updates available for " + (!multiple ? "this expansion." : "your active expansions.")).color(Color.RED));
//            Msg.msg(sender,
//                    "&cNo updates available for " + (!multiple ? "this expansion."
//                            : "your active expansions."));
            return;
        }

        Message expansionList = Message.raw("[").color(Color.DARK_GRAY);
        for (int i = 0; i < expansions.size(); i++) {
            if (i > 0) {
                expansionList = expansionList.insert(Message.raw(", ").color(Color.GRAY));
            }
            expansionList = expansionList.insert(Message.raw(expansions.get(i).getName()).color(Color.ORANGE));
        }
        expansionList = expansionList.insert(Message.raw("]").color(Color.DARK_GRAY));

        sender.sendMessage(Message.raw("Updating expansions: ").color(Color.GREEN)
                .insert(expansionList));
//        Msg.msg(sender,
//                "&aUpdating expansions: " + expansions.stream().map(CloudExpansion::getName)
//                        .collect(Collectors.joining("&7, &6", "&8[&6", "&8]&r")));

        Futures.onMainThread(plugin, downloadAndDiscover(expansions, plugin), (classes, exception) -> {
            if (exception != null) {
                sender.sendMessage(Message.raw("Failed to update expansions: ").color(Color.RED).insert(Message.raw(exception.getMessage()).color(Color.YELLOW)));
//                Msg.msg(sender,
//                        "&cFailed to update expansions: &e" + exception.getMessage());
                return;
            }

            sender.sendMessage(Message.raw("Successfully downloaded updates, registering new versions.").color(Color.GREEN));
//            Msg.msg(sender,
//                    "&aSuccessfully downloaded updates, registering new versions.");

                final List<PlaceholderExpansion> registered = classes.stream()
                    .filter(Objects::nonNull)
                    .map(plugin.localExpansionManager()::register)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();

                Message registeredMessage = Message.raw("Registered expansions:\n").color(Color.GRAY);
                for (int i = 0; i < registered.size(); i++) {
                final PlaceholderExpansion expansion = registered.get(i);
                registeredMessage = registeredMessage
                    .insert(Message.raw("  ").color(Color.GRAY))
                    .insert(Message.raw(expansion.getName()).color(Color.GREEN))
                    .insert(Message.raw(" ").color(Color.GRAY))
                    .insert(Message.raw(expansion.getVersion()).color(Color.WHITE));

                if (i < registered.size() - 1) {
                    registeredMessage = registeredMessage.insert(Message.raw("\n"));
                }
                }

                sender.sendMessage(registeredMessage);
//            Msg.msg(sender,
//                    "&7Registered expansions:", message);

        });
    }

//    @Override
//    public void complete(@NotNull final PlaceholderAPIPlugin plugin,
//                         @NotNull final CommandSender sender, @NotNull final String alias,
//                         @NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions) {
//        if (params.size() > 1) {
//            return;
//        }
//
//        final List<CloudExpansion> installed = Lists
//                .newArrayList(plugin.getCloudExpansionManager().getCloudExpansionsInstalled().values());
//        installed.removeIf(expansion -> !expansion.shouldUpdate());
//
//        if (!installed.isEmpty() && (params.isEmpty() || "all"
//                .startsWith(params.get(0).toLowerCase(Locale.ROOT)))) {
//            suggestions.add("all");
//        }
//
//        suggestByParameter(
//                installed.stream().map(CloudExpansion::getName).map(name -> name.replace(" ", "_")),
//                suggestions, params.isEmpty() ? null : params.get(0));
//    }

}
