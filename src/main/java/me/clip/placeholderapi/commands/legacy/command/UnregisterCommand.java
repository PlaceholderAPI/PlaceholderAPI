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

public final class UnregisterCommand extends Command {
    private static final int MINIMUM_ARGUMENTS = 1;

    public UnregisterCommand() {
        super("unregister", options("&cAn expansion name must be specified!", MINIMUM_ARGUMENTS, "placeholderapi.register"));
    }

    @Override
    public void execute(@NotNull final CommandSender sender, @NotNull final String[] args) {
        final String requestedExpansion = args[0];
        final PlaceholderExpansion expansion = PlaceholderAPIPlugin.getInstance().getExpansionManager()
                .getRegisteredExpansion(requestedExpansion);

        if (expansion == null) {
            Msg.msg(sender, "&cFailed to find expansion: &f" + requestedExpansion);

            return;
        }

        if (PlaceholderAPI.unregisterExpansion(expansion)) {
            Msg.msg(sender, "&aSuccessfully unregistered expansion: &f" + expansion.getName());
        } else {
            Msg.msg(sender, "&cFailed to unregister expansion: &f" + expansion.getName());
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
