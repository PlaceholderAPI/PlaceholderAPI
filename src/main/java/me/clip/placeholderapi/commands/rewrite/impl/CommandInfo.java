package me.clip.placeholderapi.commands.rewrite.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.rewrite.PlaceholderCommand;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

public final class CommandInfo extends PlaceholderCommand
{

	public CommandInfo()
	{
		super("info");
	}

	@Override
	public void evaluate(final @NotNull PlaceholderAPIPlugin plugin, final @NotNull CommandSender sender, final @NotNull String alias, final @NotNull List<String> params)
	{
		if (params.size() < 1)
		{
			Msg.msg(sender,
					"&cYou must specify the name of the expansion.");
			return;
		}

		final PlaceholderExpansion expansion = plugin.getExpansionManager().getRegisteredExpansion(params.get(0));
		if (expansion == null)
		{
			Msg.msg(sender,
					"&cThere is no expansion loaded with the identifier: &f" + params.get(0));
			return;
		}

		final StringBuilder builder = new StringBuilder();

		builder.append("&7Placeholder expansion info for: &r")
			   .append(expansion.getName())
			   .append('\n')
			   .append("&7Status: &r")
			   .append(expansion.isRegistered() ? "&aRegistered" : "7cNotRegistered")
			   .append('\n');

		final String author = expansion.getAuthor();
		if (author != null)
		{
			builder.append("&7Author: &r")
				   .append(author)
				   .append('\n');
		}

		final String version = expansion.getVersion();
		if (version != null)
		{
			builder.append("&7Version: &r")
				   .append(version)
				   .append('\n');
		}

		final String requiredPlugin = expansion.getRequiredPlugin();
		if (requiredPlugin != null)
		{
			builder.append("&7Requires plugin: &r")
				   .append(requiredPlugin)
				   .append('\n');
		}

		final List<String> placeholders = expansion.getPlaceholders();
		if (placeholders != null && !placeholders.isEmpty())
		{
			builder.append("&8&m-- &7Placeholders &8&m--&r")
				   .append('\n');

			for (final String placeholder : placeholders)
			{
				builder.append(placeholder)
					   .append('\n');
			}
		}

		Msg.msg(sender, builder.toString());
	}

	@Override
	public void complete(final @NotNull PlaceholderAPIPlugin plugin, final @NotNull CommandSender sender, final @NotNull String alias, final @NotNull List<String> params, final @NotNull List<String> suggestions)
	{
		final Stream<String> identifiers = PlaceholderAPI.getRegisteredIdentifiers().stream();

		switch (params.size())
		{
			case 0:
				identifiers.forEach(suggestions::add);
				break;
			case 1:
				identifiers.filter(identifier -> identifier.startsWith(params.get(0).toLowerCase())).forEach(suggestions::add);
				break;
		}
	}

}
