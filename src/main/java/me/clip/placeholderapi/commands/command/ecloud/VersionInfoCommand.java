package me.clip.placeholderapi.commands.command.ecloud;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.util.Msg;
import me.rayzr522.jsonmessage.JSONMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VersionInfoCommand extends Command {

    public VersionInfoCommand() {
        super("ecloud versioninfo", 2, 2);

        permissions().add("placeholderapi.ecloud");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (handleUsage(sender, args)) return true;

        final String input = args[2];
        final CloudExpansion expansion = PlaceholderAPIPlugin.getInstance().getExpansionCloud().getCloudExpansion(input);
        if (expansion == null) {
            Msg.msg(sender, "&cNo expansion found by the name: &f" + input);
            return true;
        }

        final CloudExpansion.Version version = expansion.getVersion(args[3]);
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

    @Override
    public boolean handleUsage(@NotNull CommandSender sender, @NotNull String[] args) {
        final int given = args.length - super.getLength();

        if (given < super.getMin()) {
            Msg.msg(sender, "&cAn expansion name and version must be specified!");
            return true;
        }

        return false;
    }

    @Override
    public List<String> handleCompletion(@NotNull CommandSender sender, @NotNull String[] args) {
        final int required = super.getMin() + super.getLength();

        if (args.length == required - 1) {
            final List<String> completions = new ArrayList<>(Arrays.asList(
                    "expansions.."
            ));

            return StringUtil.copyPartialMatches(args[required - 2], completions, new ArrayList<>(completions.size()));
        }
        if (args.length == required) {
            return Arrays.asList("Expansion Version");
        }

        return Collections.emptyList();
    }
}
