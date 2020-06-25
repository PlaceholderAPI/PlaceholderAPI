package me.clip.placeholderapi.commands.papi;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public class ListCommand extends Command {

    public ListCommand() {
        super("list", 0);
        options.permissions("placeholderapi.list");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, String[] args) {
        Set<String> registered = PlaceholderAPI.getRegisteredIdentifiers();
        if (registered.isEmpty()) {
            Msg.msg(sender, "&7There are no placeholder hooks currently registered!");

            return true;
        }

        Msg.msg(sender, registered.size() + " &7Placeholder hooks registered:");
        Msg.msg(sender, registered.stream().sorted().collect(Collectors.joining(", ")));

        return true;
    }
}
