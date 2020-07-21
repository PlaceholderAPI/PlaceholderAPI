package me.clip.placeholderapi.commands.legacy.command;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.commands.legacy.Command;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public final class ListCommand extends Command {
    public ListCommand() {
        super("list", permissions("placeholderapi.list"));
    }

    @Override
    public void execute(@NotNull final CommandSender sender, @NotNull final String[] args) {
        final Set<String> registered = PlaceholderAPI.getRegisteredIdentifiers();
        if (registered.isEmpty()) {
            Msg.msg(sender, "&7There are no placeholder hooks currently registered!");
            return;
        }

        Msg.msg(sender, registered.size() + " &7Placeholder hooks registered:",
                registered.stream().sorted().collect(Collectors.joining(", ")));
    }
}
