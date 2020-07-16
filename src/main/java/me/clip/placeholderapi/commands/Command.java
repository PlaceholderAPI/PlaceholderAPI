package me.clip.placeholderapi.commands;

import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public abstract class Command {
    private static final Options EMPTY_OPTIONS = new Options(null, 0);

    private final String match;
    private final String usage;
    private final int minimumArguments;
    /**
     * Commands should not have multiple permissions. This can lead to a lot of confusions.
     * This is also a lot more appropriate for maintainability, I saw a lot of commands regitered with wrong permissions.
     * We will use the main command name to parse our permission.
     */
    private final String permission;

    protected Command(String match) {
        this(match, EMPTY_OPTIONS);
    }

    protected Command(String match, Options options) {
        this.match = match;
        this.usage = options.usage == null ? "/papi " + match + " <required args> [optional args]" : options.usage;
        this.permission = "placeholderapi." + match.replace(' ', '.');
        this.minimumArguments = options.minimumArguments;
    }

    protected static Options options(String usage, int minimumArguments) {
        return new Options(usage, minimumArguments);
    }

    public String getMatch() {
        return match;
    }

    public String getUsage() {
        return usage;
    }

    public int getMinimumArguments() {
        return minimumArguments;
    }

    public String getPermission() {
        return permission;
    }

    public abstract void execute(CommandSender sender, String[] args);

    public List<String> handleCompletion(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    private static class Options {
        private final String usage;
        private final int minimumArguments;

        private Options(String usage, int minimumArguments) {
            this.usage = usage;
            this.minimumArguments = minimumArguments;
        }
    }
}
