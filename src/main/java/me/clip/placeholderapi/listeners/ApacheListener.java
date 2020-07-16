package me.clip.placeholderapi.listeners;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.bukkit.Bukkit;

/**
 * The purpose of this class is to filter the console warning messages when the plugin
 * tries to load placeholder expansions from other jars in the plugins folder.
 */
public class ApacheListener extends AbstractFilter {
    private boolean cancelled = false;

    public ApacheListener(PlaceholderAPIPlugin plugin) {
        org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
        logger.addFilter(this);

        // 3 second should be more than enough. I have no idea how to unregister a filter.
        Bukkit.getScheduler().runTaskLater(plugin, () -> cancelled = true, 3 * 20L);
    }

    @Override
    public Result filter(LogEvent event) {
        if (cancelled) return Result.NEUTRAL;
        if (event.getLevel() != Level.WARN) return Result.NEUTRAL;
        if (!event.getLoggerName().equals("PlaceholderAPI")) return Result.NEUTRAL;

        // Format:
        // Loaded class {CLASS} from {PLUGIN} {VERSION} which is not a depend, softdepend or loadbefore of this plugin.
        // E.g.
        // Loaded class com.earth2me.essentials.Essentials from PlaceholderAPI v2.10.5-DEV-84 which is not a depend, softdepend or loadbefore of this plugin.
        String message = event.getMessage().getFormattedMessage();
        if (message.startsWith("Loaded class") && message.endsWith("which is not a depend, softdepend or loadbefore of this plugin.")) return Result.DENY;
        return Result.NEUTRAL;
    }
}