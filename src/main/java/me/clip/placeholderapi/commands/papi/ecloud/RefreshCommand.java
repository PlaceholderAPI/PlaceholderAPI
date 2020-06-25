package me.clip.placeholderapi.commands.papi.ecloud;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static me.clip.placeholderapi.util.Msg.msg;

public class RefreshCommand extends Command {

    @NotNull
    private final PlaceholderAPIPlugin plugin;

    public RefreshCommand(@NotNull final PlaceholderAPIPlugin plugin) {
        super("ecloud refresh", 0);
        options.permissions("placeholderapi.ecloud");

        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, String[] args) {
        msg(sender, "&aRefresh task started. Use &f/papi ecloud list all &ain a few!!");
        plugin.getExpansionCloud().clean();
        plugin.getExpansionCloud().fetch(plugin.getPlaceholderAPIConfig().cloudAllowUnverifiedExpansions());
        return true;
    }
}
