/*
 *
 * PlaceholderAPI
 * Copyright (C) 2018 Ryan McCarthy
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
 *
 *
 */
package me.clip.placeholderapi.commands.bukkit;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Map.Entry;

public class ExpansionCloudCommands implements CommandExecutor {

	private PlaceholderAPIPlugin plugin;
	
	public ExpansionCloudCommands(PlaceholderAPIPlugin instance) {
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
		
		if (args.length == 1) {
			Msg.msg(s, "&bExpansion cloud commands",
			" ",
			"&b/papi ecloud status",
			"&fView status of the cloud",
			"&b/papi ecloud list <all/author> (page)",
			"&fList all/author specific available expansions",
			"&b/papi ecloud info <expansion name>",
			"&fView information about a specific expansion available on the cloud",
			"&b/papi ecloud download <expansion name>",
			"&fDownload a specific expansion from the cloud",
			"&b/papi ecloud refresh",
			"&fFetch the most up to date list of expansions available.",
			"&b/papi ecloud clear",
			"&fClear the expansion cloud cache.");
			return true;
		}
		
		if (args[1].equalsIgnoreCase("refresh")) {
			Msg.msg(s, "&aRefresh task started. Use &7/papi ecloud list all &fin a few!!");
			plugin.getExpansionCloud().clean();
			plugin.getExpansionCloud().fetch();
			return true;
		}
		
		if (plugin.getExpansionCloud().getCloudExpansions().isEmpty()) {
			Msg.msg(s, "&7No cloud expansions are available at this time.");
			return true;
		}
		
		if (args[1].equalsIgnoreCase("clear")) {
			plugin.getExpansionCloud().clean();
			Msg.msg(s, "&aThe cloud cache has been cleared!!");
			return true;
		}
		
		if (args[1].equalsIgnoreCase("download")) {
			
			if (args.length < 3) {
				Msg.msg(s, "&cAn expansion name must be specified!");
				return true;
			}
			
			CloudExpansion expansion = plugin.getExpansionCloud().getCloudExpansion(args[2]);
			
			if (expansion == null) {
				Msg.msg(s, "&cNo expansion found with the name: &f" + args[2]);
				return true;
			}
			
			PlaceholderExpansion loaded = plugin.getExpansionManager().getRegisteredExpansion(args[2]);
			
			if (loaded != null && loaded.isRegistered()) {
				PlaceholderAPI.unregisterPlaceholderHook(loaded.getIdentifier());
			}
			
			Msg.msg(s, "&aAttempting download of expansion &f" + expansion.getName());
			
			String player = ((s instanceof Player) ? s.getName() : null);
			
			plugin.getExpansionCloud().downloadExpansion(player, expansion);
			
			return true;
		}
		
		if (args[1].equalsIgnoreCase("status")) {
			
			Msg.msg(s, "&bThere are &f" + plugin.getExpansionCloud().getCloudExpansions().size() + " &bcloud expansions available to download",
			"&bA total of &f" + plugin.getExpansionCloud().getCloudAuthorCount() + " &bauthors have contributed.");
			
			return true;
		} else if (args[1].equalsIgnoreCase("info")) {
			
			if (args.length < 3) {
				Msg.msg(s, "&cAn expansion name must be specified!");
				return true;
			}
			
			CloudExpansion expansion = plugin.getExpansionCloud().getCloudExpansion(args[2]);
			
			if (expansion == null) {
				Msg.msg(s, "&cNo expansion found with the name: &f" + args[2]);
				return true;
			}
			
			PlaceholderExpansion exp = plugin.getExpansionManager().getRegisteredExpansion(args[2]);
			
			boolean enabled = false;
			String version = null;
			
			if (exp != null) {
				enabled = exp.isRegistered();
				version = exp.getVersion();
			}
			
			Msg.msg(s, "&aExpansion: &f" + expansion.getName());
			if (enabled) {
				Msg.msg(s, "&aThis expansion is currently enabled!",
				"&bYour version&7: &f" + version);
			}
			
			Msg.msg(s, "&bCloud version&7: &f" + expansion.getVersion(),
			"&bAuthor&7: &f" + expansion.getAuthor());
			
			String desc = expansion.getVersion();
			
			if (desc.indexOf("\n") > 0) {
				for (String line : desc.split("\n")) {
					Msg.msg(s, line);
				}
			} else {
				Msg.msg(s, desc);
			}
			
			Msg.msg(s, "&bDownload with &7/papi ecloud download " + expansion.getName());
			return true;
			
		} else if (args[1].equalsIgnoreCase("list")) {
			
			int page = 1;
			
			String author;
			
			if (args.length < 3) {
				Msg.msg(s, "&cIncorrect usage! &7/papi ecloud list <all/author> (page)");
				return true;
			}
			
			author = args[2];
			
			if (author.equalsIgnoreCase("all")) {
				author = null;
			}
			
			if (args.length >= 4) {
				try {
					page = Integer.parseInt(args[3]);
				} catch (NumberFormatException ex) {
					Msg.msg(s, "&cPage number must be an integer!");
					return true;
				}
			}
			
			if (page < 1) {
				Msg.msg(s, "&cPage must be greater than or equal to 1!");
				return true;
			}
			
			int avail;
			
			Map<Integer, CloudExpansion> ex;
			
			if (author == null) {
				ex = plugin.getExpansionCloud().getCloudExpansions();
			} else {
				ex = plugin.getExpansionCloud().getAllByAuthor(author);
			}
			
			if (ex == null) {
				Msg.msg(s, "&cNo expansions available" + (author != null ? " for author &f" + author : ""));
				return true;
			}
			
			avail = plugin.getExpansionCloud().getPagesAvailable(ex, 10);
			
			if (page > avail) {
				Msg.msg(s, "&cThere " + ((avail == 1) ? " is only &f" + avail + " &cpage available!" : "are only &f" + avail + " &cpages available!"));
				return true;
			}			
			
			Msg.msg(s, "&bExpansion cloud for &f" + (author != null ? author : "all available")+ " &8&m-- &r&bamount&7: &f" + ex.size() + " &bpage&7: &f" + page + "&7/&f" + avail);
			
			ex = plugin.getExpansionCloud().getPage(ex, page, 10);
			
			for (Entry<Integer, CloudExpansion> expansion : ex.entrySet()) {
				Msg.msg(s, "&b" + (expansion.getKey()+1) + "&7: &f" + expansion.getValue().getName() + " &8&m-- &r" + expansion.getValue().getLink());
			}
			Msg.msg(s, "&bDownload an expansion with &7/papi ecloud download <name>",
			"&bView more info on an expansion with &7/papi ecloud info <expansion>");
			return true;
		}
		
		return true;
	}
	
}
