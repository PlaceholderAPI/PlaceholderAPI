package me.clip.placeholderapi.commands.papi;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ParseCommand extends Command {

    public ParseCommand() {
        super("parse");
        options.permissions("placeholderapi.parse").requiredArgs(1);
    }

    @Override
    public boolean execute(final @NotNull CommandSender sender, final String[] args) {
        OfflinePlayer player;

        if (args[1].equalsIgnoreCase("me")) {
            if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                Msg.msg(sender, "&cThis command must target a player when used by console");

                return true;
            }
        } else {
            if (Bukkit.getPlayer(args[1]) != null) {
                player = Bukkit.getPlayer(args[1]);
            } else {
                player = Bukkit.getOfflinePlayer(args[1]);
            }
        }

        if (player == null || !player.hasPlayedBefore()) {
            Msg.msg(sender, "&cFailed to find player: &f" + args[1]);
            return true;
        }

        String parse = StringUtils.join(args, " ", 2, args.length);

        if (args[0].equalsIgnoreCase("bcparse")) {
            Msg.broadcast("&r" + PlaceholderAPI.setPlaceholders(player, parse));
        } else {
            Msg.msg(sender, "&r" + PlaceholderAPI.setPlaceholders(player, parse));
        }

        return true;
    }

}
