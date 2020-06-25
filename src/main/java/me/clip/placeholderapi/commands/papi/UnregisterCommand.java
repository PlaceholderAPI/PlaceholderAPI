package me.clip.placeholderapi.commands.papi;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class UnregisterCommand extends Command {

    @NotNull
    private final PlaceholderAPIPlugin plugin;

    public UnregisterCommand(@NotNull final PlaceholderAPIPlugin plugin) {
        super("unregister", 1);
        options.permissions("placeholderapi.register");

        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, String[] args) {
        PlaceholderExpansion ex = plugin.getExpansionManager().getRegisteredExpansion(args[0]);
        if (ex == null) {
            Msg.msg(sender, "&cFailed to find expansion: &f" + args[0]);

            return true;
        }

        if (PlaceholderAPI.unregisterExpansion(ex)) {
            Msg.msg(sender, "&aSuccessfully unregistered expansion: &f" + ex.getName());
        } else {
            Msg.msg(sender, "&cFailed to unregister expansion: &f" + ex.getName());
        }
        return true;
    }
}
