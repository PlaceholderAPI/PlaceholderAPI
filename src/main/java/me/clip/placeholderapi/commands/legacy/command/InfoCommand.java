package me.clip.placeholderapi.commands.legacy.command;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.legacy.Command;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class InfoCommand extends Command {
    private static final int MINIMUM_ARGUMENTS = 1;

    public InfoCommand() {
        super("info", options("&cIncorrect usage! &7/papi info <expansion>", MINIMUM_ARGUMENTS, "placeholderapi.info"));
    }

    @Override
    public void execute(@NotNull final CommandSender sender, @NotNull final String[] args) {
        final String requestedExpansion = args[0];
        final PlaceholderExpansion ex = PlaceholderAPIPlugin.getInstance().getExpansionManager().getRegisteredExpansion(requestedExpansion);
        if (ex == null) {
            Msg.msg(sender, "&cThere is no expansion loaded with the identifier: &f" + requestedExpansion);

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

    @NotNull
    @Override
    public List<String> handleCompletion(@NotNull final CommandSender sender, @NotNull final String[] args) {
        if (args.length == MINIMUM_ARGUMENTS) {
            final Set<String> completions = PlaceholderAPI.getRegisteredIdentifiers();

            return StringUtil.copyPartialMatches(args[0], completions, new ArrayList<>(completions.size()));
        }

        return super.handleCompletion(sender, args);
    }
}
