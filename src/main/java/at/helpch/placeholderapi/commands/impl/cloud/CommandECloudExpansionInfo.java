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
import java.util.List;

import at.helpch.placeholderapi.PlaceholderAPIPlugin;
import at.helpch.placeholderapi.commands.PlaceholderCommand;
import at.helpch.placeholderapi.expansion.cloud.CloudExpansion;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class CommandECloudExpansionInfo extends PlaceholderCommand {

    public CommandECloudExpansionInfo() {
        super("info");
    }

    @Override
    public void evaluate(@NotNull final PlaceholderAPIPlugin plugin,
                         @NotNull final CommandSender sender, @NotNull final String alias,
                         @NotNull @Unmodifiable final List<String> params) {
        if (params.isEmpty()) {
            sender.sendMessage(Message.raw("You must specify the name of the expansion.").color(Color.RED));
//            Msg.msg(sender,
//                    "&cYou must specify the name of the expansion.");
            return;
        }

        final CloudExpansion expansion = plugin.cloudExpansionManager()
                .findCloudExpansionByName(params.get(0)).orElse(null);
        if (expansion == null) {
            sender.sendMessage(Message.raw("There is no expansion with the name: ").color(Color.RED).insert(Message.raw(params.get(0)).color(Color.WHITE)));
//            Msg.msg(sender,
//                    "&cThere is no expansion with the name: &f" + params.get(0));
            return;
        }

        Message message = Message.raw("Expansion: ").color(Color.CYAN)
            .insert(Message.raw(expansion.getName()).color(expansion.shouldUpdate() ? Color.YELLOW : Color.GREEN))
            .insert(Message.raw("\nAuthor: ").color(Color.CYAN))
            .insert(Message.raw(expansion.getAuthor()).color(Color.WHITE))
            .insert(Message.raw("\n"));

        if (params.size() < 2) {
                message = message
                    .insert(Message.raw("Latest Version: ").color(Color.CYAN))
                    .insert(Message.raw(expansion.getLatestVersion()).color(Color.WHITE))
                    .insert(Message.raw("\nReleased: ").color(Color.CYAN))
                    .insert(Message.raw(expansion.getTimeSinceLastUpdate() + " ago").color(Color.WHITE))
                    .insert(Message.raw("\nVerified: ").color(Color.CYAN))
                    .insert(Message.raw(expansion.getVersion().isVerified() ? "YES" : "NO")
                        .color(expansion.getVersion().isVerified() ? Color.GREEN : Color.RED)
                        .bold(true))
                    .insert(Message.raw("\nRelease Notes: ").color(Color.CYAN))
                    .insert(Message.raw(expansion.getVersion().getReleaseNotes()).color(Color.WHITE))
                    .insert(Message.raw("\n"));
        } else {
            final CloudExpansion.Version version = expansion.getVersion(params.get(1));
            if (version == null) {
                sender.sendMessage(Message.raw("Could not find specified version: ").color(Color.RED)
                        .insert(Message.raw(params.get(1)).color(Color.WHITE))
                    .insert(Message.raw("\nVersions: ").color(Color.GREEN))
                        .insert(Message.raw(expansion.getAvailableVersions().toString()).color(Color.WHITE)));
//                Msg.msg(sender,
//                        "&cCould not find specified version: &f" + params.get(1),
//                        "&aVersions: &f" + expansion.getAvailableVersions());
                return;
            }

                message = message
                    .insert(Message.raw("Version: ").color(Color.CYAN))
                    .insert(Message.raw(version.getVersion()).color(Color.WHITE))
                    .insert(Message.raw("\nVerified: ").color(Color.CYAN))
                    .insert(Message.raw(version.isVerified() ? "YES" : "NO")
                        .color(version.isVerified() ? Color.GREEN : Color.RED)
                        .bold(true))
                    .insert(Message.raw("\nRelease Notes: ").color(Color.CYAN))
                    .insert(Message.raw(version.getReleaseNotes()).color(Color.WHITE))
                    .insert(Message.raw("\nDownload URL: ").color(Color.CYAN))
                    .insert(Message.raw(version.getUrl()).color(Color.WHITE))
                    .insert(Message.raw("\n"));
        }

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
