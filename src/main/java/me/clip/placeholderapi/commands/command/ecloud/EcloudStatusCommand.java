package me.clip.placeholderapi.commands.command.ecloud;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;

public final class EcloudStatusCommand extends Command {
    public EcloudStatusCommand() {
        super("ecloud status");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        PlaceholderAPIPlugin plugin = PlaceholderAPIPlugin.getInstance();
        Msg.msg(sender, "&bThere are &f" + plugin.getExpansionCloud().getCloudExpansions().size()
                        + " &bexpansions available on the cloud.",
                "&7A total of &f" + plugin.getExpansionCloud().getCloudAuthorCount()
                        + " &7authors have contributed to the expansion cloud.");
        if (plugin.getExpansionCloud().getToUpdateCount() > 0) {
            Msg.msg(sender, "&eYou have &f" + plugin.getExpansionCloud().getToUpdateCount()
                    + " &eexpansions installed that have updates available.");
        }
    }
}
