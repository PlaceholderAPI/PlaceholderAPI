package me.clip.placeholderapi.commands.command;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.util.Msg;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

public final class RegisterCommand extends Command {
    public RegisterCommand() {
        super("register", options("&cAn expansion file name must be specified!", 1));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String fileName = StringUtils.remove(args[0], ".jar");
        PlaceholderExpansion expansion = PlaceholderAPIPlugin.getInstance().getExpansionManager().registerExpansion(fileName);

        if (expansion == null) {
            Msg.msg(sender, "&cFailed to register expansion from " + fileName);
            return;
        }

        Msg.msg(sender, "&aSuccessfully registered expansion: &f" + expansion.getName());
    }
}
