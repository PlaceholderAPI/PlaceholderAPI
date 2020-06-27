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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UnregisterCommand extends Command {

    @NotNull
    private final PlaceholderAPIPlugin plugin;

    public UnregisterCommand(@NotNull final PlaceholderAPIPlugin plugin) {
        super("unregister", 1, 1);

        permissions().add("placeholderapi.register");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (handleUsage(sender, args)) return true;

        final String input = args[1];
        final PlaceholderExpansion ex = plugin.getExpansionManager().getRegisteredExpansion(input);
        if (ex == null) {
            Msg.msg(sender, "&cFailed to find expansion: &f" + input);

            return true;
        }

        if (PlaceholderAPI.unregisterExpansion(ex)) {
            Msg.msg(sender, "&aSuccessfully unregistered expansion: &f" + ex.getName());
        } else {
            Msg.msg(sender, "&cFailed to unregister expansion: &f" + ex.getName());
        }
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
                    "some completion"
            ));

            return StringUtil.copyPartialMatches(args[required - 1], completions, new ArrayList<>(completions.size()));
        }

        return Collections.emptyList();
    }
}
