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
import at.helpch.placeholderapi.expansion.manager.CloudExpansionManager;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class CommandECloudStatus extends PlaceholderCommand {

    public CommandECloudStatus() {
        super("status");
        setPermissions("placeholderapi.ecloud.*", "placeholderapi.ecloud.status");
    }

    @Override
    public void evaluate(@NotNull final PlaceholderAPIPlugin plugin,
                         @NotNull final CommandSender sender, @NotNull final String alias,
                         @NotNull @Unmodifiable final List<String> params) {
        final CloudExpansionManager manager = plugin.cloudExpansionManager();

        final int updateCount = manager.getCloudUpdateCount();
        final int authorCount = manager.getCloudExpansionAuthorCount();
        final int expansionCount = manager.getCloudExpansions().size();

        Message message = Message.raw("There are ").color(Color.CYAN)
            .insert(Message.raw(String.valueOf(expansionCount)).color(Color.GREEN))
            .insert(Message.raw(" expansions available on the eCloud.\n").color(Color.CYAN))
            .insert(Message.raw("A total of ").color(Color.GRAY))
            .insert(Message.raw(String.valueOf(authorCount)).color(Color.WHITE))
            .insert(Message.raw(" authors have contributed Hytale expansions to the eCloud.\n").color(Color.GRAY));

        if (updateCount > 0) {
            message = message
                .insert(Message.raw("You have ").color(Color.YELLOW))
                .insert(Message.raw(String.valueOf(updateCount)).color(Color.WHITE))
                .insert(Message.raw(updateCount > 1 ? " expansions" : " expansion").color(Color.YELLOW))
                .insert(Message.raw(" installed that ").color(Color.YELLOW))
                .insert(Message.raw(updateCount > 1 ? "have an" : "has an").color(Color.YELLOW))
                .insert(Message.raw(" update available.").color(Color.YELLOW));
        }

        sender.sendMessage(message);
//        Msg.msg(sender, builder.toString());
    }

}
