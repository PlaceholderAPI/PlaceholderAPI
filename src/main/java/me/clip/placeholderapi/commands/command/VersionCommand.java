package me.clip.placeholderapi.commands.command;

import com.google.common.collect.Sets;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VersionCommand extends Command {
    private static final Set<String> COMPLETIONS = Sets.newHashSet(
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
            "bcparse"
    );

    public VersionCommand() {
        super("");
    }

    @Override
    public boolean execute(@NotNull final CommandSender sender, @NotNull final String[] args) {
        final PluginDescriptionFile description = PlaceholderAPIPlugin.getInstance().getDescription();

        Msg.msg(sender, "PlaceholderAPI &7version &b&o" + description.getVersion(),
                "&fCreated by&7: &b" + description.getAuthors(),
                "&fPapi commands: &b/papi help",
                "&fEcloud commands: &b/papi ecloud");

        return true;
    }

    @NotNull
    @Override
    public List<String> handleCompletion(@NotNull final CommandSender sender, @NotNull final String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], COMPLETIONS, new ArrayList<>(COMPLETIONS.size()));
        }

        return super.handleCompletion(sender, args);
    }
}