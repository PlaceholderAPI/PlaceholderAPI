package me.clip.placeholderapi.commands.papi.ecloud;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.rayzr522.jsonmessage.JSONMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.clip.placeholderapi.util.Msg.color;
import static me.clip.placeholderapi.util.Msg.msg;

public class VersionInfoCommand extends Command {

    @NotNull
    private final PlaceholderAPIPlugin plugin;

    public VersionInfoCommand(@NotNull final PlaceholderAPIPlugin plugin) {
        super("ecloud versioninfo", 0);
        options.permissions("placeholderapi.ecloud");

        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, String[] args) {
        if (args.length < 4) {
            msg(sender, "&cAn expansion name and version must be specified!");
            return true;
        }

        CloudExpansion expansion = plugin.getExpansionCloud().getCloudExpansion(args[3]);
        if (expansion == null) {
            msg(sender, "&cNo expansion found by the name: &f" + args[3]);
            return true;
        }

        CloudExpansion.Version version = expansion.getVersion(args[4]);
        if (version == null) {
            msg(sender, "&cThe version specified does not exist for expansion: &f" + expansion.getName());
            return true;
        }

        msg(sender, "&bExpansion: " + (expansion.shouldUpdate() ? "&e" : "&f") + expansion.getName(),
                "&bVersion: &f" + version.getVersion(),
                "&bVersion info: &f" + version.getReleaseNotes());

        if (!(sender instanceof Player)) {
            msg(sender, "&bDownload url: " + version.getUrl());
            return true;
        }

        Player p = (Player) sender;

        JSONMessage download = JSONMessage.create(color("&7Click to download this version"));
        download.suggestCommand(
                "/papi ecloud download " + expansion.getName() + " " + version.getVersion());
        download.send(p);

        return true;
    }
}
