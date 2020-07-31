/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2020 PlaceholderAPI Team
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

package me.clip.placeholderapi.commands.impl.local;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.commands.PlaceholderCommandRouter;
import me.clip.placeholderapi.libs.JSONMessage;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public final class CommandHelp extends PlaceholderCommand
{

    public CommandHelp()
    {
        super("help", "No Description Required!");
    }

    @Override
    public void evaluate(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, @NotNull final String alias, @NotNull @Unmodifiable final List<String> params)
    {
        if (!(sender instanceof Player))
        {
            Msg.msg(sender, "&cDue to this command using JSON Messages, it can not be executed through console.");
            return;
        }

        final Player player = (Player) sender;
        final PluginDescriptionFile description = plugin.getDescription();
        Msg.msg(sender, "&b&lPlaceholderAPI &8- &7Help Menu &8- &7(&f" + description.getVersion() + "&7)");

        for (final PlaceholderCommand command : PlaceholderCommandRouter.COMMANDS)
        {
            if (command.equals(this))
            {
                continue;
            }

            final JSONMessage message = JSONMessage.create(Msg.color(" &8• &b/papi &f" + command.getLabel()));
            final String tooltip = Msg.color("&7" + command.getDescription() + "\n\n" + "&7Permission&8: &f&n" + command.getPermission());

            message.tooltip(tooltip);
            message.send(player);
        }
    }

}
