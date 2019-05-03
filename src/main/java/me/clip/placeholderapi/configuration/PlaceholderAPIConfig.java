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
package me.clip.placeholderapi.configuration;

import me.clip.placeholderapi.PlaceholderAPIPlugin;

import org.bukkit.configuration.file.FileConfiguration;

public class PlaceholderAPIConfig {

	private PlaceholderAPIPlugin plugin;
	
	public PlaceholderAPIConfig(PlaceholderAPIPlugin i) {
		plugin = i;
	}
	
	public void loadDefConfig() {
		
		FileConfiguration c = plugin.getConfig();
		
		c.options().header("PlaceholderAPI version "+plugin.getDescription().getVersion()+""
				+ "\nCreated by extended_clip"
				+ "\n"
				+ "\nNo placeholders are provided with this plugin."
				+ "\nDownload placeholders with /papi ecloud"
				+ "\nExample:"
				+ "\n/papi ecloud refresh"
				+ "\n/papi ecloud list all"
				+ "\n/papi ecloud list all 2"
				+ "\n/papi ecloud download Player"
				+ "\n/papi ecloud download Vault"
				+ "\n/papi reload");
		
		c.set("auto_install_expansions", null);
		c.set("cloud_enable_unverified_expansions", null);
		c.addDefault("check_updates", true);
		c.addDefault("cloud_enabled", true);	
		c.addDefault("cloud_allow_unverified_expansions", false);
		c.addDefault("defaults.boolean_true", "true");
		c.addDefault("defaults.boolean_false", "false");
		c.addDefault("defaults.date_format", "MM/dd/yy HH:mm:ss");
		c.options().copyDefaults(true);
		plugin.saveConfig();
		plugin.reloadConfig();
	}
	
	public boolean checkUpdates() {
		return plugin.getConfig().getBoolean("check_updates");
	}

	public boolean cloudAllowUnverifiedExpansions() {
		return plugin.getConfig().getBoolean("cloud_allow_unverified_expansions");
	}
	
	public boolean isCloudEnabled() {
		return plugin.getConfig().getBoolean("cloud_enabled");
	}
	
	public void setCloudEnabled(boolean b) {
		plugin.getConfig().set("cloud_enabled", b);
		plugin.saveConfig();
		plugin.reloadConfig();
	}
	
	public String booleanTrue() {
		return plugin.getConfig().getString("defaults.boolean_true");
	}
	
	public String booleanFalse() {
		return plugin.getConfig().getString("defaults.boolean_false");
	}
	
	public String dateFormat() {
		return plugin.getConfig().getString("defaults.date_format");
	}	
}
