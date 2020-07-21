package me.clip.placeholderapi.commands.legacy.command;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.legacy.Command;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class RegisterCommand extends Command {
    public RegisterCommand() {
        super("register", options("&cAn expansion file name must be specified!", 1,"placeholderapi.register"));
    }

    @Override
    public void execute(@NotNull final CommandSender sender, @NotNull final String[] args) {
        final String fileName = args[0].replace(".jar", "");
        final PlaceholderExpansion expansion = PlaceholderAPIPlugin.getInstance().getExpansionManager().registerExpansion(fileName);

        if (expansion == null) {
            Msg.msg(sender, "&cFailed to register expansion from " + fileName);

            return;
        }

        Msg.msg(sender, "&aSuccessfully registered expansion: &f" + expansion.getName());
    }
}
