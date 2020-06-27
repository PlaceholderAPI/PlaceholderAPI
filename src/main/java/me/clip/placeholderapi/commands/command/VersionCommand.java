package me.clip.placeholderapi.commands.command;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VersionCommand extends Command {


    public VersionCommand() {
        super("", 0, 0);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        final PlaceholderAPIPlugin plugin = PlaceholderAPIPlugin.getInstance();

        Msg.msg(sender, "PlaceholderAPI &7version &b&o" + plugin.getDescription().getVersion(),
                "&fCreated by&7: &b" + plugin.getDescription().getAuthors(),
                "&fPapi commands: &b/papi help",
                "&fEcloud commands: &b/papi ecloud");
        return true;
    }

    @Override
    public boolean handleUsage(@NotNull CommandSender sender, @NotNull String[] args) {
        return false;
    }

    @Override
    public List<String> handleCompletion(@NotNull CommandSender sender, @NotNull String[] args) {
        final List<String> completions = new ArrayList<>(Arrays.asList(
                "unregister",
                "reload",
                "register",
                "parserel",
                "parse",
                "list",
                "info",
                "help",
                "ecloud",
                "enablecloud",
                "disablecloud",
                "bcparse")
        );

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], completions, new ArrayList<>(completions.size()));
        }

        return Collections.emptyList();
    }
}
