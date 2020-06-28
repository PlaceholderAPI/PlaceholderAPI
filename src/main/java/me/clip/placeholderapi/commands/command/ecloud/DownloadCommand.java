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

    public DownloadCommand() {
        super("ecloud download", 2, 1);

        permissions().add("placeholderapi.ecloud");
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (handleUsage(sender, args)) return;

        final PlaceholderAPIPlugin plugin = PlaceholderAPIPlugin.getInstance();
        final String input = args[2];
        final CloudExpansion expansion = plugin.getExpansionCloud().getCloudExpansion(input);
        if (expansion == null) {
            Msg.msg(sender, "&cNo expansion found with the name: &f" + input);
            return;
        }

        final PlaceholderExpansion loaded = plugin.getExpansionManager().getRegisteredExpansion(input);
        if (loaded != null && loaded.isRegistered()) {
            PlaceholderAPI.unregisterPlaceholderHook(loaded.getIdentifier());
        }

        String version = expansion.getLatestVersion();

        if (args.length - super.getCommandLength() == 2) {
            version = args[3];
            if (expansion.getVersion(version) == null) {
                Msg.msg(sender, "&cThe version you specified does not exist for &f" + expansion.getName());
                Msg.msg(sender, "&7Available versions: &f" + expansion.getVersions().size());
                Msg.msg(sender, String.join("&a, &f", expansion.getAvailableVersions()));

                return;
            }
        }

        Msg.msg(sender, "&aDownload starting for expansion: &f" + expansion.getName() + " &aversion: &f" + version);
        final String player = ((sender instanceof Player) ? sender.getName() : null);
        final ExpansionCloudManager cloud = plugin.getExpansionCloud();
        cloud.downloadExpansion(player, expansion, version);
        cloud.clean();
        cloud.fetch(plugin.getPlaceholderAPIConfig().cloudAllowUnverifiedExpansions());
    }

    @Override
    public boolean handleUsage(@NotNull CommandSender sender, @NotNull String[] args) {
        final int given = args.length - super.getCommandLength();

        if (given < super.getMinArguments()) {
            Msg.msg(sender, "&cAn expansion name must be specified!");
            return true;
        }

        return false;
    }
}
