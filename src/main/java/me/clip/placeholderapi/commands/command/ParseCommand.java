package me.clip.placeholderapi.commands.command;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class ParseCommand extends Command {
    public ParseCommand() {
        super("parse", options("&cYou must specify a player.", 1, "placeholderapi.parse"));
    }

    @Override
    public void execute(@NotNull final CommandSender sender, @NotNull final String[] args) {
        final OfflinePlayer player;
        final String input = args[0];

        if (input.equalsIgnoreCase("me")) {
            if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                Msg.msg(sender, "&cThis command must target a player when used by console");

                return;
            }
        } else {
            if (Bukkit.getPlayer(input) != null) {
                player = Bukkit.getPlayer(input);
            } else {
                player = Bukkit.getOfflinePlayer(input);
            }
        }

        if (player == null || !player.hasPlayedBefore()) {
            Msg.msg(sender, "&cFailed to find player: &f" + input);
            return;
        }

        final String parse = StringUtils.join(args, " ", 1, args.length);
        Msg.msg(sender, "&r" + PlaceholderAPI.setPlaceholders(player, parse));
    }
}
