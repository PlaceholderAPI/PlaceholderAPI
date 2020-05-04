package me.clip.placeholderapi.commands.papi;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ParseRelCommand extends Command {

    public ParseRelCommand() {
        super("parserel");
        options.permissions("placeholderapi.parse").playerOnly(true).requiredArgs(2);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, String[] args) {
        Player one = Bukkit.getPlayer(args[1]);
        if (one == null) {
            Msg.msg(sender, args[1] + " &cis not online!");

            return true;
        }

        Player two = Bukkit.getPlayer(args[2]);
        if (two == null) {
            Msg.msg(sender, args[2] + " &cis not online!");

            return true;
        }

        String parse = StringUtils.join(args, " ", 3, args.length);
        Msg.msg(sender, "&r" + PlaceholderAPI.setRelationalPlaceholders(one, two, parse));

        return true;
    }
}
