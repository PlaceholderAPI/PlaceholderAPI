package me.clip.placeholderapi.commands.legacy.command.ecloud;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.legacy.Command;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class EcloudClearCommand extends Command {
    public EcloudClearCommand() {
        super("ecloud clear", permissions("placeholderapi.ecloud"));
    }

    @Override
    public void execute(@NotNull final CommandSender sender, @NotNull final String[] args) {
        PlaceholderAPIPlugin.getInstance().getExpansionCloud().clean();
        Msg.msg(sender, "&aThe cache has been cleared!!");
    }
}
