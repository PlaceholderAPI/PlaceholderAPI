package me.clip.placeholderapi.commands;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.papi.InfoCommand;
import me.clip.placeholderapi.commands.papi.ListCommand;
import me.clip.placeholderapi.commands.papi.*;
import me.clip.placeholderapi.commands.papi.ecloud.*;
import me.clip.placeholderapi.exceptions.NoDefaultCommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandHandler implements CommandExecutor {

    private final Set<Command> commands;
    private final Command defaultCommand;

    public CommandHandler(final PlaceholderAPIPlugin plugin) {
        this.commands = Stream.of(new VersionCommand(plugin), new UnregisterCommand(plugin), new RegisterCommand(plugin), new ReloadCommand(plugin), new ParseRelCommand(), new ParseCommand(), new ListCommand(), new InfoCommand(plugin), new HelpCommand(plugin), new EnableEcloudCommand(plugin), new EcloudCommand(plugin), new DisableEcloudCommand(plugin), new VersionInfoCommand(plugin), new StatusCommand(plugin), new RefreshCommand(plugin), new PlaceholdersCommand(plugin), new me.clip.placeholderapi.commands.papi.ecloud.ListCommand(plugin), new me.clip.placeholderapi.commands.papi.ecloud.InfoCommand(plugin), new DownloadCommand(plugin), new ClearCommand(plugin)).collect(Collectors.toSet());
        defaultCommand = commands.stream()
                .filter(Command::isDefault)
                .findAny().orElseThrow(() -> new NoDefaultCommandException("There is no default command present in the plugin."));
    }

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final org.bukkit.command.Command bukkitCommand,
                             @NotNull final String label, @NotNull final String[] args) {
        if (args.length == 0) {
            defaultCommand.execute(sender, args);
            return true;
        }

        final Optional<Command> optionalCommand = commands.stream().filter(cmd -> cmd.getCommand().equalsIgnoreCase(args[0])).findAny();

        if (!optionalCommand.isPresent()) {
            sender.sendMessage("Unknown Command.");
            return true;
        }

        final Command command = optionalCommand.get();

        if (command.isPlayerOnly() && !(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by a player!");
            return true;
        }

        if (!command.getPermissions().isEmpty() && command.getPermissions().stream().noneMatch(sender::hasPermission)) {
            sender.sendMessage("You do not have permission for this command!");
            return true;
        }

        final boolean result = command.execute(sender, Arrays.copyOfRange(args, 1, args.length));

        if (!result) {
            sender.sendMessage("Incorrect usage! Usage: " + args[0] + " " + command.getUsage());
        }

        return true;
    }

}