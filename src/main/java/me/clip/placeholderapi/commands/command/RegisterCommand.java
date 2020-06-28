package me.clip.placeholderapi.commands.command;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RegisterCommand extends Command {

    public RegisterCommand() {
        super("register", 1, 1);

        permissions().add("placeholderapi.register");
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (handleUsage(sender, args)) return;

        final String fileName = args[1].replace(".jar", "");
        final PlaceholderExpansion ex = PlaceholderAPIPlugin.getInstance().getExpansionManager().registerExpansion(fileName);

        if (ex == null) {
            Msg.msg(sender, "&cFailed to register expansion from " + fileName);

            return;
        }

        Msg.msg(sender, "&aSuccessfully registered expansion: &f" + ex.getName());
    }

    @Override
    public boolean handleUsage(@NotNull CommandSender sender, @NotNull String[] args) {
        final int given = args.length - super.getCommandLength();

        if (given < super.getMinArguments()) {
            Msg.msg(sender, "&cAn expansion file name must be specified!");
            return true;
        }
        return false;
    }

}
