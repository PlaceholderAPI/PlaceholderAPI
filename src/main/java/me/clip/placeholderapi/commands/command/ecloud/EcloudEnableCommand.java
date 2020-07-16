package me.clip.placeholderapi.commands.command.ecloud;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;

public final class EnableCloudCommand extends Command {
    public EnableCloudCommand() {
        super("enablecloud");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        PlaceholderAPIPlugin plugin = PlaceholderAPIPlugin.getInstance();
        if (plugin.getExpansionCloud() != null) {
            Msg.msg(sender, "&7The cloud is already enabled!");

            return;
        }

        plugin.enableCloud();
        plugin.getPlaceholderAPIConfig().setCloudEnabled(true);
        Msg.msg(sender, "&aThe cloud has been enabled!");
    }
}
