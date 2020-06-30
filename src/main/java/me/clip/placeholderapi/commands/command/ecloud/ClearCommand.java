package me.clip.placeholderapi.commands.command.ecloud;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ClearCommand extends Command {

    public ClearCommand() {
        super("ecloud clear", permissions("placeholderapi.ecloud"));
    }

    @Override
    public boolean execute(@NotNull final CommandSender sender, @NotNull final String[] args) {
        PlaceholderAPIPlugin.getInstance().getExpansionCloud().clean();
        Msg.msg(sender, "&aThe cache has been cleared!!");

        return true;
    }
}
