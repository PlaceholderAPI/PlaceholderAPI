/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2024 PlaceholderAPI Team
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

import java.util.List;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class CommandHelp extends PlaceholderCommand {

  public CommandHelp() {
    super("help");
  }


  @Override
  public void evaluate(@NotNull final PlaceholderAPIPlugin plugin,
      @NotNull final CommandSender sender, @NotNull final String alias,
      @NotNull @Unmodifiable final List<String> params) {
    final PluginDescriptionFile description = plugin.getDescription();

    Msg.msg(sender,
        "&b&lPlaceholderAPI &8- &7Help Menu &8- &7(&f" + description.getVersion() + "&7)",
        " ",
        "&b/papi &fbcparse &9<me|--null|player name> <message>",
        "  &7&oParse a message with placeholders and broadcast it",
        "&b/papi &fcmdparse &9<me|player> <command with placeholders>",
        "  &7&oParse a message with relational placeholders",
        "&b/papi &fdump",
        "  &7&oDump all relevant information needed to help debug issues into a paste link.",
        "&b/papi &finfo &9<placeholder name>",
        "  &7&oView information for a specific expansion",
        "&b/papi &flist",
        "  &7&oList active expansions",
        "&b/papi &fparse &9<me|--null|player name> <message>",
        "  &7&oParse a message with placeholders",
        "&b/papi &fparserel &9<player one> <player two> <message>",
        "  &7&oParse a message with relational placeholders",
        "&b/papi &fregister &9<file name>",
        "  &7&oRegister an expansion by the name of the file",
        "&b/papi &freload",
        "  &7&oReload the config of PAPI",
        "&b/papi &funregister &9<expansion name>",
        "  &7&oUnregister an expansion by name",
        "&b/papi &fversion",
        "  &7&oView plugin info/version");
  }

}
