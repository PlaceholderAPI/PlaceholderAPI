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
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

import at.helpch.placeholderapi.PlaceholderAPI;
import at.helpch.placeholderapi.PlaceholderAPIPlugin;
import at.helpch.placeholderapi.commands.PlaceholderCommand;
import at.helpch.placeholderapi.expansion.PlaceholderExpansion;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.NameMatching;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public final class CommandParse extends PlaceholderCommand {

    public CommandParse() {
        super("parse", "bcparse", "parserel", "cmdparse");
        setPermissions("placeholderapi.admin", "placeholderapi.parse");
    }


    @Override
    public void evaluate(@NotNull final PlaceholderAPIPlugin plugin,
                         @NotNull final CommandSender sender, @NotNull final String alias,
                         @NotNull @Unmodifiable final List<String> params) {
        final Runnable logic = () -> {
            switch (alias.toLowerCase(Locale.ROOT)) {
                case "parserel":
                    evaluateParseRelation(sender, params);
                    break;
                case "parse":
                    evaluateParseSingular(sender, params, false, false);
                    break;
                case "bcparse":
                    evaluateParseSingular(sender, params, true, false);
                    break;
                case "cmdparse":
                    evaluateParseSingular(sender, params, false, true);
                    break;
            };
        };

        final World world;

        if (sender instanceof Player) {
            world = ((Player) sender).getWorld();
        } else if (sender instanceof PlayerRef) {
            UUID uuid = ((PlayerRef) sender).getWorldUuid();
            world = uuid == null ? Universe.get().getDefaultWorld() : Universe.get().getWorld(uuid);
        } else {
            world = Universe.get().getDefaultWorld();
        }

        if (world != null) {
            world.execute(logic);
        } else {
            logic.run();
        }
    }

    private void evaluateParseSingular(@NotNull final CommandSender sender,
                                       @NotNull @Unmodifiable final List<String> params, final boolean broadcast,
                                       final boolean command) {
        if (params.size() < 2) {
            sender.sendMessage(Message.raw("You must provide a target and a message: ").color(Color.RED)
                    .insert(Message.raw("/papi ").color(Color.CYAN))
                    .insert(Message.raw(command ? "cmdparse" : (broadcast ? "bcparse" : "parse")).color(Color.CYAN))
                    .insert(Message.raw(" {target}").color(Color.GRAY))
                    .insert(Message.raw(" {message}").color(Color.GREEN)));
            return;
        }

        PlayerRef player;

        if ("me".equalsIgnoreCase(params.getFirst())) {
            if (!(sender instanceof Player) && !(sender instanceof PlayerRef)) {
                sender.sendMessage(Message.raw("You must be a player to use ").color(Color.RED).insert(Message.raw("me").color(Color.GRAY)).insert(Message.raw(" as a target!").color(Color.RED)));
                return;
            }

            if (sender instanceof Player) {
                player = ((Player) sender).getPlayerRef();
            } else {
                player = (PlayerRef) sender;
            }
        } else if ("--null".equalsIgnoreCase(params.get(0))) {
            player = null;
        } else {
            final PlayerRef target = resolvePlayer(params.get(0));
            if (target == null) {
                sender.sendMessage(Message.raw("Failed to find player: ").color(Color.RED).insert(Message.raw(params.get(0)).color(Color.WHITE)));
                return;
            }

            player = target;
        }

        final String message = PlaceholderAPI
                .setPlaceholders(player, String.join(" ", params.subList(1, params.size())));

        if (command) {
            sender.sendMessage(Message.raw("To be implemented")); // todo: implement
            return;
        }

        if (broadcast) {
            Universe.get().sendMessage(Message.raw(message));
        } else {
            sender.sendMessage(Message.raw(message));
        }
    }

    private void evaluateParseRelation(@NotNull final CommandSender sender,
                                       @NotNull @Unmodifiable final List<String> params) {
        if (params.size() < 3) {
            sender.sendMessage(Message.raw("You must supply two targets, and a message: ").color(Color.RED)
                    .insert(Message.raw("/papi parserel ").color(Color.CYAN))
                    .insert(Message.raw("{target one} {target two} ").color(Color.GRAY))
                    .insert(Message.raw("{message}").color(Color.GREEN)));
            return;
        }

        PlayerRef playerOne;

        if ("me".equalsIgnoreCase(params.get(0))) {
            if (!(sender instanceof Player) && !(sender instanceof PlayerRef)) {
                sender.sendMessage(Message.raw("You must be a player to use ").color(Color.RED)
                        .insert(Message.raw("me").color(Color.GRAY))
                        .insert(Message.raw(" as a target!").color(Color.RED)));
                return;
            }

            if (sender instanceof Player) {
                playerOne = ((Player) sender).getPlayerRef();
            } else {
                playerOne = (PlayerRef) sender;
            }
        } else {
            playerOne = resolvePlayer(params.get(0));
        }

        if (playerOne == null) {
            sender.sendMessage(Message.raw("Failed to find player: ").color(Color.RED).insert(Message.raw(params.get(0)).color(Color.WHITE)));
            return;
        }

        PlayerRef playerTwo;

        if ("me".equalsIgnoreCase(params.get(1))) {
            if (!(sender instanceof Player) && !(sender instanceof PlayerRef)) {
                sender.sendMessage(Message.raw("You must be a player to use ").color(Color.RED).insert(Message.raw("me").color(Color.GRAY)).insert(Message.raw(" as a target!").color(Color.RED)));
                return;
            }

            if (sender instanceof Player) {
                playerTwo = ((Player) sender).getPlayerRef();
            } else {
                playerTwo = (PlayerRef) sender;
            }
        } else {
            playerTwo = resolvePlayer(params.get(1));
        }

        if (playerTwo == null) {
            sender.sendMessage(Message.raw("Failed to find player: ").color(Color.RED).insert(Message.raw(params.get(1)).color(Color.WHITE)));
            return;
        }

        final String message = PlaceholderAPI
                .setRelationalPlaceholders(playerOne, playerTwo,
                        String.join(" ", params.subList(2, params.size())));

        sender.sendMessage(Message.raw(message));
    }


    private void completeParseSingular(@NotNull final CommandSender sender,
                                       @NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions) {
        if (params.size() <= 1) {
            if (sender instanceof Player && (params.isEmpty() || "me"
                    .startsWith(params.get(0).toLowerCase(Locale.ROOT)))) {
                suggestions.add("me");
            }

            if ("--null".startsWith(params.get(0).toLowerCase(Locale.ROOT))) {
                suggestions.add("--null");
            }

            final Stream<String> names = Universe.get().getPlayers().stream().map(PlayerRef::getUsername);
            suggestByParameter(names, suggestions, params.isEmpty() ? null : params.get(0));

            return;
        }

        final String name = params.get(params.size() - 1);
        if (!name.startsWith("%") || name.endsWith("%")) {
            return;
        }

        final int index = name.indexOf('_');
        if (index == -1) {
            return; // no arguments supplied yet
        }

        final PlaceholderExpansion expansion = PlaceholderAPIPlugin.instance()
                .localExpansionManager().findExpansionByIdentifier(name.substring(1, index))
                .orElse(null);
        if (expansion == null) {
            return;
        }

        final Set<String> possible = new HashSet<>(expansion.getPlaceholders());

        PlaceholderAPIPlugin.instance()
                .cloudExpansionManager()
                .findCloudExpansionByName(expansion.getName())
                .ifPresent(cloud -> possible.addAll(cloud.getPlaceholders()));

        suggestByParameter(possible.stream(), suggestions, params.get(params.size() - 1));
    }

//    private void completeParseRelation(@NotNull @Unmodifiable final List<String> params,
//                                       @NotNull final List<String> suggestions) {
//        if (params.size() > 2) {
//            return;
//        }
//
//        final Stream<String> names = Bukkit.getOnlinePlayers().stream().map(Player::getName);
//        suggestByParameter(names, suggestions, params.isEmpty() ? null : params.get(params.size() - 1));
//    }


    @Nullable
    private PlayerRef resolvePlayer(@NotNull final String name) {
//        Player target = Universe.get().getPlayerByUsername(name, NameMatching.EXACT);
//        final Optional<Player> target = world.getPlayers().stream().filter(player -> player.getDisplayName().equals(name)).findAny();
//
//        if (target.isEmpty()) {
//            // Not the best option, but Spigot doesn't offer a good replacement (as usual)
////            target = Bukkit.getOfflinePlayer(name);
////
////            return target.hasPlayedBefore() ? target : null;
//            return null;
//        }
//
//        return target.get();
        return Universe.get().getPlayerByUsername(name, NameMatching.EXACT);
    }

}
