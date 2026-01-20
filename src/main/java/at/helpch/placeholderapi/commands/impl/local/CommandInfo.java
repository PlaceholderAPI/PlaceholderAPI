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
import java.util.List;

import at.helpch.placeholderapi.PlaceholderAPIPlugin;
import at.helpch.placeholderapi.commands.PlaceholderCommand;
import at.helpch.placeholderapi.expansion.PlaceholderExpansion;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class CommandInfo extends PlaceholderCommand {

    public CommandInfo() {
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

        final PlaceholderExpansion expansion = plugin.localExpansionManager()
                .findExpansionByIdentifier(params.get(0)).orElse(null);
        if (expansion == null) {
            sender.sendMessage(Message.raw("There is no expansion loaded with the identifier: ").color(Color.RED).insert(Message.raw(params.getFirst()).color(Color.WHITE)));
//            Msg.msg(sender,
//                    "&cThere is no expansion loaded with the identifier: &f" + params.get(0));
            return;
        }

        Message message = Message.empty()
                .insert(Message.raw("Placeholder expansion info for: &r").color(Color.GRAY))
                .insert(Message.raw(expansion.getName() + "\n").color(Color.WHITE))
                .insert(Message.raw("Status: ").color(Color.GRAY))
                .insert(Message.raw(expansion.isRegistered() ? "Registered" : "Not Registered").color(expansion.isRegistered() ? Color.GRAY : Color.RED))
                .insert("\n");

        final String author = expansion.getAuthor();
        if (author != null) {
            message = message.insert(Message.raw("Author: ").color(Color.GRAY))
                            .insert(Message.raw(author + "\n").color(Color.WHITE));
//            builder.append("&7Author: &r")
//                    .append(author)
//                    .append('\n');
        }

        final String version = expansion.getVersion();
        if (version != null) {
            message = message.insert(Message.raw("Version: ").color(Color.GRAY))
                    .insert(Message.raw(version + "\n").color(Color.WHITE));

//            builder.append("&7Version: &r")
//                    .append(version)
//                    .append('\n');
        }

        final String requiredPlugin = expansion.getRequiredPlugin();
        if (requiredPlugin != null) {
            message = message.insert(Message.raw("Requires plugin: ").color(Color.GRAY))
                    .insert(Message.raw(requiredPlugin + '\n').color(Color.WHITE));

//            builder.append("&7Requires plugin: &r")
//                    .append(requiredPlugin)
//                    .append('\n');
        }

        final List<String> placeholders = expansion.getPlaceholders();
        if (placeholders != null && !placeholders.isEmpty()) {
            message = message.insert(Message.raw("-- ").color(Color.DARK_GRAY))
                            .insert(Message.raw("Placeholders ").color(Color.GRAY))
                            .insert(Message.raw("--\n").color(Color.DARK_GRAY));
//            builder.append("&8&m-- &7Placeholders &8&m--&r")
//                    .append('\n');

            for (final String placeholder : placeholders) {
                message = message.insert(Message.raw(placeholder + "\n").color(Color.WHITE));
//                builder.append(placeholder)
//                        .append('\n');
            }
        }

        sender.sendMessage(message);


//        Msg.msg(sender, builder.toString());
    }

//    @Override
//    public void complete(@NotNull final PlaceholderAPIPlugin plugin,
//                         @NotNull final CommandSender sender, @NotNull final String alias,
//                         @NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions) {
//        if (params.size() > 1) {
//            return;
//        }
//
//        suggestByParameter(PlaceholderAPI.getRegisteredIdentifiers().stream(), suggestions,
//                params.isEmpty() ? null : params.get(0));
//    }

}
