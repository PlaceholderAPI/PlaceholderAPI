package me.clip.placeholderapi.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CompletionHandler implements TabCompleter {
    private final Set<Command> commands;

    CompletionHandler(@NotNull final Set<Command> commands) {
        this.commands = commands;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final org.bukkit.command.Command bukkitCommand,
                                      @NotNull final String name, @NotNull final String[] args) {
        final String joined = String.join(" ", args).toLowerCase();
        final Optional<Command> optional = commands.stream().filter(command ->
                joined.startsWith(command.getCommand()) && args.length >= command.getCommandLength()).findAny();

        return optional.map(command -> command.handleCompletion(sender, args)).orElse(Collections.emptyList());
    }
}
