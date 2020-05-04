package me.clip.placeholderapi.commands.papi;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static me.clip.placeholderapi.util.Msg.msg;

public class EcloudCommand extends Command {

    @NotNull
    private final PlaceholderAPIPlugin plugin;

    public EcloudCommand(@NotNull final PlaceholderAPIPlugin plugin) {
        super("ecloud");
        options.permissions("placeholderapi.ecloud");

        this.plugin = plugin;
    }

    @Override
    public boolean execute(final @NotNull CommandSender sender, final String[] args) {
        if (args.length == 1) {
            msg(sender, "&bExpansion cloud commands",
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
            return true;
        }

        if (plugin.getExpansionCloud() == null) {
            Msg.msg(sender, "&7The expansion cloud is not enabled!");

            return true;
        }

        if (plugin.getExpansionCloud().getCloudExpansions().isEmpty()) {
            msg(sender, "&7No cloud expansions are available at this time.");
        }
        return true;
    }

}
