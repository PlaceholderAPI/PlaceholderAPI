package me.clip.placeholderapi.commands;

import com.google.common.collect.Lists;
import me.clip.placeholderapi.commands.command.*;
import me.clip.placeholderapi.commands.command.ecloud.*;
import me.clip.placeholderapi.util.Msg;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public final class CommandHandler implements CommandExecutor {
    private static final Command DEFAULT = new VersionCommand();

    protected static final List<Command> COMMANDS = Lists.newArrayList(
            DEFAULT,
            new HelpCommand(),
            new InfoCommand(),
            new ListCommand(),
            new RegisterCommand(),
            new UnregisterCommand(),
            new ReloadCommand(),
            new BcParseCommand(),
            new ParseCommand(),
            new ParseRelCommand(),

            new EcloudCommand(),
            new EcloudClearCommand(),
            new EcloudDownloadCommand(),
            new EcloudInfoCommand(),
            new EcloudListCommand(),
            new EcloudPlaceholdersCommand(),
            new EcloudRefreshCommand(),
            new EcloudStatusCommand(),
            new EcloudVersionInfoCommand(),
            new EcloudDisableCommand(),
            new EcloudEnableCommand()
    );

    static {
        COMMANDS.sort((command1, command2) -> {
            int comparison = Integer.compare(command1.getMatch().length(), command2.getMatch().length());

            if (comparison == 1) return -1;
            if (comparison == -1) return 1;
            return 0;
        });
    }

    private static String[] splitArguments(String joinedArguments, String command) {
        joinedArguments = StringUtils.remove(joinedArguments, command).trim();
        String[] args = StringUtils.split(joinedArguments);
        return args.length == 1 && args[0].isEmpty() ? new String[0] : args;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command bukkitCommand, @NotNull String name, String[] args) {
        if (args.length == 0) {
            DEFAULT.execute(sender, args);
            return true;
        }

        String joined = String.join(" ", args).toLowerCase();
        Optional<Command> optional = COMMANDS.stream()
                .filter(command -> joined.startsWith(command.getMatch()))
                .findFirst();

        if (!optional.isPresent()) {
            Msg.msg(sender, "&cUnknown command.");
            return true;
        }

        Command command = optional.get();
        String permission = command.getPermission();
        if (!sender.hasPermission(permission)) {
            Msg.msg(sender, "&cYou do not have the permission to use this command.");
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
}
