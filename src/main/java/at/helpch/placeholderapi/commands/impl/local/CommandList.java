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

package at.helpch.placeholderapi.commands.impl.local;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import at.helpch.placeholderapi.PlaceholderAPI;
import at.helpch.placeholderapi.PlaceholderAPIPlugin;
import at.helpch.placeholderapi.commands.PlaceholderCommand;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class CommandList extends PlaceholderCommand {

    public CommandList() {
        super("list");
    }


    @Override
    public void evaluate(@NotNull final PlaceholderAPIPlugin plugin,
                         @NotNull final CommandSender sender, @NotNull final String alias,
                         @NotNull @Unmodifiable final List<String> params) {
        final List<String> identifiers = new ArrayList<>(PlaceholderAPI.getRegisteredIdentifiers());
        if (identifiers.isEmpty()) {
            sender.sendMessage(Message.raw("There are no placeholder hooks active!").color(Color.RED));
//            Msg.msg(sender, "&cThere are no placeholder hooks active!");
            return;
        }


        final List<List<String>> partitions = new ArrayList<>(IntStream.range(0, identifiers.size()).boxed().collect(Collectors.groupingBy(i -> i/10, Collectors.mapping(identifiers::get, Collectors.toList()))).values());
//        final List<List<String>> partitions = Lists
//                .partition(identifiers.stream().sorted().collect(Collectors.toList()), 10);

        Message message = Message.raw("A total of ").color(Color.GRAY)
                .insert(Message.raw(identifiers.size() + " ").color(Color.WHITE))
                .insert(Message.raw("placeholder hook(s) are active: ").color(Color.GRAY));

        for (int i = 0; i < partitions.size(); ++i) {
            final List<String> partition = partitions.get(i);

            for (int j = 0; j < partition.size(); ++j) {
                message = message.insert(Message.raw(partition.get(j)).color(Color.GREEN));

                if (j != partition.size() - 1) {
                    message = message.insert(Message.raw(", ").color(Color.GRAY));
                }
            }

            if (i != partitions.size() - 1) {
                message = message.insert(Message.raw("\n"));
            }
        }

        sender.sendMessage(message);

//        Msg.msg(sender,
//                "&7A total of &f" + identifiers.size() + "&7 placeholder hook(s) are active: &a",
//                partitions.stream().map(partition -> String.join("&7, &a", partition))
//                        .collect(Collectors.joining("\n")));
    }

}
