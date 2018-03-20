package me.clip.placeholderapi;

import org.bukkit.entity.Player;

public abstract class PlaceholderHook {

	/**
	 * called when a placeholder is requested from this PlaceholderHook
	 * @param p Player requesting the placeholder value for, null if not needed for a player
	 * @param params String passed for the placeholder hook to determine what value to return
	 * @return value for the requested player and params
	 */
	public abstract String onPlaceholderRequest(Player p, String params);
}
