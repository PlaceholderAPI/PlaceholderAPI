package me.clip.placeholderapi.commands.papi.ecloud;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static me.clip.placeholderapi.util.Msg.msg;

public class ClearCommand extends Command {

    @NotNull
    private final PlaceholderAPIPlugin plugin;

    public ClearCommand(@NotNull final PlaceholderAPIPlugin plugin) {
        super("ecloud clear", 0);
        options.permissions("placeholderapi.ecloud");

        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, String[] args) {
        msg(sender, "&aThe cache has been cleared!!");
        plugin.getExpansionCloud().clean();

        return true;
    }
}
