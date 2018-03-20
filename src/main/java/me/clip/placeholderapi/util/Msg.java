package me.clip.placeholderapi.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Msg {

	public static void msg(CommandSender s, String msg) {
		s.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}
	
}
