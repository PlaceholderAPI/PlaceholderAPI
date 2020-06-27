package me.clip.placeholderapi.commands.command.ecloud;

import me.clip.placeholderapi.PlaceholderAPI;
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

public class PlaceholdersCommand extends Command {

    @NotNull
    private final PlaceholderAPIPlugin plugin;

    public PlaceholdersCommand(@NotNull final PlaceholderAPIPlugin plugin) {
        super("ecloud placeholders", 2, 1);

        permissions().add("placeholderapi.ecloud");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (handleUsage(sender, args)) return true;

        final String input = args[2];
        final CloudExpansion expansion = plugin.getExpansionCloud().getCloudExpansion(input);
        if (expansion == null) {
            Msg.msg(sender, "&cNo expansion found by the name: &f" + input);

            return true;
        }

        final List<String> placeholders = expansion.getPlaceholders();
        if (placeholders == null) {
            Msg.msg(sender, "&cThe expansion: &f" + expansion.getName()
                            + " &cdoes not have any placeholders listed.",
                    "&7You should contact &f" + expansion.getAuthor() + " &7and ask for them to be added.");

            return true;
        }

        if (!(sender instanceof Player)
                || plugin.getExpansionManager().getRegisteredExpansion(expansion.getName()) == null) {
            Msg.msg(sender, "&bPlaceholders: &f" + placeholders.size(),
                    String.join("&a, &f", placeholders));

            return true;
        }

        final Player p = (Player) sender;
        final JSONMessage message = JSONMessage.create(Msg.color("&bPlaceholders: &f" + placeholders.size()));
        message.then("\n");

        for (int i = 0; i < placeholders.size(); i++) {
            if (i == placeholders.size() - 1) {
                message.then(placeholders.get(i));
            } else {
                message.then(Msg.color(placeholders.get(i) + "&b, &f"));
            }
            try {
                message.tooltip(PlaceholderAPI.setPlaceholders(p, placeholders.get(i)));
            } catch (Exception e) {
                // Why you catching pokemon, and then ignoring them :C
            }
        }

        message.send(p);
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
                    "expansions.."
            ));

            return StringUtil.copyPartialMatches(args[required - 1], completions, new ArrayList<>(completions.size()));
        }

        return Collections.emptyList();
    }
}
