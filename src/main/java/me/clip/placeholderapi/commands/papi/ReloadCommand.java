package me.clip.placeholderapi.commands.papi;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends Command {

    @NotNull
    private final PlaceholderAPIPlugin plugin;

    public ReloadCommand(@NotNull final PlaceholderAPIPlugin plugin) {
        super("reload");
        options.permissions("placeholderapi.reload");

        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, String[] args) {
        Msg.msg(sender, "&fPlaceholder&7API &bconfiguration reloaded!");
        plugin.reloadConf(sender);
        return true;
    }
}
