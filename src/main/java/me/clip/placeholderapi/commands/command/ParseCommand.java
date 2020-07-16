package me.clip.placeholderapi.commands.command;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.commands.Command;
import me.clip.placeholderapi.util.Msg;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class ParseCommand extends Command {
    public ParseCommand() {
        super("parse", options("&cYou must specify a player.", 1));
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
        Msg.msg(sender, "&r" + PlaceholderAPI.setPlaceholders(player, parse));
    }

    @Override
    public List<String> handleCompletion(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> players = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
            players.add("me");
            if (args[0].isEmpty()) return players;
            else return players.stream().filter(name -> name.startsWith(args[0])).collect(Collectors.toList());
        }
        if (args.length == 2) return Collections.singletonList("<message>");
        return new ArrayList<>();
    }
}
