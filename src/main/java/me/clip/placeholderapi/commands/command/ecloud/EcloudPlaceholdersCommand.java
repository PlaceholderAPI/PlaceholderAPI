package me.clip.placeholderapi.commands.command.ecloud;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.util.Msg;
import me.rayzr522.jsonmessage.JSONMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class EcloudPlaceholdersCommand extends Command {
    public EcloudPlaceholdersCommand() {
        super("ecloud placeholders", options("&cAn expansion name must be specified!", 1, "placeholderapi.ecloud"));
    }

    @Override
    public void execute(@NotNull final CommandSender sender, @NotNull final String[] args) {
        final PlaceholderAPIPlugin plugin = PlaceholderAPIPlugin.getInstance();
        final String input = args[0];
        final CloudExpansion expansion = plugin.getExpansionCloud().getCloudExpansion(input);
        if (expansion == null) {
            Msg.msg(sender, "&cNo expansion found by the name: &f" + input);

            return;
        }

        final List<String> placeholders = expansion.getPlaceholders();
        if (placeholders == null) {
            Msg.msg(sender, "&cThe expansion: &f" + expansion.getName()
                            + " &cdoes not have any placeholders listed.",
                    "&7You should contact &f" + expansion.getAuthor() + " &7and ask for them to be added.");

            return;
        }

        if (!(sender instanceof Player)
                || plugin.getExpansionManager().getRegisteredExpansion(expansion.getName()) == null) {
            Msg.msg(sender, "&bPlaceholders: &f" + placeholders.size(),
                    String.join("&a, &f", placeholders));

            return;
        }

        final Player p = (Player) sender;
        final JSONMessage message = JSONMessage.create(Msg.color("&bPlaceholders: &f" + placeholders.size()));
        message.then("\n");

        for (int i = 0; i < placeholders.size(); i++) {
            message.then(i == placeholders.size() - 1 ? placeholders.get(i) : Msg.color(placeholders.get(i) + "&b, &f"));
            try {
                message.tooltip(PlaceholderAPI.setPlaceholders(p, placeholders.get(i)));
            } catch (final Exception ignored) {
                // Ignored exception
            }
        }

        message.send(p);
    }
}
