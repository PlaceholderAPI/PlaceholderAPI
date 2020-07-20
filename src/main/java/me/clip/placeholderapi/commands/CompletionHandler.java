package me.clip.placeholderapi.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class CompletionHandler implements TabCompleter {
    private final List<Command> commands;

    CompletionHandler(@NotNull final List<Command> commands) {
        this.commands = commands;
    }

    // it makes me physically cringe trying to understand why bukkit uses a list instead of a set for this
    @NotNull
    @Override
    public List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final org.bukkit.command.Command bukkitCommand,
                                      @NotNull final String name, @NotNull final String[] args) {
        final String joined = String.join(" ", args).toLowerCase();
        final Optional<Command> optional = commands.stream()
                .filter(command -> joined.startsWith(command.getMatch()))
                .findAny();

        return optional
                .map(command -> command.handleCompletion(sender, CommandHandler.splitArguments(joined, command.getMatch())))
                .orElse(Collections.emptyList());
    }
}
