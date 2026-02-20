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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import at.helpch.placeholderapi.PlaceholderAPIPlugin;
import at.helpch.placeholderapi.commands.PlaceholderCommand;
import at.helpch.placeholderapi.expansion.cloud.CloudExpansion;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class CommandECloudExpansionPlaceholders extends PlaceholderCommand {

    public CommandECloudExpansionPlaceholders() {
        super("placeholders");
        setPermissions("placeholderapi.ecloud.*", "placeholderapi.ecloud.placeholders");
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
            sender.sendMessage(Message.raw("There is no expansion with the name: ").color(Color.RED).insert(Message.raw(params.getFirst()).color(Color.WHITE)));
//            Msg.msg(sender,
//                    "&cThere is no expansion with the name: &f" + params.get(0));
            return;
        }

        final List<String> placeholders = expansion.getPlaceholders();
        if (placeholders == null || placeholders.isEmpty()) {
            sender.sendMessage(Message.raw("The expansion specified does not have placeholders listed.").color(Color.RED));
//            Msg.msg(sender,
//                    "&cThe expansion specified does not have placeholders listed.");
            return;
        }

//        final List<List<String>> partitions = Lists
//                .partition(placeholders.stream().sorted().collect(Collectors.toList()), 10);
        final List<List<String>> partitions = new ArrayList<>(IntStream.range(0, placeholders.size()).boxed().collect(Collectors.groupingBy(i -> i/10, Collectors.mapping(placeholders::get, Collectors.toList()))).values());

        Message message = Message.raw(" ").color(Color.ORANGE)
                .insert(Message.raw(String.valueOf(placeholders.size())).color(Color.ORANGE))
                .insert(Message.raw(" placeholders: ").color(Color.LIGHT_GRAY));

        for (int i = 0; i < partitions.size(); i++) {
            if (i == 0) {
                message = message.insert(Message.raw("\n"));
            }

            final List<String> partition = partitions.get(i);
            for (int j = 0; j < partition.size(); j++) {
                message = message.insert(Message.raw(partition.get(j)).color(Color.GREEN));
                if (j < partition.size() - 1) {
                    message = message.insert(Message.raw(", ").color(Color.LIGHT_GRAY));
                }
            }

            if (i < partitions.size() - 1) {
                message = message.insert(Message.raw("\n"));
            }
        }

        sender.sendMessage(message);
//        Msg.msg(sender,
//                "&6" + placeholders.size() + "&7 placeholders: &a",
//                partitions.stream().map(partition -> String.join(", ", partition))
//                        .collect(Collectors.joining("\n")));

    }

//    @Override
//    public void complete(@NotNull final PlaceholderAPIPlugin plugin,
//                         @NotNull final CommandSender sender, @NotNull final String alias,
//                         @NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions) {
//        if (params.size() > 1) {
//            return;
//        }
//
//        final Stream<String> names = plugin.getCloudExpansionManager()
//                .getCloudExpansions()
//                .values()
//                .stream()
//                .map(CloudExpansion::getName)
//                .map(name -> name.replace(' ', '_'));
//
//        suggestByParameter(names, suggestions, params.isEmpty() ? null : params.get(0));
//    }

}
