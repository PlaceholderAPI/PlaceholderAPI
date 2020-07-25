package me.clip.placeholderapi.commands.impl.local;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.util.Msg;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.stream.Stream;

public final class CommandParse extends PlaceholderCommand
{

	public CommandParse()
	{
		super("parse", "bcparse", "parserel", "cmdparse");
	}


	@Override
	public void evaluate(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, @NotNull final String alias, @NotNull @Unmodifiable final List<String> params)
	{
		switch (alias.toLowerCase())
		{
			case "parserel":
				evaluateParseRelation(sender, params);
				break;
			case "parse":
				evaluateParseSingular(sender, params, false, false);
				break;
			case "bcparse":
				evaluateParseSingular(sender, params, true, false);
				break;
			case "cmdparse":
				evaluateParseSingular(sender, params, false, true);
				break;
		}
	}

	@Override
	public void complete(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, @NotNull final String alias, @NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions)
	{
		switch (alias.toLowerCase())
		{
			case "parserel":
				completeParseRelation(params, suggestions);
				break;
			case "parse":
			case "bcparse":
			case "cmdparse":
				completeParseSingular(sender, params, suggestions);
				break;
		}
	}


	private void evaluateParseSingular(@NotNull final CommandSender sender, @NotNull @Unmodifiable final List<String> params, final boolean broadcast, final boolean command)
	{
		if (params.size() < 2)
		{
			Msg.msg(sender, "&cYou must supply a target, and a message: &b/papi " + (broadcast ? "bcparse" : "parse") + " &7{target} &a{message}");
			return;
		}

		@NotNull final OfflinePlayer player;

		if ("me".equalsIgnoreCase(params.get(0)))
		{
			if (!(sender instanceof Player))
			{
				Msg.msg(sender, "&cYou must be a player to use &7me&c as a target!");
				return;
			}

			player = ((Player) sender);
		}
		else
		{
			final OfflinePlayer target = resolvePlayer(params.get(0));
			if (target == null)
			{
				Msg.msg(sender, "&cFailed to find player: &7" + params.get(0));
				return;
			}

			player = target;
		}

		final String message = PlaceholderAPI.setPlaceholders(player, String.join(" ", params.subList(1, params.size())));

		if (command)
		{
			Bukkit.dispatchCommand(sender, message);
			return;
		}

		if (broadcast)
		{
			Msg.broadcast(message);
		}
		else
		{
			if (!(sender instanceof Player))
			{
				Msg.msg(sender, message);
			}
			else
			{
				sender.spigot().sendMessage(TextComponent.fromLegacyText(message));
			}
		}
	}

	private void evaluateParseRelation(@NotNull final CommandSender sender, @NotNull @Unmodifiable final List<String> params)
	{
		if (params.size() < 3)
		{
			Msg.msg(sender, "&cYou must supply two targets, and a message: &b/papi parserel &7{target one} {target two} &a{message}");
			return;
		}

		final OfflinePlayer targetOne = resolvePlayer(params.get(0));
		if (targetOne == null || !targetOne.isOnline())
		{
			Msg.msg(sender, "&cFailed to find player: &7" + params.get(0));
			return;
		}

		final OfflinePlayer targetTwo = resolvePlayer(params.get(1));
		if (targetTwo == null || !targetTwo.isOnline())
		{
			Msg.msg(sender, "&cFailed to find player: &7" + params.get(1));
			return;
		}

		final String message = PlaceholderAPI.setRelationalPlaceholders(((Player) targetOne), ((Player) targetTwo), String.join(" ", params.subList(2, params.size())));
		Msg.msg(sender, message);
	}


	private void completeParseSingular(@NotNull final CommandSender sender, @NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions)
	{
		if (sender instanceof Player && (params.isEmpty() || (params.size() == 1 && "me".startsWith(params.get(0).toLowerCase()))))
		{
			suggestions.add("me");
		}

		final Stream<String> names = Bukkit.getOnlinePlayers().stream().map(Player::getName);
		suggestByParameter(names, suggestions, params.isEmpty() ? null : params.get(0));
	}

	private void completeParseRelation(@NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions)
	{
		if (params.size() > 2)
		{
			return;
		}

		final Stream<String> names = Bukkit.getOnlinePlayers().stream().map(Player::getName);
		suggestByParameter(names, suggestions, params.isEmpty() ? null : params.get(params.size() - 1));
	}


	@Nullable
	private OfflinePlayer resolvePlayer(@NotNull final String name)
	{
		OfflinePlayer target = Bukkit.getPlayer(name);

		if (target == null)
		{
			target = Bukkit.getOfflinePlayer(name); // this is probably not a great idea.
		}

		return target.hasPlayedBefore() ? target : null;

	}

}
