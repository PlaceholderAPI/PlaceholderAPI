package me.clip.placeholderapi.commands.command.ecloud;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.util.Msg;
import me.rayzr522.jsonmessage.JSONMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VersionInfoCommand extends Command {
    private static final int MINIMUM_ARGUMENTS = 2;

    public VersionInfoCommand() {
        super("ecloud versioninfo", options("placeholderapi.ecloud"));
    }

    @Override
    public boolean execute(@NotNull final CommandSender sender, @NotNull final String[] args) {
        if (args.length < MINIMUM_ARGUMENTS) {
            return false;
        }

        final String input = args[0];
        final CloudExpansion expansion = PlaceholderAPIPlugin.getInstance().getExpansionCloud().getCloudExpansion(input);
        if (expansion == null) {
            Msg.msg(sender, "&cNo expansion found by the name: &f" + input);
            return true;
        }

        final CloudExpansion.Version version = expansion.getVersion(args[1]);
        if (version == null) {
            Msg.msg(sender, "&cThe version specified does not exist for expansion: &f" + expansion.getName());
            return true;
        }

        Msg.msg(sender, "&bExpansion: " + (expansion.shouldUpdate() ? "&e" : "&f") + expansion.getName(),
                "&bVersion: &f" + version.getVersion(),
                "&bVersion info: &f" + version.getReleaseNotes());

        if (!(sender instanceof Player)) {
            Msg.msg(sender, "&bDownload url: " + version.getUrl());
            return true;
        }

        final Player p = (Player) sender;
        final JSONMessage download = JSONMessage.create(Msg.color("&7Click to download this version"));
        download.suggestCommand(
                "/papi ecloud download " + expansion.getName() + " " + version.getVersion());
        download.send(p);

        return true;
    }
}
