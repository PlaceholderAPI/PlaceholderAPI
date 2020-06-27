package me.clip.placeholderapi.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CompletionHandler implements TabCompleter {

    private final List<Command> commands;

    CompletionHandler(@NotNull final List<Command> cmds) {
        this.commands = cmds;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command cmd, @NotNull String name, @NotNull String[] args) {
        final String joined = String.join(" ", args).toLowerCase();
        final Optional<Command> optional = commands.stream().filter(command -> joined.startsWith(command.getCommand()) && args.length >= command.getLength()).findAny();

        if (optional.isPresent()) {
            final Command command = optional.get();

            return command.handleCompletion(sender, args);
        }

        return Collections.emptyList();
    }
}
