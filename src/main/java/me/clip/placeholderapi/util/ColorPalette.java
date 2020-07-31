package me.clip.placeholderapi.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

public enum ColorPalette
{

    // MAIN
    MAIN_BLUE("#1aaec4", ChatColor.AQUA),
    MAIN_WHITE("#dae1e3", ChatColor.WHITE),
    MAIN_GRAY("#909191", ChatColor.GRAY),

    // ERRORS
    ERROR_RED("#fa4732", ChatColor.DARK_RED),
    ERROR_GRAY("#4d4d4d", ChatColor.GRAY),

    // SUCCESS
    SUCCESS_BLUE("#5aacdb", ChatColor.BLUE),
    SUCCESS_GREEN("#69d6b2", ChatColor.GREEN),

    // UTILITY
    ITALIC("&o", ChatColor.ITALIC),
    UNDERLINE("&n", ChatColor.UNDERLINE);

    private final String hex;
    private final ChatColor color;

    ColorPalette(final String hex, final ChatColor color) {
        this.hex = hex;
        this.color = color;
    }

    public ChatColor getColor() {
        if (Bukkit.getServer().getClass().getPackage().getName().contains("16")) {
            return ChatColor.of(hex);
        }
        return color;
    }


}
