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
		c.addDefault("boolean.true", "yes");
		c.addDefault("boolean.false", "no");
		c.addDefault("date_format", "MM/dd/yy HH:mm:ss");
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
		return plugin.getConfig().getString("boolean.true");
	}
	
	public String booleanFalse() {
		return plugin.getConfig().getString("boolean.false");
	}
	
	public String dateFormat() {
		return plugin.getConfig().getString("date_format");
	}	
}
