package me.clip.placeholderapi.commands;

import com.google.common.collect.Lists;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.command.*;
import me.clip.placeholderapi.commands.command.ecloud.EcloudInfoCommand;
import me.clip.placeholderapi.commands.command.ecloud.EcloudListCommand;
import me.clip.placeholderapi.commands.command.ecloud.*;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

public final class CommandHandler implements CommandExecutor {
    private static final Command DEFAULT = new VersionCommand();

    private static final List<Command> COMMANDS = Lists.newArrayList(
            new EcloudClearCommand(),
            new EcloudDownloadCommand(),
            new EcloudInfoCommand(),
            new EcloudListCommand(),
            new EcloudPlaceholdersCommand(),
            new EcloudRefreshCommand(),
            new EcloudStatusCommand(),
            new EcloudVersionInfoCommand(),
            new EcloudCommand(),
            new BcParseCommand(),
            new ParseCommand(),
            new ParseRelCommand(),
            new DisableEcloudCommand(),
            new EnableCloudCommand(),
            new HelpCommand(),
            new InfoCommand(),
            new ListCommand(),
            new RegisterCommand(),
            new ReloadCommand(),
            DEFAULT,
            new UnregisterCommand()
    );

    static {
        COMMANDS.sort((command1, command2) -> {
            final int comparison = Integer.compare(command1.getMatch().length(), command2.getMatch().length());

            if (comparison == 1) return -1;
            if (comparison == -1) return 1;
            return 0;
        });
        Objects.requireNonNull(PlaceholderAPIPlugin.getInstance().getCommand("placeholderapi"))
            .setTabCompleter(new CompletionHandler(COMMANDS));
    }

    private static final Pattern SPACE_PATTERN = Pattern.compile(" ");

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
                .findFirst();

        if (!optional.isPresent()) {
            sender.sendMessage("Specified command is not valid.");
            return true;
        }

        final Command command = optional.get();

        if (!command.getPermissions().isEmpty() && command.getPermissions().stream().noneMatch(sender::hasPermission)) {
            sender.sendMessage("You do not have the permission to execute specified command.");
            return true;
        }

        args = splitArguments(joined, command.getMatch());

        if (args.length < command.getMinimumArguments()) {
            Msg.msg(sender, command.getUsage());
            return true;
        }

        command.execute(sender, args);

        return true;
    }

    static String[] splitArguments(@NotNull final String joinedArguments, @NotNull final String command) {
        final String[] args = SPACE_PATTERN.split(joinedArguments.replace(command, "").trim());
        return args.length == 1 && args[0].isEmpty() ? new String[]{} : args;
    }
}
