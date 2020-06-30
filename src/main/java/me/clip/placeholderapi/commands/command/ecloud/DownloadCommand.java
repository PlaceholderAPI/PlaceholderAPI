package me.clip.placeholderapi.commands.command.ecloud;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.expansion.cloud.ExpansionCloudManager;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DownloadCommand extends Command {
    private static final int MINIMUM_ARGUMENTS = 1;

    public DownloadCommand() {
        super("ecloud download", options("&cAn expansion name must be specified!", "placeholderapi.ecloud"));
    }

    @Override
    public boolean execute(@NotNull final CommandSender sender, @NotNull final String[] args) {
        if (args.length < MINIMUM_ARGUMENTS) {
            return false;
        }

        final PlaceholderAPIPlugin plugin = PlaceholderAPIPlugin.getInstance();
        final String input = args[1];
        final CloudExpansion expansion = plugin.getExpansionCloud().getCloudExpansion(input);

        if (expansion == null) {
            Msg.msg(sender, "&cNo expansion found with the name: &f" + input);
            return true;
        }

        final PlaceholderExpansion loaded = plugin.getExpansionManager().getRegisteredExpansion(input);
        if (loaded != null && loaded.isRegistered()) {
            PlaceholderAPI.unregisterPlaceholderHook(loaded.getIdentifier());
        }

        String version = expansion.getLatestVersion();

        if (args.length == 3) {
            version = args[2];
            if (expansion.getVersion(version) == null) {
                Msg.msg(sender, "&cThe version you specified does not exist for &f" + expansion.getName());
                Msg.msg(sender, "&7Available versions: &f" + expansion.getVersions().size());
                Msg.msg(sender, String.join("&a, &f", expansion.getAvailableVersions()));

                return true;
            }
        }

        Msg.msg(sender, "&aDownload starting for expansion: &f" + expansion.getName() + " &aversion: &f" + version);
        final String player = ((sender instanceof Player) ? sender.getName() : null);
        final ExpansionCloudManager cloud = plugin.getExpansionCloud();
        cloud.downloadExpansion(player, expansion, version);
        cloud.clean();
        cloud.fetch(plugin.getPlaceholderAPIConfig().cloudAllowUnverifiedExpansions());

        return true;
    }
}
