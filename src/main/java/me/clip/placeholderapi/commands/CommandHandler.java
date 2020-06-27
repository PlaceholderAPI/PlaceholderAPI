package me.clip.placeholderapi.commands;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.command.*;
import me.clip.placeholderapi.commands.command.ecloud.InfoCommand;
import me.clip.placeholderapi.commands.command.ecloud.ListCommand;
import me.clip.placeholderapi.commands.command.ecloud.*;
import me.clip.placeholderapi.exceptions.NoDefaultCommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CommandHandler implements CommandExecutor {

    private final List<Command> commands;
    private final Command def;

    public CommandHandler(final PlaceholderAPIPlugin plugin) {
        this.commands = Arrays.asList(
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

        this.def = commands.stream().filter(command -> command.getCommand().length() == 0)
                .findAny().orElseThrow(() -> new NoDefaultCommandException("There is no default command present in the plugin."));

        Objects.requireNonNull(plugin.getCommand("placeholderapi")).setTabCompleter(new CompletionHandler(commands));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command cmd, @NotNull String name, @NotNull String[] args) {
        if (args.length == 0) {
            def.execute(sender, args);
            return true;
        }

        final String joined = String.join(" ", args).toLowerCase();
        final Optional<Command> optional = commands.stream().filter(command -> joined.startsWith(command.getCommand())).findAny();

        if (!optional.isPresent()) {
            sender.sendMessage("Specified command is not valid.");
            return true;
        }

        final Command command = optional.get();
        if (!checkPermissions(command, sender)) {
            sender.sendMessage("You do not have the permission to execute specified command.");
            return true;
        }

        command.execute(sender, args);
        return true;
    }

    private boolean checkPermissions(Command command, CommandSender sender) {
        return command.permissions().getPermissions().stream().anyMatch(sender::hasPermission);
    }
}
