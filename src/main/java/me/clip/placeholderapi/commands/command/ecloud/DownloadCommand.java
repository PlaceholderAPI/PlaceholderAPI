package me.clip.placeholderapi.commands.command.ecloud;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DownloadCommand extends Command {

    @NotNull
    private final PlaceholderAPIPlugin plugin;

    public DownloadCommand(@NotNull final PlaceholderAPIPlugin plugin) {
        super("ecloud download", 2, 1);

        permissions().add("placeholderapi.ecloud");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (handleUsage(sender, args)) return true;

        final String input = args[2];
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

        if (args.length - super.getLength() == 2) {
            version = args[3];
            if (expansion.getVersion(version) == null) {
                Msg.msg(sender, "&cThe version you specified does not exist for &f" + expansion.getName());
                Msg.msg(sender, "&7Available versions: &f" + expansion.getVersions().size());
                Msg.msg(sender, String.join("&a, &f", expansion.getAvailableVersions()));

                return true;
            }
        }

        Msg.msg(sender, "&aDownload starting for expansion: &f" + expansion.getName() + " &aversion: &f" + version);
        String player = ((sender instanceof Player) ? sender.getName() : null);
        plugin.getExpansionCloud().downloadExpansion(player, expansion, version);
        plugin.getExpansionCloud().clean();
        plugin.getExpansionCloud().fetch(plugin.getPlaceholderAPIConfig().cloudAllowUnverifiedExpansions());

        return true;
    }

    @Override
    public boolean handleUsage(@NotNull CommandSender sender, @NotNull String[] args) {
        final int given = args.length - super.getLength();

        if (given < super.getMin()) {
            Msg.msg(sender, "&cAn expansion name must be specified!");
            return true;
        }

        return false;
    }

    @Override
    public List<String> handleCompletion(@NotNull CommandSender sender, @NotNull String[] args) {
        final int required = super.getMin() + super.getLength();
        if (args.length == required) {
            final List<String> completions = new ArrayList<>(Arrays.asList(
                    "player",
                    "vault"
            ));

            return StringUtil.copyPartialMatches(args[required - 1], completions, new ArrayList<>(completions.size()));
        }

        return Collections.emptyList();
    }
}
