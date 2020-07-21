package me.clip.placeholderapi.commands.rewrite.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.rewrite.PlaceholderCommand;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

public final class CommandParse extends PlaceholderCommand
{

	public CommandParse()
	{
		super("parse", "bcparse");
	}


	@Override
	public void evaluate(final @NotNull PlaceholderAPIPlugin plugin, final @NotNull CommandSender sender, final @NotNull String alias, final @NotNull List<String> params)
	{
		if (params.size() < 2)
		{
			Msg.msg(sender, "&cYou must supply a target, and message: &b/papi parse &7{target} &a{message}");
			return;
		}

		final OfflinePlayer player;

		if (!"me".equalsIgnoreCase(params.get(0)))
		{
			player = Bukkit.getPlayer(params.get(0));

			if (player == null)
			{
				Msg.msg(sender, "&cFailed to find player: &7" + params.get(0));
				return;
			}

		}
		else
		{
			if (!(sender instanceof Player))
			{
				Msg.msg(sender, "&cYou must be a player to use &7me&c as a target!");
				return;
			}

			player = ((Player) sender);
		}

		final String message = String.join(" ", params.subList(1, params.size()));

		if (alias.equalsIgnoreCase("bcparse"))
		{
			Msg.broadcast(PlaceholderAPI.setPlaceholders(player, message));
		}
		else
		{
			Msg.msg(sender, PlaceholderAPI.setPlaceholders(player, message));
		}

	}

	@Override
	public void complete(final @NotNull PlaceholderAPIPlugin plugin, final @NotNull CommandSender sender, final @NotNull String alias, final @NotNull List<String> params, final @NotNull List<String> suggestions)
	{
		if (params.isEmpty() || (params.size() == 1 && params.get(0).toLowerCase().startsWith("m")))
		{
			suggestions.add("me");
		}

		final Stream<String> names = plugin.getServer().getOnlinePlayers().stream().map(Player::getName);

		switch (params.size())
		{
			case 0:
				names.forEach(suggestions::add);
				break;
			case 1:
				names.filter(name -> name.toLowerCase().startsWith(params.get(0).toLowerCase())).forEach(suggestions::add);
				break;
		}
	}

}
