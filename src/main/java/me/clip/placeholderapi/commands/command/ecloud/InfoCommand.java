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

import static me.clip.placeholderapi.util.Msg.color;

public class InfoCommand extends Command {

    public InfoCommand() {
        super("ecloud info", 2, 1);

        permissions().add("placeholderapi.ecloud");
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (handleUsage(sender, args)) return;

        final String input = args[2];
        final CloudExpansion expansion = PlaceholderAPIPlugin.getInstance().getExpansionCloud().getCloudExpansion(input);

        if (expansion == null) {
            Msg.msg(sender, "&cNo expansion found by the name: &f" + input);

            return;
        }

        if (!(sender instanceof Player)) {
            Msg.msg(sender,
                    (expansion.shouldUpdate() ? "&e" : "") + expansion.getName() + " &8&m-- &r" + expansion
                            .getVersion().getUrl());

            return;
        }

        final Player p = (Player) sender;

        Msg.msg(sender, "&bExpansion&7: &f" + expansion.getName(),
                "&bAuthor: &f" + expansion.getAuthor(),
                "&bVerified: &f" + expansion.isVerified()
        );

        // latest version
        final JSONMessage latestVersion = JSONMessage
                .create(color("&bLatest version: &f" + expansion.getLatestVersion()));
        latestVersion.tooltip(color("&bReleased: &f" + expansion.getTimeSinceLastUpdate()
                + "\n&bUpdate information: &f" + expansion.getVersion().getReleaseNotes()
        ));
        latestVersion.send(p);

        // versions
        final JSONMessage versions = JSONMessage
                .create(color("&bVersions available: &f" + expansion.getVersions().size()));
        versions.tooltip(color(String.join("&b, &f", expansion.getAvailableVersions())));
        versions.suggestCommand(
                "/papi ecloud versioninfo " + expansion.getName() + " " + expansion.getLatestVersion());
        versions.send(p);

        // placeholders
        if (expansion.getPlaceholders() != null) {
            final JSONMessage placeholders = JSONMessage
                    .create(color("&bPlaceholders: &f" + expansion.getPlaceholders().size()));
            placeholders.tooltip(color(String.join("&b, &f", expansion.getPlaceholders())));
            placeholders.suggestCommand("/papi ecloud placeholders " + expansion.getName());
            placeholders.send(p);
        }
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

    @Override
    public List<String> handleCompletion(@NotNull CommandSender sender, @NotNull String[] args) {
        final int required = super.getMinArguments() + super.getCommandLength();
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
