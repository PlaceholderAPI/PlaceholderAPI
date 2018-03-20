package me.clip.placeholderapi.expansion;

import org.bukkit.entity.Player;

public interface Relational {
	String onPlaceholderRequest(Player one, Player two, String identifier);
}
