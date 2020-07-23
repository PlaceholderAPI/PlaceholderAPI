package me.clip.placeholderapi.commands.rewrite.impl;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.rewrite.PlaceholderCommand;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public final class CommandExpansionRegister extends PlaceholderCommand
{

	public CommandExpansionRegister()
	{
		super("register");
	}

	@Override
	public void evaluate(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, @NotNull final String alias, @NotNull final List<String> params)
	{
		if (params.size() < 1)
		{
			Msg.msg(sender,
					"&cYou must specify the name of an expansion file.");
			return;
		}

		final PlaceholderExpansion expansion = plugin.getExpansionManager().registerExpansion(params.get(0));
		if (expansion == null)
		{
			Msg.msg(sender,
					"&cFailed to register expansion from &f" + params.get(0));
			return;
		}

		Msg.msg(sender,
				"&aSuccessfully registered expansion: &f" + expansion.getName());
	}

	@Override
	public void complete(final @NotNull PlaceholderAPIPlugin plugin, final @NotNull CommandSender sender, final @NotNull String alias, final @NotNull @Unmodifiable List<String> params, final @NotNull List<String> suggestions)
	{
		final String[] files = plugin.getExpansionManager().getFolder().list((dir, name) -> name.endsWith(".jar"));
		if (files == null || files.length == 0)
		{
			return;
		}

		final Stream<String> stream = Arrays.stream(files);

		switch (params.size())
		{
			case 0:
				stream.forEach(suggestions::add);
				break;
			case 1:
				stream.filter(file -> file.startsWith(params.get(0))).forEach(suggestions::add);
				break;
		}
	}

}
