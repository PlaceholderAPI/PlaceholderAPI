package me.clip.placeholderapi.commands.command.ecloud;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;

public final class EcloudClearCommand extends Command {
    public EcloudClearCommand() {
        super("ecloud clear");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        PlaceholderAPIPlugin.getInstance().getExpansionCloud().clean();
        Msg.msg(sender, "&aThe cache has been cleared!!");
    }
}
