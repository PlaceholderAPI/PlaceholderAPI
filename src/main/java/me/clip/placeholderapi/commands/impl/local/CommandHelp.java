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
import me.clip.placeholderapi.util.ColorPalette;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
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
        final PluginDescriptionFile description = plugin.getDescription();
        final Collection<PlaceholderCommand> commands = PlaceholderCommandRouter.COMMANDS;
        Msg.msg(sender, "&b&lPlaceholderAPI &8- &7Help Menu &8- &7(&f" + description.getVersion() + "&7)");

        if (!(sender instanceof Player))
        {
            final StringBuilder builder = new StringBuilder();
            for (final PlaceholderCommand command : commands)
            {
                if (command.equals(this))
                {
                    continue;
                }

                builder.append(ColorPalette.MAIN_GRAY.getColor()).append(" • ").append(ColorPalette.MAIN_BLUE.getColor()).append("/papi ").append(ColorPalette.MAIN_WHITE.getColor()).append(command.getLabel()).append("\n");
                builder.append(ColorPalette.MAIN_GRAY.getColor()).append(ColorPalette.ITALIC.getColor()).append("   ").append(command.getDescription()).append("\n");
            }

            sender.sendMessage(Msg.color(builder.toString()));
            return;
        }

        final Player player = (Player) sender;
        for (final PlaceholderCommand command : PlaceholderCommandRouter.COMMANDS)
        {
            if (command.equals(this))
            {
                continue;
            }

            final JSONMessage message = JSONMessage.create(Msg.color(ColorPalette.MAIN_GRAY.getColor() + " • " + ColorPalette.MAIN_BLUE.getColor() + "/papi " + ColorPalette.MAIN_WHITE.getColor() + command.getLabel()));
            final String tooltip = ColorPalette.MAIN_GRAY.getColor() + command.getDescription() + "\n\n" + ColorPalette.MAIN_GRAY.getColor() + "Permission: " + ColorPalette.MAIN_WHITE.getColor() + ColorPalette.UNDERLINE.getColor() + command.getPermission();

            message.tooltip(Msg.color(tooltip));
            message.send(player);
        }
    }

}
