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

public class BcParseCommand extends Command {

    public BcParseCommand() {
        super("bcparse", 1);
        options.permissions("placeholderapi.parse");
    }

    @Override
    public boolean execute(final @NotNull CommandSender sender, final String[] args) {
        OfflinePlayer player;

        if (args[0].equalsIgnoreCase("me")) {
            if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                Msg.msg(sender, "&cThis command must target a player when used by console");

                return true;
            }
        } else {
            if (Bukkit.getPlayer(args[0]) != null) {
                player = Bukkit.getPlayer(args[0]);
            } else {
                player = Bukkit.getOfflinePlayer(args[0]);
            }
        }

        if (player == null || !player.hasPlayedBefore()) {
            Msg.msg(sender, "&cFailed to find player: &f" + args[0]);
            return true;
        }

        String parse = StringUtils.join(args, " ", 1, args.length);
        Msg.broadcast("&r" + PlaceholderAPI.setPlaceholders(player, parse));
        return true;
    }
}
