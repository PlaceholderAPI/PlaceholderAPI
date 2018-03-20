package me.clip.placeholderapi.expansion;

import org.bukkit.entity.Player;

/**
 * This interface allows a class which extends a {@link PlaceholderExpansion}
 * to have the cleanup method called every time a player leaves the server.
 * This is useful if we want to clean up after the player
 * @author Ryan McCarthy
 *
 */
public interface Cleanable {

	/**
	 * Called when a player leaves the server
	 * @param p (@link Player} who left the server
	 */
	void cleanup(Player p);
}
