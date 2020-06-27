package me.clip.placeholderapi.commands.command.ecloud;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class RefreshCommand extends Command {

    public RefreshCommand() {
        super("ecloud refresh", 2, 0);

        permissions().add("placeholderapi.ecloud");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        final PlaceholderAPIPlugin plugin = PlaceholderAPIPlugin.getInstance();
        Msg.msg(sender, "&aRefresh task started. Use &f/papi ecloud list all &ain a few!!");
        plugin.getExpansionCloud().clean();
        plugin.getExpansionCloud().fetch(plugin.getPlaceholderAPIConfig().cloudAllowUnverifiedExpansions());
        return true;
    }

    @Override
    public boolean handleUsage(@NotNull CommandSender sender, @NotNull String[] args) {
        return false;
    }

    @Override
    public List<String> handleCompletion(@NotNull CommandSender sender, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
