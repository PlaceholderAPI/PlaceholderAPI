package me.clip.placeholderapi.commands.papi;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DisableEcloudCommand extends Command {

    @NotNull
    private final PlaceholderAPIPlugin plugin;

    public DisableEcloudCommand(@NotNull final PlaceholderAPIPlugin plugin) {
        super("disablecloud", 0);
        options.permissions("placeholderapi.ecloud");

        this.plugin = plugin;
    }

    @Override
    public boolean execute(final @NotNull CommandSender sender, final String[] args) {
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
