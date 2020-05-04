package me.clip.placeholderapi.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public abstract class Command {
    protected final Options options = new Options();
    private final String command;
    private List<String> permissions;
    private String usage;
    private int requiredArgs = 0;
    private boolean def = false;
    private boolean playerOnly = false;

    protected Command(@NotNull final String command) {
        this.command = command;
    }

    @NotNull
    public String getCommand() {
        return this.command;
    }

    @NotNull
    public String getUsage() {
        return this.usage;
    }

    @NotNull
    public List<String> getPermissions() {
        return this.permissions;
    }

    public int getRequiredArgs() {
        return this.requiredArgs;
    }

    public boolean isDefault() {
        return this.def;
    }

    public boolean isPlayerOnly() {
        return this.playerOnly;
    }

    public abstract boolean execute(@NotNull final CommandSender sender, @NotNull final String[] args);

    protected final class Options {
        @NotNull
        public Options permissions(@NotNull final String... values) {
            permissions = Arrays.asList(values);
            return this;
        }

        @NotNull
        public Options usage(@NotNull final String value) {
            usage = value;
            return this;
        }

        @NotNull
        public Options def(final boolean value) {
            def = value;
            return this;
        }

        @NotNull
        public Options playerOnly(final boolean value) {
            playerOnly = value;
            return this;
        }

        @NotNull
        public Options requiredArgs(final int value) {
            requiredArgs = value;
            return this;
        }
    }
}

