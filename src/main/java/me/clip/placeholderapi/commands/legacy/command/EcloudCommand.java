package me.clip.placeholderapi.commands.legacy.command;

import com.google.common.collect.Sets;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.legacy.Command;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class EcloudCommand extends Command {
    private static final int MAXIMUM_ARGUMENTS = 1;
    private static final Set<String> COMPLETIONS = Sets.newHashSet(
            "clear",
            "download",
            "info",
            "list",
            "placeholders",
            "refresh",
            "status",
            "versioninfo"
    );

    public EcloudCommand() {
        super("ecloud", permissions("placeholderapi.ecloud"));
    }

    @Override
    public void execute(@NotNull final CommandSender sender, @NotNull final String[] args) {
        final PlaceholderAPIPlugin plugin = PlaceholderAPIPlugin.getInstance();

        if (args.length == 0) {
            Msg.msg(sender, "&bExpansion cloud commands",
                    " ",
                    "&b/papi ecloud status",
                    "&fView status of the ecloud",
                    "&b/papi ecloud list <all/author> (page)",
                    "&fList all/author specific available expansions",
                    "&b/papi ecloud info <expansion name>",
                    "&fView information about a specific expansion available on the cloud",
                    "&b/papi ecloud versioninfo <expansion name> <version>",
                    "&fView information about a specific version of an expansion",
                    "&b/papi ecloud placeholders <expansion name>",
                    "&fView placeholders for an expansion",
                    "&b/papi ecloud download <expansion name> (version)",
                    "&fDownload an expansion from the ecloud",
                    "&b/papi ecloud refresh",
                    "&fFetch the most up to date list of expansions available.",
                    "&b/papi ecloud clear",
                    "&fClear the expansion cloud cache.");
            return;
        }

        if (plugin.getExpansionCloud() == null) {
            Msg.msg(sender, "&7The expansion cloud is not enabled!");

            return;
        }

        if (plugin.getExpansionCloud().getCloudExpansions().isEmpty()) {
            Msg.msg(sender, "&7No cloud expansions are available at this time.");
            return;
        }

        sender.sendMessage("Specified command is not valid.");
    }

    @NotNull
    @Override
    public List<String> handleCompletion(@NotNull final CommandSender sender, @NotNull final String[] args) {
        if (args.length == MAXIMUM_ARGUMENTS) {
            return StringUtil.copyPartialMatches(args[0], COMPLETIONS, new ArrayList<>(COMPLETIONS.size()));
        }

        return super.handleCompletion(sender, args);
    }
}
