package me.clip.placeholderapi.commands.command;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class BcParseCommand extends Command {
    public BcParseCommand() {
        super("bcparse", options("&cYou must specify a player.", 1));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        OfflinePlayer player;
        String input = args[0];

        if (input.equalsIgnoreCase("me")) {
            if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                Msg.msg(sender, "&cThis command must target a player when used by console");
                return;
            }
        } else {
            player = Bukkit.getPlayer(input);
            if (player == null) player = Bukkit.getOfflinePlayer(input);
            if (player == null || !player.hasPlayedBefore()) {
                Msg.msg(sender, "&cCould not find player&8: &f" + input);
                return;
            }
        }

        String parse = StringUtils.join(args, " ", 1, args.length);
        Msg.broadcast("&r" + PlaceholderAPI.setPlaceholders(player, parse));
    }
}
