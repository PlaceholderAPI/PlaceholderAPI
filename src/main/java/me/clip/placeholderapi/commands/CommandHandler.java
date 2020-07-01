package me.clip.placeholderapi.commands;

import com.google.common.collect.Sets;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.command.*;
import me.clip.placeholderapi.commands.command.ecloud.InfoCommand;
import me.clip.placeholderapi.commands.command.ecloud.ListCommand;
import me.clip.placeholderapi.commands.command.ecloud.*;
import me.clip.placeholderapi.exceptions.NoDefaultCommandException;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

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
            .filter(command -> command.getMatch().isEmpty())
            .findAny().orElseThrow(() -> new NoDefaultCommandException("There is no default command present in the plugin."));

    private static final Pattern SPACE_PATTERN = Pattern.compile(" ");

    static {
        Objects.requireNonNull(PlaceholderAPIPlugin.getInstance().getCommand("placeholderapi"))
                .setTabCompleter(new CompletionHandler(COMMANDS));
    }

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final org.bukkit.command.Command bukkitCommand,
                             @NotNull final String name, @NotNull String[] args) {
        if (args.length == 0) {
            DEFAULT.execute(sender, args);
            return true;
        }

        final String joined = String.join(" ", args).toLowerCase();
        final Optional<Command> optional = COMMANDS.stream()
                .filter(command -> joined.startsWith(command.getMatch()))
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

        args = shiftArguments(args, command.getMatch());

        if (args.length < command.getMinimumArguments()) {
            Msg.msg(sender, command.getUsage());
            return true;
        }

        command.execute(sender, args);

        return true;
    }

    static String[] shiftArguments(@NotNull final String[] arguments, final String command) {
        final int shift = SPACE_PATTERN.split(command).length + 1;
        final int newSize = arguments.length - shift;
        final String[] result = new String[newSize];

        if (newSize >= 0) System.arraycopy(arguments, shift, result, 0, newSize);

        return result;
    }
}
