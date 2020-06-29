package me.clip.placeholderapi.commands;

import com.google.common.collect.Sets;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.command.BcParseCommand;
import me.clip.placeholderapi.commands.command.DisableEcloudCommand;
import me.clip.placeholderapi.commands.command.EcloudCommand;
import me.clip.placeholderapi.commands.command.EnableCloudCommand;
import me.clip.placeholderapi.commands.command.HelpCommand;
import me.clip.placeholderapi.commands.command.ParseCommand;
import me.clip.placeholderapi.commands.command.ParseRelCommand;
import me.clip.placeholderapi.commands.command.RegisterCommand;
import me.clip.placeholderapi.commands.command.ReloadCommand;
import me.clip.placeholderapi.commands.command.UnregisterCommand;
import me.clip.placeholderapi.commands.command.VersionCommand;
import me.clip.placeholderapi.commands.command.ecloud.ClearCommand;
import me.clip.placeholderapi.commands.command.ecloud.DownloadCommand;
import me.clip.placeholderapi.commands.command.ecloud.InfoCommand;
import me.clip.placeholderapi.commands.command.ecloud.ListCommand;
import me.clip.placeholderapi.commands.command.ecloud.PlaceholdersCommand;
import me.clip.placeholderapi.commands.command.ecloud.RefreshCommand;
import me.clip.placeholderapi.commands.command.ecloud.StatusCommand;
import me.clip.placeholderapi.commands.command.ecloud.VersionInfoCommand;
import me.clip.placeholderapi.exceptions.NoDefaultCommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandHandler implements CommandExecutor {
    private static final Set<Command> COMMANDS = Sets.newHashSet(
            new ClearCommand(),
            new DownloadCommand(),
            new InfoCommand(),
            new ListCommand(),
            new PlaceholdersCommand(),
            new RefreshCommand(),
            new StatusCommand(),
            new VersionInfoCommand(),
            new EcloudCommand(),
            new BcParseCommand(),
            new ParseCommand(),
            new ParseRelCommand(),
            new DisableEcloudCommand(),
            new EnableCloudCommand(),
            new HelpCommand(),
            new me.clip.placeholderapi.commands.command.InfoCommand(),
            new me.clip.placeholderapi.commands.command.ListCommand(),
            new RegisterCommand(),
            new ReloadCommand(),
            new VersionCommand(),
            new UnregisterCommand()
    );

    private static final Command DEFAULT = COMMANDS.stream()
            .filter(command -> command.getCommand().isEmpty())
            .findAny().orElseThrow(() -> new NoDefaultCommandException("There is no default command present in the plugin."));

    static {
        Objects.requireNonNull(PlaceholderAPIPlugin.getInstance().getCommand("placeholderapi"))
                .setTabCompleter(new CompletionHandler(COMMANDS));
    }

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final org.bukkit.command.Command bukkitCommand,
                             @NotNull final String name, @NotNull final String[] args) {
        if (args.length == 0) {
            DEFAULT.execute(sender, args);
            return true;
        }

        final String joined = String.join(" ", args).toLowerCase();
        final Optional<Command> optional = COMMANDS.stream()
                .filter(command -> joined.startsWith(command.getCommand()))
                .findAny();

        if (!optional.isPresent()) {
            sender.sendMessage("Specified command is not valid.");
            return true;
        }

        final Command command = optional.get();

        if (!command.getPermissions().isEmpty() && command.getPermissions().stream().anyMatch(sender::hasPermission)) {
            sender.sendMessage("You do not have the permission to execute specified command.");
            return true;
        }

        command.execute(sender, args);
        return true;
    }
}
