package me.clip.placeholderapi.commands.command.ecloud;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.expansion.cloud.ExpansionCloudManager;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;

public final class EcloudRefreshCommand extends Command {
    public EcloudRefreshCommand() {
        super("ecloud refresh");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        PlaceholderAPIPlugin plugin = PlaceholderAPIPlugin.getInstance();
        ExpansionCloudManager cloud = plugin.getExpansionCloud();
        Msg.msg(sender, "&aRefresh task started. Use &f/papi ecloud list all &ain a few!!");
        cloud.clean();
        cloud.fetch(plugin.getPlaceholderAPIConfig().cloudAllowUnverifiedExpansions());
    }
}
