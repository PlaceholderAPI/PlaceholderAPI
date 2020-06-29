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

public class UnregisterCommand extends Command {

    public UnregisterCommand() {
        super("unregister", 1, 1);

        permissions("placeholderapi.register");
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (handleUsage(sender, args)) return;
        final PlaceholderAPIPlugin plugin = PlaceholderAPIPlugin.getInstance();

        final String input = args[1];
        final PlaceholderExpansion ex = plugin.getExpansionManager().getRegisteredExpansion(input);
        if (ex == null) {
            Msg.msg(sender, "&cFailed to find expansion: &f" + input);

            return;
        }

        if (PlaceholderAPI.unregisterExpansion(ex)) {
            Msg.msg(sender, "&aSuccessfully unregistered expansion: &f" + ex.getName());
        } else {
            Msg.msg(sender, "&cFailed to unregister expansion: &f" + ex.getName());
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
    public @NotNull List<String> handleCompletion(@NotNull CommandSender sender, @NotNull String[] args) {
        final int required = super.getMinArguments() + super.getCommandLength();

        if (args.length == required) {
            final Set<String> completions = PlaceholderAPI.getRegisteredIdentifiers();

            return StringUtil.copyPartialMatches(args[required - 1], completions, new ArrayList<>(completions.size()));
        }

        return Collections.emptyList();
    }
}
