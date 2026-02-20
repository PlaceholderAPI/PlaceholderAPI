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
import java.util.stream.Collectors;

import at.helpch.placeholderapi.PlaceholderAPIPlugin;
import at.helpch.placeholderapi.commands.PlaceholderCommand;
import com.hypixel.hytale.common.plugin.AuthorInfo;
import com.hypixel.hytale.common.plugin.PluginManifest;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class CommandVersion extends PlaceholderCommand {

    public CommandVersion() {
        super("version");
        setPermissions("placeholderapi.admin", "placeholderapi.version");
    }


    @Override
    public void evaluate(@NotNull final PlaceholderAPIPlugin plugin,
                         @NotNull final CommandSender sender, @NotNull final String alias,
                         @NotNull @Unmodifiable final List<String> params) {
        final PluginManifest description = plugin.getManifest();

        sender.sendMessage(Message.empty()
                .insert(Message.raw("PlaceholderAPI ").color(Color.CYAN).bold(true))
                .insert(Message.raw("(").color(Color.LIGHT_GRAY))
                .insert(Message.raw(description.getVersion().toString()).color(Color.WHITE))
                .insert(Message.raw(")").color(Color.LIGHT_GRAY))
                .insert(Message.raw("\nAuthor: ").color(Color.LIGHT_GRAY))
                .insert(Message.raw(description.getAuthors().stream().map(AuthorInfo::getName).collect(Collectors.joining(", "))).color(Color.WHITE))
                .insert(Message.raw("\nPAPI Commands: ").color(Color.LIGHT_GRAY))
                .insert(Message.raw("/papi ").color(Color.CYAN))
                .insert(Message.raw("help").color(Color.WHITE))
                .insert(Message.raw("\neCloud Commands: ").color(Color.LIGHT_GRAY))
                .insert(Message.raw("/papi ").color(Color.CYAN))
                .insert(Message.raw("ecloud").color(Color.WHITE)));

//        Msg.msg(sender,
//                "&b&lPlaceholderAPI &7(&f" + description.getVersion() + "&7)",
//                "&7Author: &f" + description.getAuthors().stream().map(AuthorInfo::getName).collect(Collectors.joining(", ")),
//                "&7PAPI Commands: &b/papi &fhelp",
//                "&7eCloud Commands&8: &b/papi &fecloud");
    }

}
