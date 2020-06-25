package me.clip.placeholderapi.commands.papi;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class VersionCommand extends Command {

    @NotNull
    private final PlaceholderAPIPlugin plugin;

    public VersionCommand(@NotNull final PlaceholderAPIPlugin plugin) {
        super("version", 0);
        options.def(true).permissions();

        this.plugin = plugin;
    }

    @Override
    public boolean execute(final @NotNull CommandSender sender, final String[] args) {
        Msg.msg(sender, "PlaceholderAPI &7version &b&o" + plugin.getDescription().getVersion(),
                "&fCreated by&7: &b" + plugin.getDescription().getAuthors(),
                "&fPapi commands: &b/papi help",
                "&fEcloud commands: &b/papi ecloud");
        return true;
    }

}
