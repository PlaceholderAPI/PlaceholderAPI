package me.clip.placeholderapi.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class Command {
    private final String command;
    private final Set<String> permissions = new HashSet<>();
    private String usage;

    protected Command(@NotNull final String command) {
        this.command = command;
        this.length = length;
        this.min = min;
        usage = ""
    }

    protected void permissions(@NotNull final String... permissions) {
        this.permissions.addAll(Arrays.asList(permissions));
    }

    @NotNull
    public String getCommand() {
        return command;
    }

    @NotNull
    public Set<String> getPermissions() {
        return permissions;
    }

    @NotNull
    public String getUsage() {
        return usage;
    }

    public abstract void execute(@NotNull final CommandSender sender, @NotNull final String[] args);

    public boolean handleUsage(@NotNull final CommandSender sender, @NotNull final String[] args) {
        return false;
    }

    @NotNull
    public List<String> handleCompletion(@NotNull final CommandSender sender, @NotNull final String[] args) {
        return Collections.emptyList();
    }
}
