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

import java.util.Collections;
import java.util.List;

public class BcParseCommand extends Command {

    public BcParseCommand() {
        super("bcparse", 1, 1);

        permissions().add("placeholderapi.parse");
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (handleUsage(sender, args)) return;

        OfflinePlayer player;

        final String input = args[1];
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

        final String parse = StringUtils.join(args, " ", 2, args.length);
        Msg.broadcast("&r" + PlaceholderAPI.setPlaceholders(player, parse));
    }

    @Override
    public boolean handleUsage(@NotNull CommandSender sender, @NotNull String[] args) {
        final int given = args.length - super.getCommandLength();

        if (given < super.getMinArguments()) {
            Msg.msg(sender, "&cYou must specify a player.");
            return true;
        }
        return false;
    }

    @Override
    public List<String> handleCompletion(@NotNull CommandSender sender, @NotNull String[] args) {
        final int required = super.getMinArguments() + super.getCommandLength();
        if (args.length == required) {
            return null;
        }

        return Collections.emptyList();
    }
}
