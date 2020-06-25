package me.clip.placeholderapi.commands.papi;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class RegisterCommand extends Command {

    @NotNull
    private final PlaceholderAPIPlugin plugin;

    public RegisterCommand(@NotNull final PlaceholderAPIPlugin plugin) {
        super("register", 1);
        options.permissions("placeholderapi.register");

        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, String[] args) {
        String fileName = args[0].replace(".jar", "");
        PlaceholderExpansion ex = plugin.getExpansionManager().registerExpansion(fileName);

        if (ex == null) {
            Msg.msg(sender, "&cFailed to register expansion from " + fileName);

            return true;
        }

        Msg.msg(sender, "&aSuccessfully registered expansion: &f" + ex.getName());
        return true;
    }
}
