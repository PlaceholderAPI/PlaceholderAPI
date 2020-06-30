package me.clip.placeholderapi.commands.command;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ParseRelCommand extends Command {
    private static final int MINIMUM_ARGUMENTS = 2;

    public ParseRelCommand() {
        super("parserel", options("&cYou must specify at least two players.", "placeholderapi.parse"));
    }

    @Override
    public boolean execute(@NotNull final CommandSender sender, @NotNull final String[] args) {
        if (args.length < MINIMUM_ARGUMENTS) {
            return false;
        }

        final Player one = Bukkit.getPlayer(args[0]);
        if (one == null) {
            Msg.msg(sender, args[0] + " &cis not online!");

            return true;
        }

        final Player two = Bukkit.getPlayer(args[0]);
        if (two == null) {
            Msg.msg(sender, args[0] + " &cis not online!");

            return true;
        }

        final String parse = StringUtils.join(args, " ", 2, args.length);
        Msg.msg(sender, "&r" + PlaceholderAPI.setRelationalPlaceholders(one, two, parse));

        return true;
    }
}
