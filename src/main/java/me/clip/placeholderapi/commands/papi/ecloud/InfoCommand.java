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

public class InfoCommand extends Command {

    @NotNull
    private final PlaceholderAPIPlugin plugin;

    public InfoCommand(@NotNull final PlaceholderAPIPlugin plugin) {
        super("ecloud info", 1);
        options.permissions("placeholderapi.ecloud");

        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, String[] args) {
        if (args.length < 2) {
            msg(sender, "&cAn expansion name must be specified!");

            return true;
        }

        final String input = args[1];
        CloudExpansion expansion = plugin.getExpansionCloud().getCloudExpansion(input);

        if (expansion == null) {
            msg(sender, "&cNo expansion found by the name: &f" + input);

            return true;
        }

        if (!(sender instanceof Player)) {
            msg(sender,
                    (expansion.shouldUpdate() ? "&e" : "") + expansion.getName() + " &8&m-- &r" + expansion
                            .getVersion().getUrl());

            return true;
        }

        Player p = (Player) sender;

        msg(sender, "&bExpansion&7: &f" + expansion.getName(),
                "&bAuthor: &f" + expansion.getAuthor(),
                "&bVerified: &f" + expansion.isVerified()
        );

        // latest version
        JSONMessage latestVersion = JSONMessage
                .create(color("&bLatest version: &f" + expansion.getLatestVersion()));
        latestVersion.tooltip(color("&bReleased: &f" + expansion.getTimeSinceLastUpdate()
                + "\n&bUpdate information: &f" + expansion.getVersion().getReleaseNotes()
        ));
        latestVersion.send(p);

        // versions
        JSONMessage versions = JSONMessage
                .create(color("&bVersions available: &f" + expansion.getVersions().size()));
        versions.tooltip(color(String.join("&b, &f", expansion.getAvailableVersions())));
        versions.suggestCommand(
                "/papi ecloud versioninfo " + expansion.getName() + " " + expansion.getLatestVersion());
        versions.send(p);

        // placeholders
        if (expansion.getPlaceholders() != null) {
            JSONMessage placeholders = JSONMessage
                    .create(color("&bPlaceholders: &f" + expansion.getPlaceholders().size()));
            placeholders.tooltip(color(String.join("&b, &f", expansion.getPlaceholders())));
            placeholders.suggestCommand("/papi ecloud placeholders " + expansion.getName());
            placeholders.send(p);
        }
        return true;
    }
}
