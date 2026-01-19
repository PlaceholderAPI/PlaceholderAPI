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

import java.util.List;
import java.util.Optional;

import at.helpch.placeholderapi.PlaceholderAPI;
import at.helpch.placeholderapi.PlaceholderAPIPlugin;
import at.helpch.placeholderapi.commands.PlaceholderCommand;
import at.helpch.placeholderapi.expansion.PlaceholderExpansion;
import at.helpch.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class CommandExpansionUnregister extends PlaceholderCommand {

    public CommandExpansionUnregister() {
        super("unregister");
    }

    @Override
    public void evaluate(@NotNull final PlaceholderAPIPlugin plugin,
                         @NotNull final CommandSender sender, @NotNull final String alias,
                         @NotNull @Unmodifiable final List<String> params) {
        if (params.isEmpty()) {
            Msg.msg(sender,
                    "&cYou must specify the name of the expansion.");
            return;
        }

        final Optional<PlaceholderExpansion> expansion = plugin.getLocalExpansionManager()
                .findExpansionByName(params.get(0));
        if (!expansion.isPresent()) {
            Msg.msg(sender,
                    "&cThere is no expansion loaded with the identifier: &f" + params.get(0));
            return;
        }

        final String message = !expansion.get().unregister() ?
                "&cFailed to unregister expansion: &f" :
                "&aSuccessfully unregistered expansion: &f";

        Msg.msg(sender, message + expansion.get().getName());
    }

    @Override
    public void complete(@NotNull final PlaceholderAPIPlugin plugin,
                         @NotNull final CommandSender sender, @NotNull final String alias,
                         @NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions) {
        if (params.size() > 1) {
            return;
        }

        suggestByParameter(PlaceholderAPI.getRegisteredIdentifiers().stream(), suggestions,
                params.isEmpty() ? null : params.get(0));
    }

}
