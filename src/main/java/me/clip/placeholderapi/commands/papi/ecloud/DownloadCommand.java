package me.clip.placeholderapi.commands.papi.ecloud;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.clip.placeholderapi.util.Msg.msg;

public class DownloadCommand extends Command {

    @NotNull
    private final PlaceholderAPIPlugin plugin;

    public DownloadCommand(@NotNull final PlaceholderAPIPlugin plugin) {
        super("ecloud download");
        options.permissions("placeholderapi.ecloud");

        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, String[] args) {
        if (args.length < 3) {
            msg(sender, "&cAn expansion name must be specified!");
            return true;
        }

        CloudExpansion expansion = plugin.getExpansionCloud().getCloudExpansion(args[2]);
        if (expansion == null) {
            msg(sender, "&cNo expansion found with the name: &f" + args[2]);
            return true;
        }

        PlaceholderExpansion loaded = plugin.getExpansionManager().getRegisteredExpansion(args[2]);
        if (loaded != null && loaded.isRegistered()) {
            PlaceholderAPI.unregisterPlaceholderHook(loaded.getIdentifier());
        }

        String version = expansion.getLatestVersion();

        if (args.length == 4) {
            version = args[3];
            if (expansion.getVersion(version) == null) {
                msg(sender, "&cThe version you specified does not exist for &f" + expansion.getName());
                msg(sender, "&7Available versions: &f" + expansion.getVersions().size());
                msg(sender, String.join("&a, &f", expansion.getAvailableVersions()));

                return true;
            }
        }

        msg(sender, "&aDownload starting for expansion: &f" + expansion.getName() + " &aversion: &f" + version);
        String player = ((sender instanceof Player) ? sender.getName() : null);
        plugin.getExpansionCloud().downloadExpansion(player, expansion, version);
        plugin.getExpansionCloud().clean();
        plugin.getExpansionCloud().fetch(plugin.getPlaceholderAPIConfig().cloudAllowUnverifiedExpansions());

        return true;
    }
}
