/*
 * PlaceholderAPI
 * Copyright (C) 2019 Ryan McCarthy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.clip.placeholderapi.nukkit.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import me.clip.placeholderapi.common.PAPIPlayer;
import me.clip.placeholderapi.common.PlaceholderAPI;
import me.clip.placeholderapi.common.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.common.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.common.expansion.cloud.ExpansionCloudManager;
import me.clip.placeholderapi.nukkit.PlaceholderAPINukkitPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static me.clip.placeholderapi.common.util.Msg.color;
import static me.clip.placeholderapi.common.util.Msg.msg;

public class ExpansionCloudCommands implements CommandExecutor {
    private PlaceholderAPINukkitPlugin plugin;

    public ExpansionCloudCommands(PlaceholderAPINukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
        if (args.length == 1) {
            msg((PAPIPlayer) s, "&bExpansion cloud commands",
                    " ",
                    "&b/papi ecloud status",
                    "&fView status of the ecloud",
                    "&b/papi ecloud list <all/author> (page)",
                    "&fList all/author specific available expansions",
                    "&b/papi ecloud info <expansion name>",
                    "&fView information about a specific expansion available on the cloud",
                    "&b/papi ecloud versioninfo <expansion name> <version>",
                    "&fView information about a specific version of an expansion",
                    "&b/papi ecloud placeholders <expansion name>",
                    "&fView placeholders for an expansion",
                    "&b/papi ecloud download <expansion name> (version)",
                    "&fDownload an expansion from the ecloud",
                    "&b/papi ecloud refresh",
                    "&fFetch the most up to date list of expansions available.",
                    "&b/papi ecloud clear",
                    "&fClear the expansion cloud cache.");
            return true;
        }

        if (args[1].equalsIgnoreCase("refresh") || args[1].equalsIgnoreCase("update") || args[1].equalsIgnoreCase("fetch")) {
            msg((PAPIPlayer) s, "&aRefresh task started. Use &f/papi ecloud list all &ain a few!!");
            plugin.getExpansionCloud().clean();
            plugin.getExpansionCloud().fetch(plugin.getPlaceholderAPIConfig().cloudAllowUnverifiedExpansions());
            return true;
        }

        if (plugin.getExpansionCloud().getCloudExpansions().isEmpty()) {
            msg((PAPIPlayer) s, "&7No cloud expansions are available at this time.");
            return true;
        }

        if (args[1].equalsIgnoreCase("clear")) {
            plugin.getExpansionCloud().clean();
            msg((PAPIPlayer) s, "&aThe cache has been cleared!!");
            return true;
        }

        if (args[1].equalsIgnoreCase("status")) {
            msg((PAPIPlayer) s, "&bThere are &f" + plugin.getExpansionCloud().getCloudExpansions().size() + " &bexpansions available on the cloud.",
                    "&7A total of &f" + plugin.getExpansionCloud().getCloudAuthorCount() + " &7authors have contributed to the expansion cloud.");
            if (plugin.getExpansionCloud().getToUpdateCount() > 0) {
                msg((PAPIPlayer) s, "&eYou have &f" + plugin.getExpansionCloud().getToUpdateCount() + " &eexpansions installed that have updates available.");
            }

            return true;
        }

        if (args[1].equalsIgnoreCase("info")) {
            if (args.length < 3) {
                msg((PAPIPlayer) s, "&cAn expansion name must be specified!");
                return true;
            }

            CloudExpansion expansion = ExpansionCloudManager.getCloudExpansion(args[2]);

            if (expansion == null) {
                msg((PAPIPlayer) s, "&cNo expansion found by that name: &f" + args[2]);
                return true;
            }

            if (!(s instanceof PAPIPlayer)) {
                msg((PAPIPlayer) s, (expansion.shouldUpdate() ? "&e" : "") + expansion.getName() + " &8&m-- &r" + expansion.getVersion().getUrl());
                return true;
            }

            msg((PAPIPlayer) s, "&bExpansion&7: &f" + expansion.getName(),
                    "&bAuthor: &f" + expansion.getAuthor(),
                    "&bVerified: &f" + expansion.isVerified(),
                    "&bLatest version: &f" + expansion.getLatestVersion(),
                    "&bVersions available: &f" + expansion.getVersions().size()
            );

            // TODO: Add the hover and command suggestions if Nukkit supports it

            if (expansion.getPlaceholders() != null) {
                // TODO: Add the hover and command suggestions if Nukkit supports it
                msg((PAPIPlayer) s, "&bPlaceholders: &f" + expansion.getPlaceholders().size());
            }

            return true;
        }

        if (args[1].equalsIgnoreCase("versioninfo")) {
            if (args.length < 4) {
                msg((PAPIPlayer) s, "&cAn expansion name and version must be specified!");
                return true;
            }

            CloudExpansion expansion = ExpansionCloudManager.getCloudExpansion(args[2]);

            if (expansion == null) {
                msg((PAPIPlayer) s, "&cNo expansion found by the name: &f" + args[2]);
                return true;
            }

            CloudExpansion.Version version = expansion.getVersion(args[3]);

            if (version == null) {
                msg((PAPIPlayer) s, "&cThe version specified does not exist for expansion: &f" + expansion.getName());
                return true;
            }

            msg((PAPIPlayer) s, "&bExpansion: " + (expansion.shouldUpdate() ? "&e" : "&f") + expansion.getName(),
                    "&bVersion: &f" + version.getVersion(),
                    "&bVersion info: &f" + version.getReleaseNotes()
            );

            if (!(s instanceof PAPIPlayer)) {
                msg((PAPIPlayer) s, "&bDownload url: " + version.getUrl());
                return true;
            }

            // TODO: Add the hover and command suggestions if Nukkit supports it

            return true;
        }

        if (args[1].equalsIgnoreCase("placeholders")) {
            if (args.length < 3) {
                msg((PAPIPlayer) s, "&cAn expansion name must be specified!");
                return true;
            }

            CloudExpansion expansion = ExpansionCloudManager.getCloudExpansion(args[2]);

            if (expansion == null) {
                msg((PAPIPlayer) s, "&cNo expansion found by the name: &f" + args[2]);
                return true;
            }

            List<String> placeholders = expansion.getPlaceholders();

            if (placeholders == null) {
                msg((PAPIPlayer) s, "&cThe expansion: &f" + expansion.getName() + " &cdoes not have any placeholders listed.",
                        "&7You should contact &f" + expansion.getAuthor() + " &7and ask for them to be added.");
                return true;
            }

            if (!(s instanceof PAPIPlayer) || plugin.getExpansionManager().getRegisteredExpansion(expansion.getName()) == null) {
                msg((PAPIPlayer) s, "&bPlaceholders: &f" + placeholders.size(), String.join("&a, &f", placeholders));
                return true;
            }

            Player p = (Player) s;
            StringBuilder message = new StringBuilder(color("&bPlaceholders: &f" + placeholders.size()));
            message.append("\n");

            for (int i = 0; i < placeholders.size(); i++) {
                if (i == placeholders.size() - 1) {
                    message.append(placeholders.get(i));
                } else {
                    message.append(color(placeholders.get(i) + "&b, &f"));
                }
                // TODO: Add the hover and command suggestions if Nukkit supports it
            }

            p.sendMessage(message.toString());
            return true;
        }

        if (args[1].equalsIgnoreCase("list")) {
            int page = 1;
            String author;
            boolean installed = false;

            if (args.length < 3) {
                msg((PAPIPlayer) s, "&cIncorrect usage! &7/papi ecloud list <all/author/installed> (page)");
                return true;
            }

            author = args[2];

            if (author.equalsIgnoreCase("all")) {
                author = null;
            } else if (author.equalsIgnoreCase("installed")) {
                author = null;
                installed = true;
            }

            if (args.length >= 4) {
                try {
                    page = Integer.parseInt(args[3]);
                } catch (NumberFormatException ex) {
                    msg((PAPIPlayer) s, "&cPage number must be an integer!");
                    return true;
                }
            }

            if (page < 1) {
                msg((PAPIPlayer) s, "&cPage must be greater than or equal to 1!");
                return true;
            }

            int avail;

            Map<Integer, CloudExpansion> ex;

            if (installed) {
                ex = plugin.getExpansionCloud().getAllInstalled();
            } else if (author == null) {
                ex = plugin.getExpansionCloud().getCloudExpansions();
            } else {
                ex = plugin.getExpansionCloud().getAllByAuthor(author);
            }

            if (ex == null || ex.isEmpty()) {
                msg((PAPIPlayer) s, "&cNo expansions available" + (author != null ? " for author &f" + author : ""));
                return true;
            }

            avail = plugin.getExpansionCloud().getPagesAvailable(ex, 10);

            if (page > avail) {
                msg((PAPIPlayer) s, "&cThere " + ((avail == 1) ? " is only &f" + avail + " &cpage available!" : "are only &f" + avail + " &cpages available!"));
                return true;
            }

            msg((PAPIPlayer) s, "&bShowing expansions for&7: &f" + (author != null ? author : (installed ? "all installed" : "all available")) +
                    " &8&m--&r &bamount&7: &f" + ex.size() + " &bpage&7: &f" + page + "&7/&f" + avail);

            ex = plugin.getExpansionCloud().getPage(ex, page, 10);

            if (ex == null) {
                msg((PAPIPlayer) s, "&cThere was a problem getting the requested page...");
                return true;
            }

            msg((PAPIPlayer) s, "&aGreen = Expansions you have");
            msg((PAPIPlayer) s, "&6Gold = Expansions which need updated");

            if (!(s instanceof Player)) {
                Map<String, CloudExpansion> expansions = new HashMap<>();
                for (CloudExpansion exp : ex.values()) {
                    if (exp == null || exp.getName() == null) {
                        continue;
                    }
                    expansions.put(exp.getName(), exp);
                }

                List<String> ce = expansions.keySet().stream().sorted().collect(Collectors.toList());
                int i = (int) ex.keySet().toArray()[0];

                for (String name : ce) {
                    if (expansions.get(name) == null) {
                        continue;
                    }

                    CloudExpansion expansion = expansions.get(name);
                    msg((PAPIPlayer) s, "&b" + i + "&7: " + (expansion.shouldUpdate() ? "&6" : (expansion.hasExpansion() ? "&a" : "&7")) + expansion.getName() +
                            " &8&m-- &r" + expansion.getVersion().getUrl());
                    i++;
                }

                return true;
            }

            Map<String, CloudExpansion> expansions = new HashMap<>();

            for (CloudExpansion exp : ex.values()) {
                if (exp == null || exp.getName() == null) {
                    continue;
                }

                expansions.put(exp.getName(), exp);
            }

            // TODO Hovering and suggesting commands

            return true;
        }

        if (args[1].equalsIgnoreCase("download")) {
            if (args.length < 3) {
                msg((PAPIPlayer) s, "&cAn expansion name must be specified!");
                return true;
            }

            CloudExpansion expansion = ExpansionCloudManager.getCloudExpansion(args[2]);

            if (expansion == null) {
                msg((PAPIPlayer) s, "&cNo expansion found with the name: &f" + args[2]);
                return true;
            }

            PlaceholderExpansion loaded = plugin.getExpansionManager().getRegisteredExpansion(args[2]);

            if (loaded != null && loaded.isRegistered()) {
                PlaceholderAPI.unregisterPlaceholderHook(loaded.getIdentifier());
            }

            String version = expansion.getLatestVersion();

            if (args.length == 4) {
                version = args[3];
                if (expansion.getVersion(version) == null) {
                    msg((PAPIPlayer) s, "&cThe version you specified does not exist for &f" + expansion.getName());
                    msg((PAPIPlayer) s, "&7Available versions: &f" + expansion.getVersions().size());
                    msg((PAPIPlayer) s, String.join("&a, &f", expansion.getAvailableVersions()));
                    return true;
                }
            }

            msg((PAPIPlayer) s, "&aDownload starting for expansion: &f" + expansion.getName() + " &aversion: &f" + version);
            String player = ((s instanceof Player) ? s.getName() : null);
            plugin.getExpansionCloud().downloadExpansion(player, expansion, version);
            plugin.getExpansionCloud().clean();
            plugin.getExpansionCloud().fetch(plugin.getPlaceholderAPIConfig().cloudAllowUnverifiedExpansions());
            return true;
        }

        msg((PAPIPlayer) s, "&cIncorrect usage! &b/papi ecloud");
        return true;
    }
}