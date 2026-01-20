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
import java.util.Arrays;
import java.util.List;

import at.helpch.placeholderapi.PlaceholderAPIPlugin;
import at.helpch.placeholderapi.commands.PlaceholderCommand;
import at.helpch.placeholderapi.expansion.cloud.CloudExpansion;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class CommandECloudDownload extends PlaceholderCommand {

    public CommandECloudDownload() {
        super("download");
    }

    private boolean isBlockedExpansion(String name) {
        String env = System.getenv("PAPI_BLOCKED_EXPANSIONS");
        if (env == null) {
            return false;
        }

        return Arrays.stream(env.split(","))
                .anyMatch(s -> s.equalsIgnoreCase(name));
    }

    @Override
    public void evaluate(@NotNull final PlaceholderAPIPlugin plugin,
                         @NotNull final CommandSender sender, @NotNull final String alias,
                         @NotNull @Unmodifiable final List<String> params) {
        if (params.isEmpty()) {
            sender.sendMessage(Message.raw("You must supply the name of an expansion.").color(Color.RED));
//            Msg.msg(sender,
//                    "&cYou must supply the name of an expansion.");
            return;
        }

        if (isBlockedExpansion(params.get(0))) {
            sender.sendMessage(Message.raw("This expansion can't be downloaded.").color(Color.RED));
//            Msg.msg(sender,
//                    "&cThis expansion can't be downloaded.");
            return;
        }

        final CloudExpansion expansion = plugin.cloudExpansionManager()
                .findCloudExpansionByName(params.get(0)).orElse(null);
        if (expansion == null) {
            sender.sendMessage(Message.raw("Failed to find an expansion named: ").color(Color.GREEN).insert(Message.raw(params.get(0)).color(Color.WHITE)));
//            Msg.msg(sender,
//                    "&cFailed to find an expansion named: &f" + params.get(0));
            return;
        }

        final CloudExpansion.Version version;
        if (params.size() < 2) {
            version = expansion.getVersion(expansion.getLatestVersion());
            if (version == null) {
                sender.sendMessage(Message.raw("Could not find latest version for expansion.").color(Color.RED));
//                Msg.msg(sender,
//                        "&cCould not find latest version for expansion.");
                return;
            }
        } else {
            version = expansion.getVersion(params.get(1));
            if (version == null) {
                sender.sendMessage(Message.raw("Could not find specified version: ").color(Color.RED)
                        .insert(Message.raw(params.get(0) + "\n").color(Color.WHITE))
                        .insert(Message.raw("Available versions: ").color(Color.GRAY))
                        .insert(Message.raw(expansion.getAvailableVersions().toString()).color(Color.WHITE)));
//                Msg.msg(sender,
//                        "&cCould not find specified version: &f" + params.get(1),
//                        "&7Available versions: &f" + expansion.getAvailableVersions());
                return;
            }
        }

        if (!version.isVerified()) {
            sender.sendMessage(Message.raw("The expansion: '").color(Color.RED)
                    .insert(Message.raw(params.get(0)).color(Color.WHITE))
                    .insert(Message.raw("' is not verified and can only be downloaded manually from ").color(Color.RED))
                    .insert(Message.raw("https://ecloud.placeholderapi.com").color(Color.WHITE)));
//            Msg.msg(sender, "&cThe expansion '&f" + params.get(0) + "&c' is not verified and can only be downloaded manually from &fhttps://ecloud.placeholderapi.com");
            return;
        }

        plugin.cloudExpansionManager().downloadExpansion(expansion, version)
                .whenComplete((file, exception) -> {
                    if (exception != null) {
                        sender.sendMessage(Message.raw("Failed to download expansion: ").color(Color.RED).insert(Message.raw(exception.getMessage()).color(Color.WHITE)));
//                        Msg.msg(sender,
//                                "&cFailed to download expansion: &f" + exception.getMessage());
                        return;
                    }

                    sender.sendMessage(Message.raw("Successfully downloaded expansion ").color(Color.GREEN)
                            .insert(Message.raw(expansion.getName() + " [" + version.getVersion() + "] ").color(Color.WHITE))
                            .insert(Message.raw("to file: ").color(Color.GREEN))
                            .insert(Message.raw(file.getName()).color(Color.WHITE))
                            .insert(Message.raw("\nMake sure to type ").color(Color.GREEN))
                            .insert(Message.raw("/papi reload ").color(Color.GREEN))
                            .insert(Message.raw("to enable your new expansion!").color(Color.WHITE)));
//                    Msg.msg(sender,
//                            "&aSuccessfully downloaded expansion &f" + expansion.getName() + " [" + version
//                                    .getVersion() + "] &ato file: &f" + file.getName(),
//                            "&aMake sure to type &f/papi reload &ato enable your new expansion!");

                    plugin.cloudExpansionManager().load();
                });
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
//            final Stream<String> names = plugin.getCloudExpansionManager().getCloudExpansions().values()
//                    .stream().map(CloudExpansion::getName).map(name -> name.replace(' ', '_'));
//            suggestByParameter(names, suggestions, params.isEmpty() ? null : params.get(0));
//            return;
//        }
//
//        final Optional<CloudExpansion> expansion = plugin.getCloudExpansionManager()
//                .findCloudExpansionByName(params.get(0));
//        if (!expansion.isPresent()) {
//            return;
//        }
//
//        suggestByParameter(expansion.get().getAvailableVersions().stream(), suggestions, params.get(1));
//    }

}
