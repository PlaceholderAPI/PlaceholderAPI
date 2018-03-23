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
import me.clip.placeholderapi.util.Msg;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class PlaceholderAPICommands implements CommandExecutor {

	private PlaceholderAPIPlugin plugin;
	
	private ExpansionCloudCommands eCloud;
	
	
	public PlaceholderAPICommands(PlaceholderAPIPlugin i) {
		plugin = i;
		eCloud = new ExpansionCloudCommands(i);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String label, String[] args) {

		if (args.length == 0) {
			Msg.msg(s, "PlaceholderAPI &7version &b&o"+plugin.getDescription().getVersion());
			Msg.msg(s, "&fCreated by&7: &bextended_clip");
			return true;
			
		} else {

			if (args[0].equalsIgnoreCase("help")) {
				Msg.msg(s, "PlaceholderAPI &aHelp &e(&f" + plugin.getDescription().getVersion() + "&e)");
				Msg.msg(s, "&b/papi");
				Msg.msg(s, "&fView plugin info/version info");
				Msg.msg(s, "&b/papi list");
				Msg.msg(s, "&fList all placeholder expansions that are currently active");
				Msg.msg(s, "&b/papi info <placeholder name>");
				Msg.msg(s, "&fView information for a specific expansion");
				Msg.msg(s, "&b/papi parse <...args>");
				Msg.msg(s, "&fParse a String with placeholders");
				Msg.msg(s, "&b/papi reload");
				Msg.msg(s, "&fReload the config settings");
				
				boolean enabled = plugin.getExpansionCloud() != null;
				
				
				if (s.isOp()) {
					if (!enabled) {
						Msg.msg(s, "&b/papi enablecloud");
						Msg.msg(s, "&fEnable the expansion cloud");
					} else {
						Msg.msg(s, "&b/papi disablecloud");
						Msg.msg(s, "&fDisable the expansion cloud");
						Msg.msg(s, "&b/papi ecloud");
						Msg.msg(s, "&fView information about the PlaceholderAPI expansion cloud");
						Msg.msg(s, "&b/papi ecloud status");
						Msg.msg(s, "&fView status of the PlaceholderAPI expansion cloud");
						Msg.msg(s, "&b/papi ecloud list <all/author> <page>");
						Msg.msg(s, "&fList all available expansions");
						Msg.msg(s, "&b/papi ecloud info <expansion name>");
						Msg.msg(s, "&fView information about a specific expansion on the cloud");
						Msg.msg(s, "&b/papi ecloud download <expansion name>");
						Msg.msg(s, "&fDownload a specific expansion from the cloud");
					}	
				}
				
				return true;
				
			} else if (args[0].equalsIgnoreCase("ecloud")) {
				if (!s.isOp()) {
					Msg.msg(s, "&cYou don't have permission to do that!");
					return true;
				}
				
				if (plugin.getExpansionCloud() == null) {
					Msg.msg(s, "&7The expansion cloud is not enabled!");
					return true;
				}
				
				return eCloud.onCommand(s, c, label, args);
				
			} else if (args[0].equalsIgnoreCase("enablecloud")) {
				
				if (!s.isOp()) {
					Msg.msg(s, "&cYou don't have permission to do that!");
					return true;
				}
				
				if (plugin.getExpansionCloud() != null) {
					Msg.msg(s, "&7The cloud is already enabled!");
					return true;
				}
				
				plugin.enableCloud();
				plugin.getPlaceholderAPIConfig().setCloudEnabled(true);
				Msg.msg(s, "&aThe cloud has been enabled!");
				return true;
				
			} else if (args[0].equalsIgnoreCase("disablecloud")) {
				
				if (!s.isOp()) {
					Msg.msg(s, "&cYou don't have permission to do that!");
					return true;
				}
				
				if (plugin.getExpansionCloud() == null) {
					Msg.msg(s, "&7The cloud is already disabled!");
					return true;
				}
				
				plugin.disableCloud();
				plugin.getPlaceholderAPIConfig().setCloudEnabled(false);
				Msg.msg(s, "&aThe cloud has been disabled!");
				return true;
				
			} else if (args[0].equalsIgnoreCase("info")) {

				if (!s.hasPermission("placeholderapi.info")) {
					Msg.msg(s, "&cYou don't have permission to do that!");
					return true;
				}
				
				PlaceholderExpansion ex = plugin.getExpansionManager().getRegisteredExpansion(args[1]);
				
				if (ex == null) {
					Msg.msg(s, "&cThere is no expansion loaded with the identifier: &f" + args[1]);
					return true;
				}
				
				Msg.msg(s, "&7Placeholder expansion info for: &f%" + ex.getIdentifier() + "_<identifier>%");
				
				Msg.msg(s, "&7Status: " + (ex.isRegistered() ? "&aRegistered" : "&cNot registered"));

				if (ex.getAuthor() != null) {
					Msg.msg(s, "&7Created by: &f" + ex.getAuthor());
				}
				
				if (ex.getVersion() != null) {
					Msg.msg(s, "&7Version: &f" + ex.getVersion());
				}
				
				if (ex.getDescription() != null) {
					Msg.msg(s, ex.getDescription());
				}
				
				if (ex.getLink() != null) {
					Msg.msg(s, "&7Link: &f" + ex.getLink());
				}
				
				if (ex.getPlugin() != null) {
					Msg.msg(s, "&7Requires plugin: &f" + ex.getPlugin());
				}
				
				if (ex.getPlaceholders() != null) {
					Msg.msg(s, "&8&m-- &r&7Placeholders &8&m--");
					for (String placeholder : ex.getPlaceholders()) {
						Msg.msg(s, placeholder);
					}
				}
				
				return true;
			} else if (args[0].equalsIgnoreCase("parse")) {

				if (!(s instanceof Player)) {
					Msg.msg(s, "&cThis command can only be used in game!");
					return true;
				} else {
					if (!s.hasPermission("placeholderapi.parse")) {
						Msg.msg(s, "&cYou don't have permission to do that!");
						return true;
					}
				}
				
				Player p = (Player) s;
				
				String parse = StringUtils.join(args, " ", 1, args.length);
				
				Msg.msg(s, "&r" + PlaceholderAPI.setPlaceholders(p, parse));
				
				return true;
			} else if (args[0].equalsIgnoreCase("reload")) {

				if (s instanceof Player) {
					if (!s.hasPermission("placeholderapi.reload")) {
						Msg.msg(s, "&cYou don't have permission to do that!");
						return true;
					}
				}
				
				Msg.msg(s, "&cClips &fPlaceholder&7API &bconfiguration reloaded!");
				
				plugin.reloadConf(s);

			} else if (args[0].equalsIgnoreCase("list")) {
				
				if (s instanceof Player) {
					if (!s.hasPermission("placeholderapi.list")) {
						Msg.msg(s, "&cYou don't have permission to do that!");
						return true;
					}
				}
				
				Set<String> registered = PlaceholderAPI.getRegisteredIdentifiers();
				
				if (registered.isEmpty()) {
					Msg.msg(s, "&7There are no placeholder hooks currently registered!");
					return true;
				}
				Msg.msg(s, registered.size()+" &7Placeholder hooks registered:");
				Msg.msg(s, registered.toString());
			} else {
				Msg.msg(s, "&cIncorrect usage! &7/papi help");
			}
		}
		
		return true;
	}

}
