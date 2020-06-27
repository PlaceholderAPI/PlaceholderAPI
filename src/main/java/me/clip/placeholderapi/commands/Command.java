package me.clip.placeholderapi.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Command {

    private final String command;
    private final int length;
    private final int min;
    private final Permissions permissions = new Permissions();

    protected Command(@NotNull final String command, final int length, final int min) {
        this.command = command;
        this.length = length;
        this.min = min;
    }

    public Permissions permissions() {
        return permissions;
    }

    public String getCommand() {
        return command;
    }

    public int getMin() {
        return min;
    }

    public int getLength() {
        return length;
    }

    public abstract boolean execute(@NotNull final CommandSender sender, @NotNull final String[] args);

    public abstract boolean handleUsage(@NotNull final CommandSender sender, @NotNull final String[] args);

    public abstract List<String> handleCompletion(@NotNull final CommandSender sender, @NotNull final String[] args);

    protected static class Permissions {

        private List<String> permissions = new ArrayList<>();

        public List<String> getPermissions() {
            return permissions;
        }

        public void add(final String... permissions) {
            this.permissions.addAll(Arrays.asList(permissions));
        }
    }
}
