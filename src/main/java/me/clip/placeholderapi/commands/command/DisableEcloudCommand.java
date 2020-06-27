package me.clip.placeholderapi.commands.command;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class DisableEcloudCommand extends Command {

    @NotNull
    private final PlaceholderAPIPlugin plugin;

    public DisableEcloudCommand(@NotNull final PlaceholderAPIPlugin plugin) {
        super("disablecloud", 1, 0);

        permissions().add("placeholderapi.ecloud");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (plugin.getExpansionCloud() == null) {
            Msg.msg(sender, "&7The cloud is already disabled!");

            return true;
        }

        plugin.disableCloud();
        plugin.getPlaceholderAPIConfig().setCloudEnabled(false);
        Msg.msg(sender, "&aThe cloud has been disabled!");

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
