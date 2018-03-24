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
package me.clip.placeholderapi.commands.spigot;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.util.Msg;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
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
		
		if (args[1].equalsIgnoreCase("refresh") || args[1].equalsIgnoreCase("update") || args[1].equalsIgnoreCase("fetch")) {
			Msg.msg(s, "&aRefresh task started. Use &f/papi ecloud list all &ain a few!!");
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
		
		if (args[1].equalsIgnoreCase("status")) {
			
			Msg.msg(s, "&bThere are &f" + plugin.getExpansionCloud().getCloudExpansions().size() + " &bexpansions available on the cloud.",
			"&7A total of &f" + plugin.getExpansionCloud().getCloudAuthorCount() + " &7authors have contributed to the expansion cloud.");
			if (plugin.getExpansionCloud().getToUpdateCount() > 0) {
				Msg.msg(s, "&eYou have &f" + plugin.getExpansionCloud().getToUpdateCount() 
						+ " &eexpansions installed that have updates available.");
			}
			
			return true;
		} 
		
		if (args[1].equalsIgnoreCase("info")) {
			
			if (args.length < 3) {
				Msg.msg(s, "&cAn expansion name must be specified!");
				return true;
			}
			
			CloudExpansion expansion = plugin.getExpansionCloud().getCloudExpansion(args[2]);
			
			if (expansion == null) {
				Msg.msg(s, "&cNo expansion found with the name: &f" + args[2]);
				return true;
			}
			
			if (!(s instanceof Player)) {
				Msg.msg(s, (expansion.shouldUpdate() ? "&e" : "") + expansion.getName() + " &8&m-- &r" + expansion.getLink());
				return true;
			}
			
			Player p = (Player) s;
			
			Msg.msg(s, "&bCloud expansion info for&7:" + (expansion.shouldUpdate() ? "&6" : (expansion.hasExpansion() ? "&e" : "")) + expansion.getName());
			
			StringBuilder sb = new StringBuilder();
			
			sb.append(expansion.getDescription());
			
			if (expansion.getReleaseNotes() != null) {
				sb.append("\n\n" + expansion.getReleaseNotes());
			}
			
			String hover = ChatColor.translateAlternateColorCodes('&', sb.toString());
			
			if (expansion.hasExpansion()) {
				if (expansion.shouldUpdate()) {
					Msg.msg(s, "&6You have this expansion but there is a newer version available.");
				} else {
					Msg.msg(s, "&aYou have the latest version of this expansion!");
				}
			} else {
				Msg.msg(s, "&7You do not have this expansion installed");
			}
			
			sms(p, "&bAuthor&7: &f" + expansion.getAuthor(), hover, null);
			sms(p, "&bVersion&7: &f" + expansion.getVersion(), hover, null);
			if (expansion.getLastUpdate() > 1) {
				sb.append("&bLast updated&7: &f" + expansion.getTimeSinceLastUpdate() + " ago");
			}
			sms(p, "&aClick here to download!", hover, expansion.getName());
			return true;
		} 
		
		if (args[1].equalsIgnoreCase("list")) {
			
			int page = 1;
			
			String author;
			boolean installed = false;
			
			if (args.length < 3) {
				Msg.msg(s, "&cIncorrect usage! &7/papi ecloud list <all/author/installed> (page)");
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
			
			if (installed) {
				ex = plugin.getExpansionCloud().getAllInstalled();
			} else if (author == null) {
				ex = plugin.getExpansionCloud().getCloudExpansions();
			} else {
				ex = plugin.getExpansionCloud().getAllByAuthor(author);
			}
			
			if (ex == null || ex.isEmpty()) {
				Msg.msg(s, "&cNo expansions available" + (author != null ? " for author &f" + author : ""));
				return true;
			}
			
			avail = plugin.getExpansionCloud().getPagesAvailable(ex, 10);
			
			if (page > avail) {
				Msg.msg(s, "&cThere " + ((avail == 1) ? " is only &f" + avail + " &cpage available!" : "are only &f" + avail + " &cpages available!"));
				return true;
			}			
			
			Msg.msg(s, "&bShowing expansions for&7: &f" + (author != null ? author : (installed ? "all installed" : "all available"))+ " &8&m--&r &bamount&7: &f" + ex.size() + " &bpage&7: &f" + page + "&7/&f" + avail);
			
			ex = plugin.getExpansionCloud().getPage(ex, page, 10);
			
			if (ex == null) {
				Msg.msg(s, "&cThere was a problem getting the requested page...");
				return true;
			}
			
			Msg.msg(s, "&aGreen = Expansions you have");
			Msg.msg(s, "&6Gold = Expansions which need updated");
			
			if (!(s instanceof Player)) {
				
				for (Entry<Integer, CloudExpansion> expansion : ex.entrySet()) {
					if (expansion == null || expansion.getKey() == null || expansion.getValue() == null) continue;
					Msg.msg(s, "&b" + (expansion.getKey()+1) + "&7: " + (expansion.getValue().shouldUpdate() ? "&6" : (expansion.getValue().hasExpansion() ? "&a" : "&7")) + expansion.getValue().getName() + " &8&m-- &r" + expansion.getValue().getLink());
				}
				
				return true;
			}  
			
			Player p = (Player) s;
			
			for (Entry<Integer, CloudExpansion> expansion : ex.entrySet()) {
				
				if (expansion == null || expansion.getValue() == null) {
					continue;
				}
				
				StringBuilder sb = new StringBuilder();
				if (expansion.getValue().shouldUpdate()) {
					sb.append("&6Click to update to the latest version of this expansion\n\n");
				} else if (!expansion.getValue().hasExpansion()) {
					sb.append("&bClick to download this expansion\n\n");
				} else {
					sb.append("&aYou have the latest version of this expansion\n\n");
				}
				sb.append("&bAuthor&7: &f" + expansion.getValue().getAuthor() + "\n");
				sb.append("&bVersion&7: &f" + expansion.getValue().getVersion() + "\n");
				if (expansion.getValue().getLastUpdate() > 1) {
					sb.append("&bLast updated&7: &f" + expansion.getValue().getTimeSinceLastUpdate() + " ago\n");
				}
				if (expansion.getValue().getReleaseNotes() != null) {
					sb.append("&bRelease Notes&7: &f" + expansion.getValue().getReleaseNotes() + "\n");
				}
				sb.append("\n" + expansion.getValue().getDescription());

				String msg = ChatColor.translateAlternateColorCodes('&', "&b" + (expansion.getKey()+1) + "&7: " + (expansion.getValue().shouldUpdate() ? "&6" : (expansion.getValue().hasExpansion() ? "&a" : "")) + expansion.getValue().getName());
				
				String hover = ChatColor.translateAlternateColorCodes('&', sb.toString());
				
				sms(p, msg, hover, expansion.getValue().getName());
			}
			
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
			
			if (expansion.hasExpansion() && !expansion.shouldUpdate()) {
				Msg.msg(s, "&aYou already have this expansion installed and your version is up to date!");
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
		
		Msg.msg(s, "&cIncorrect usage! &b/papi ecloud");
		return true;
	}
	
	private void sms(Player p, String text, String hover, String name) {
		TextComponent message = new TextComponent( ChatColor.translateAlternateColorCodes('&', text) );
		if (hover != null) {
			message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', hover)).create() ) );
		}
		if (name != null) {
			message.setClickEvent( new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "/papi ecloud download " + name) );
		}
		p.spigot().sendMessage( message );
	}
	
}
