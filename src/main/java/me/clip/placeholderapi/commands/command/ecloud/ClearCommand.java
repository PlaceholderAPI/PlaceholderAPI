package me.clip.placeholderapi.commands.command.ecloud;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ClearCommand extends Command {

    public ClearCommand() {
        super("ecloud clear", 2, 0);

        permissions().add("placeholderapi.ecloud");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        Msg.msg(sender, "&aThe cache has been cleared!!");
        PlaceholderAPIPlugin.getInstance().getExpansionCloud().clean();
        return true;
    }

    @Override
    public boolean handleUsage(@NotNull CommandSender sender, @NotNull String[] args) {
        return false;
    }

    @Override
    public List<String> handleCompletion(@NotNull CommandSender sender, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
