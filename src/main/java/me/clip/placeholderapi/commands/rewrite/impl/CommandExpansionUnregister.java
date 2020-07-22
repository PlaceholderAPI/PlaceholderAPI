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

public final class CommandExpansionUnregister extends PlaceholderCommand
{

	public CommandExpansionUnregister()
	{
		super("unregister");
	}

	@Override
	public void evaluate(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, @NotNull final String alias, @NotNull final List<String> params)
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


		final String message = !PlaceholderAPI.unregisterExpansion(expansion) ?
							   "&cFailed to unregister expansion: &f" :
							   "&aSuccessfully unregistered expansion: &f";

		Msg.msg(sender, message + expansion.getName());
	}

	@Override
	public void complete(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, @NotNull final String alias, @NotNull final List<String> params, @NotNull final List<String> suggestions)
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
