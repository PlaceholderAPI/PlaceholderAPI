package me.clip.placeholderapi.commands.command;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class RegisterCommand extends Command {
    private static final int MINIMUM_ARGUMENTS = 1;

    public RegisterCommand() {
        super("register", options("&cAn expansion file name must be specified!", "placeholderapi.register"));
    }

    @Override
    public boolean execute(@NotNull final CommandSender sender, @NotNull final String[] args) {
        if (args.length < MINIMUM_ARGUMENTS) {
            return false;
        }

        final String fileName = args[0].replace(".jar", "");
        final PlaceholderExpansion expansion = PlaceholderAPIPlugin.getInstance().getExpansionManager().registerExpansion(fileName);

        if (expansion == null) {
            Msg.msg(sender, "&cFailed to register expansion from " + fileName);

            return true;
        }

        Msg.msg(sender, "&aSuccessfully registered expansion: &f" + expansion.getName());
        return true;
    }
}
