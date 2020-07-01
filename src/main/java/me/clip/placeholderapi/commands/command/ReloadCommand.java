package me.clip.placeholderapi.commands.command;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class ReloadCommand extends Command {
    public ReloadCommand() {
        super("reload", permissions("placeholderapi.reload"));
    }

    @Override
    public void execute(@NotNull final CommandSender sender, @NotNull final String[] args) {
        Msg.msg(sender, "&fPlaceholder&7API &bconfiguration reloaded!");
        PlaceholderAPIPlugin.getInstance().reloadConf(sender);
    }
}
