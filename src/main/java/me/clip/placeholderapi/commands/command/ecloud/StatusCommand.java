package me.clip.placeholderapi.commands.command.ecloud;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class StatusCommand extends Command {

    @NotNull
    private final PlaceholderAPIPlugin plugin;

    public StatusCommand(@NotNull final PlaceholderAPIPlugin plugin) {
        super("ecloud status", 2, 0);

        permissions().add("placeholderapi.ecloud");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        Msg.msg(sender, "&bThere are &f" + plugin.getExpansionCloud().getCloudExpansions().size()
                        + " &bexpansions available on the cloud.",
                "&7A total of &f" + plugin.getExpansionCloud().getCloudAuthorCount()
                        + " &7authors have contributed to the expansion cloud.");
        if (plugin.getExpansionCloud().getToUpdateCount() > 0) {
            Msg.msg(sender, "&eYou have &f" + plugin.getExpansionCloud().getToUpdateCount()
                    + " &eexpansions installed that have updates available.");
        }

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
