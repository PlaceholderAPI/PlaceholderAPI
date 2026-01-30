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

package at.helpch.placeholderapi.commands;


import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import at.helpch.placeholderapi.PlaceholderAPIPlugin;
import at.helpch.placeholderapi.commands.impl.cloud.CommandECloud;
import at.helpch.placeholderapi.commands.impl.local.*;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.*;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public final class PlaceholderCommandRouter extends AbstractCommand {

    @Unmodifiable
    private static final List<PlaceholderCommand> COMMANDS = List.of(new CommandHelp(),
            new CommandInfo(),
            new CommandList(),
            new CommandDump(),
            new CommandECloud(),
            new CommandParse(),
            new CommandReload(),
            new CommandVersion(),
            new CommandExpansionRegister(),
            new CommandExpansionUnregister());


    @NotNull
    private final PlaceholderAPIPlugin plugin;
    @NotNull
    @Unmodifiable
    private final Map<String, PlaceholderCommand> commands;

    @Override
    public String getName() {
        return "papi";
    }

    public PlaceholderCommandRouter(@NotNull final PlaceholderAPIPlugin plugin) {
        super("papi", "papi");
        addAliases("placeholderapi");
        setAllowsExtraArguments(true);

        this.plugin = plugin;

        final Map<String, PlaceholderCommand> commands = new HashMap<>();

        for (final PlaceholderCommand command : COMMANDS) {
            command.getLabels().forEach(label -> commands.put(label, command));
        }

        this.commands = commands;
    }

    @Override
    protected @Nullable CompletableFuture<Void> execute(@NotNull final CommandContext context) {
        final String[] args = context.getInputString().replace("papi", "").replace("placeholderapi", "").trim().split(" ");
        final CommandSender sender = context.sender();

        if (args.length == 0 || args[0].isBlank()) {
            final PlaceholderCommand fallback = commands.get("version");
            if (fallback != null) {
                fallback.evaluate(plugin, sender, "", Collections.emptyList());
            }

            return CompletableFuture.completedFuture(null);
        }

        final String search = args[0].toLowerCase(Locale.ROOT);
        final PlaceholderCommand target = commands.get(search);

        if (target == null) {
            sender.sendMessage(Message.raw("Unknown command ").color(Color.RED).insert(Message.raw(search).color(Color.GRAY)));

            return CompletableFuture.completedFuture(null);
        }

        final String permission = target.getPermission();
        if (permission != null && !permission.isEmpty() && !sender.hasPermission(permission)) {
            sender.sendMessage(Message.raw("You do not have permission to do this!").color(Color.RED));

            return CompletableFuture.completedFuture(null);
        }

        target
                .evaluate(plugin, sender, search, Arrays.asList(Arrays.copyOfRange(args, 1, args.length)));

        return CompletableFuture.completedFuture(null);
    }

}
