package me.clip.placeholderapi.commands.papi.ecloud;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.rayzr522.jsonmessage.JSONMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static me.clip.placeholderapi.util.Msg.color;
import static me.clip.placeholderapi.util.Msg.msg;

public class PlaceholdersCommand extends Command {

    @NotNull
    private final PlaceholderAPIPlugin plugin;

    public PlaceholdersCommand(@NotNull final PlaceholderAPIPlugin plugin) {
        super("ecloud placeholders");
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
            msg(sender, "&cNo expansion found by the name: &f" + args[2]);

            return true;
        }

        List<String> placeholders = expansion.getPlaceholders();
        if (placeholders == null) {
            msg(sender, "&cThe expansion: &f" + expansion.getName()
                            + " &cdoes not have any placeholders listed.",
                    "&7You should contact &f" + expansion.getAuthor() + " &7and ask for them to be added.");

            return true;
        }

        if (!(sender instanceof Player)
                || plugin.getExpansionManager().getRegisteredExpansion(expansion.getName()) == null) {
            msg(sender, "&bPlaceholders: &f" + placeholders.size(),
                    String.join("&a, &f", placeholders));

            return true;
        }

        Player p = (Player) sender;
        JSONMessage message = JSONMessage.create(color("&bPlaceholders: &f" + placeholders.size()));
        message.then("\n");

        for (int i = 0; i < placeholders.size(); i++) {
            if (i == placeholders.size() - 1) {
                message.then(placeholders.get(i));
            } else {
                message.then(color(placeholders.get(i) + "&b, &f"));
            }
            try {
                message.tooltip(PlaceholderAPI.setPlaceholders(p, placeholders.get(i)));
            } catch (Exception e) {

            }
        }

        message.send(p);

        return true;
    }
}
