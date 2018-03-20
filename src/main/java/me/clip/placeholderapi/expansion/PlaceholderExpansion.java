package me.clip.placeholderapi.expansion;

import java.util.List;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.PlaceholderHook;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public abstract class PlaceholderExpansion extends PlaceholderHook {
	
	/**
	 * The name of this expansion
	 * @return identifier used for expansion if no name present
	 */
	public String getName() {
		return getIdentifier();
	}
	/**
	 * Get the identifier that this placeholder expansion uses to be passed placeholder requests
	 * @return placeholder identifier that is associated with this class
	 */
	public abstract String getIdentifier();
	
	/**
	 * Get the plugin that this expansion hooks into. 
	 * This will ensure the expansion is added to a cache if canRegister() returns false
	 * get. 
	 * The expansion will be removed from the cache
	 * once a plugin loads with the name that is here and the expansion is registered
	 * @return placeholder identifier that is associated with this class
	 */
	public abstract String getPlugin();
	
	/**
	 * Get the author of this PlaceholderExpansion
	 * @return name of the author for this expansion
	 */
	public abstract String getAuthor();
	
	/**
	 * Get the version of this PlaceholderExpansion
	 * @return current version of this expansion
	 */
	public abstract String getVersion();
	
	/**
	 * Check if a placeholder has already been registered with this identifier
	 * @return true if the identifier for this expansion has already been registered
	 */
	public boolean isRegistered() {
		Validate.notNull(getIdentifier(), "Placeholder identifier can not be null!");
		return PlaceholderAPI.getRegisteredIdentifiers().contains(getIdentifier());
	}
	
	/**
	 * If any requirements are required to be checked before this hook can register, add them here
	 * @return true if this hook meets all the requirements to register
	 */
	public boolean canRegister() {
		return getPlugin() == null || Bukkit.getPluginManager().getPlugin(getPlugin()) != null;
	}
	
	/**
	 * Attempt to register this PlaceholderExpansion with PlaceholderAPI
	 * @return true if this class and identifier have been successfully registered with PlaceholderAPI
	 */
	public boolean register() {
		Validate.notNull(getIdentifier(), "Placeholder identifier can not be null!");
		return PlaceholderAPI.registerPlaceholderHook(getIdentifier(), this);
	}
	
	
	
	/**
	 * Quick getter for the {@link PlaceholderAPIPlugin} instance
	 * @return {@link PlaceholderAPIPlugin} instance
	 */
	public PlaceholderAPIPlugin getPlaceholderAPI() {
		return PlaceholderAPIPlugin.getInstance();
	}
	
	/**
	 * A short description of this expansion
	 * @return null if no description
	 */
	public String getDescription() {
		return null;
	}
	
	/**
	 * The url link to this expansion page
	 * @return null if no link
	 */
	public String getLink() {
		return null;
	}
	
	/**
	 * A list of all valid placeholders
	 * @return null if you dont care
	 */
	public List<String> getPlaceholders() {
		return null;
	}
	
	public String getString(String path, String def) {
		return getPlaceholderAPI().getConfig().getString("expansions." + getIdentifier() + "." + path, def);
	}
	
	public int getInt(String path, int def) {
		return getPlaceholderAPI().getConfig().getInt("expansions." + getIdentifier() + "." + path, def);
	}
	
	public long getLong(String path, long def) {
		return getPlaceholderAPI().getConfig().getLong("expansions." + getIdentifier() + "." + path, def);
	}
	
	public double getDouble(String path, double def) {
		return getPlaceholderAPI().getConfig().getDouble("expansions." + getIdentifier() + "." + path, def);
	}	
	
	public List<String> getStringList(String path) {
		return getPlaceholderAPI().getConfig().getStringList("expansions." + getIdentifier() + "." + path);
	}
	
	public Object get(String path, Object def) {
		return getPlaceholderAPI().getConfig().get("expansions." + getIdentifier() + "." + path, def);
	}
	
	public ConfigurationSection getConfigSection(String path) {
		return getPlaceholderAPI().getConfig().getConfigurationSection("expansions." + getIdentifier() + "." + path); 
	}
	
	public ConfigurationSection getConfigSection() {
		return getPlaceholderAPI().getConfig().getConfigurationSection("expansions." + getIdentifier()); 
	}
	
	public boolean configurationContains(String path) {
		return getPlaceholderAPI().getConfig().contains("expansions." + getIdentifier() + "." + path); 
	}
}