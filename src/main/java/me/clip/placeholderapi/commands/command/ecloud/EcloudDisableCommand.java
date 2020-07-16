package me.clip.placeholderapi.commands.command.ecloud;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;

public final class DisableEcloudCommand extends Command {
    public DisableEcloudCommand() {
        super("disablecloud");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        PlaceholderAPIPlugin plugin = PlaceholderAPIPlugin.getInstance();
        if (plugin.getExpansionCloud() == null) {
            Msg.msg(sender, "&7The cloud is already disabled!");
            return;
        }

        plugin.disableCloud();
        plugin.getPlaceholderAPIConfig().setCloudEnabled(false);
        Msg.msg(sender, "&aThe cloud has been disabled!");
    }
}
