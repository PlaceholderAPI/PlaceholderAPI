package me.clip.placeholderapi.commands.command;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends Command {

    public ReloadCommand() {
        super("reload", 1, 0);

        permissions().add("placeholderapi.reload");
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        Msg.msg(sender, "&fPlaceholder&7API &bconfiguration reloaded!");
        PlaceholderAPIPlugin.getInstance().reloadConf(sender);
    }

}
