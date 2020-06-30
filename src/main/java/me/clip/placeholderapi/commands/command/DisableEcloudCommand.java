package me.clip.placeholderapi.commands.command;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DisableEcloudCommand extends Command {
    public DisableEcloudCommand() {
        super("disablecloud", permissions("placeholderapi.ecloud"));
    }

    @Override
    public boolean execute(@NotNull final CommandSender sender, @NotNull final String[] args) {
        final PlaceholderAPIPlugin plugin = PlaceholderAPIPlugin.getInstance();
        if (plugin.getExpansionCloud() == null) {
            Msg.msg(sender, "&7The cloud is already disabled!");

            return true;
        }

        plugin.disableCloud();
        plugin.getPlaceholderAPIConfig().setCloudEnabled(false);
        Msg.msg(sender, "&aThe cloud has been disabled!");

        return true;
    }
}
