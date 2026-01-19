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
import java.util.stream.Stream;

import at.helpch.placeholderapi.PlaceholderAPIPlugin;
import at.helpch.placeholderapi.commands.impl.cloud.CommandECloud;
import at.helpch.placeholderapi.commands.impl.local.CommandDump;
import at.helpch.placeholderapi.commands.impl.local.CommandExpansionRegister;
import at.helpch.placeholderapi.commands.impl.local.CommandExpansionUnregister;
import at.helpch.placeholderapi.commands.impl.local.CommandHelp;
import at.helpch.placeholderapi.commands.impl.local.CommandInfo;
import at.helpch.placeholderapi.commands.impl.local.CommandList;
import at.helpch.placeholderapi.commands.impl.local.CommandParse;
import at.helpch.placeholderapi.commands.impl.local.CommandReload;
import at.helpch.placeholderapi.commands.impl.local.CommandVersion;
import at.helpch.placeholderapi.util.Msg;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.*;
import com.hypixel.hytale.server.core.command.system.CommandSender;
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


    public PlaceholderCommandRouter(@NotNull final PlaceholderAPIPlugin plugin) {
        super("papi", "PlaceholderAPI Command");
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
    @NotNull
    public CompletableFuture<Void> acceptCall(@NotNull final CommandSender sender, @NotNull final ParserContext parserContext,
                                                        @NotNull final ParseResult parseResult) {
        final String[] args = parserContext.getInputString().split(" ");

        if (args.length == 0) {
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

//            Msg.msg(sender, "&cUnknown command &7" + search);
            return CompletableFuture.completedFuture(null);
        }

        final String permission = target.getPermission();
        if (permission != null && !permission.isEmpty() && !sender.hasPermission(permission)) {
            sender.sendMessage(Message.raw("You do not have permission to do this!").color(Color.RED));

//            Msg.msg(sender, "&cYou do not have permission to do this!");
            return CompletableFuture.completedFuture(null);
        }

        target
                .evaluate(plugin, sender, search, Arrays.asList(Arrays.copyOfRange(args, 1, args.length)));

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Nullable
    protected CompletableFuture<Void> execute(@NotNull final CommandContext commandContext) {
        return null;
    }

//    @Override
//    public List<String> onTabComplete(@NotNull final CommandSender sender,
//                                      @NotNull final Command command, @NotNull final String alias, @NotNull final String[] args) {
//        final List<String> suggestions = new ArrayList<>();
//
//        if (args.length > 1) {
//            final PlaceholderCommand target = this.commands.get(args[0].toLowerCase(Locale.ROOT));
//
//            if (target != null) {
//                target.complete(plugin, sender, args[0].toLowerCase(Locale.ROOT),
//                        Arrays.asList(Arrays.copyOfRange(args, 1, args.length)), suggestions);
//            }
//
//            return suggestions;
//        }
//
//        final Stream<String> targets = PlaceholderCommand
//                .filterByPermission(sender, commands.values().stream()).map(PlaceholderCommand::getLabels)
//                .flatMap(Collection::stream);
//        PlaceholderCommand.suggestByParameter(targets, suggestions, args.length == 0 ? null : args[0]);
//
//        return suggestions;
//    }

}
