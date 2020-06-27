package me.clip.placeholderapi.commands.command;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ParseRelCommand extends Command {

    public ParseRelCommand() {
        super("parserel", 1, 2);

        permissions().add("placeholderapi.parse");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (handleUsage(sender, args)) return true;

        final Player one = Bukkit.getPlayer(args[0]);
        if (one == null) {
            Msg.msg(sender, args[0] + " &cis not online!");

            return true;
        }

        final Player two = Bukkit.getPlayer(args[1]);
        if (two == null) {
            Msg.msg(sender, args[1] + " &cis not online!");

            return true;
        }

        final String parse = StringUtils.join(args, " ", 2, args.length);
        Msg.msg(sender, "&r" + PlaceholderAPI.setRelationalPlaceholders(one, two, parse));

        return true;
    }

    @Override
    public boolean handleUsage(@NotNull CommandSender sender, @NotNull String[] args) {
        final int given = args.length - super.getLength();

        if (given < super.getMin()) {
            Msg.msg(sender, "&cYou must specify at least two players.");
            return true;
        }
        return false;
    }

    @Override
    public List<String> handleCompletion(@NotNull CommandSender sender, @NotNull String[] args) {
        final int required = super.getMin() + super.getLength();

        if (args.length == required || args.length == required + 1) {
            return null;
        }

        return Collections.emptyList();
    }
}
