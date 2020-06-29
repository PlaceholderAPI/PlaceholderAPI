package me.clip.placeholderapi.commands.command;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class InfoCommand extends Command {

    public InfoCommand() {
        super("info", 1, 1);

        permissions("placeholderapi.info");
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (handleUsage(sender, args)) return;

        final String input = args[1];
        final PlaceholderExpansion ex = PlaceholderAPIPlugin.getInstance().getExpansionManager().getRegisteredExpansion(input);
        if (ex == null) {
            Msg.msg(sender, "&cThere is no expansion loaded with the identifier: &f" + input);

            return;
        }

        Msg.msg(sender, "&7Placeholder expansion info for: &f" + ex.getName());
        Msg.msg(sender, "&7Status: " + (ex.isRegistered() ? "&aRegistered" : "&cNot registered"));

        if (ex.getAuthor() != null) {
            Msg.msg(sender, "&7Created by: &f" + ex.getAuthor());
        }

        if (ex.getVersion() != null) {
            Msg.msg(sender, "&7Version: &f" + ex.getVersion());
        }

        if (ex.getRequiredPlugin() != null) {
            Msg.msg(sender, "&7Requires plugin: &f" + ex.getRequiredPlugin());
        }

        if (ex.getPlaceholders() != null) {
            Msg.msg(sender, "&8&m-- &r&7Placeholders &8&m--");

            for (String placeholder : ex.getPlaceholders()) {
                Msg.msg(sender, placeholder);
            }
        }
    }

    @Override
    public boolean handleUsage(@NotNull CommandSender sender, @NotNull String[] args) {
        final int given = args.length - super.getCommandLength();

        if (given < super.getMinArguments()) {
            Msg.msg(sender, "&cIncorrect usage! &7/papi info <expansion>");
            return true;
        }

        return false;
    }

    @Override
    public @NotNull List<String> handleCompletion(@NotNull CommandSender sender, @NotNull String[] args) {
        final int required = super.getMinArguments() + super.getCommandLength();

        if (args.length == required) {
            final Set<String> completions = PlaceholderAPI.getRegisteredIdentifiers();

            return StringUtil.copyPartialMatches(args[required - 1], completions, new ArrayList<>(completions.size()));
        }

        return Collections.emptyList();
    }
}
