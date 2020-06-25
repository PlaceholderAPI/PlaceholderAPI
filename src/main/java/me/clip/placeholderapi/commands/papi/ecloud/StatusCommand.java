package me.clip.placeholderapi.commands.papi.ecloud;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static me.clip.placeholderapi.util.Msg.msg;

public class StatusCommand extends Command {

    @NotNull
    private final PlaceholderAPIPlugin plugin;

    public StatusCommand(@NotNull final PlaceholderAPIPlugin plugin) {
        super("ecloud status", 0);
        options.permissions("placeholderapi.ecloud");

        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, String[] args) {
        msg(sender, "&bThere are &f" + plugin.getExpansionCloud().getCloudExpansions().size()
                        + " &bexpansions available on the cloud.",
                "&7A total of &f" + plugin.getExpansionCloud().getCloudAuthorCount()
                        + " &7authors have contributed to the expansion cloud.");
        if (plugin.getExpansionCloud().getToUpdateCount() > 0) {
            msg(sender, "&eYou have &f" + plugin.getExpansionCloud().getToUpdateCount()
                    + " &eexpansions installed that have updates available.");
        }

        return true;
    }
}
