package me.clip.placeholderapi.commands.papi;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class EnableEcloudCommand extends Command {

    @NotNull
    private final PlaceholderAPIPlugin plugin;

    public EnableEcloudCommand(@NotNull final PlaceholderAPIPlugin plugin) {
        super("enablecloud", 0);
        options.permissions("placeholderapi.ecloud");

        this.plugin = plugin;
    }

    @Override
    public boolean execute(final @NotNull CommandSender sender, final String[] args) {
        if (plugin.getExpansionCloud() != null) {
            Msg.msg(sender, "&7The cloud is already enabled!");

            return true;
        }

        plugin.enableCloud();
        plugin.getPlaceholderAPIConfig().setCloudEnabled(true);
        Msg.msg(sender, "&aThe cloud has been enabled!");

        return true;
    }

}
