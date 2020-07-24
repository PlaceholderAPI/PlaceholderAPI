package me.clip.placeholderapi.commands.impl.local;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Arrays;
import java.util.List;

public final class CommandExpansionRegister extends PlaceholderCommand
{

	public CommandExpansionRegister()
	{
		super("register");
	}

	@Override
	public void evaluate(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, @NotNull final String alias, @NotNull @Unmodifiable final List<String> params)
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
	public void complete(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, @NotNull final String alias, @NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions)
	{
		if (params.size() > 1)
		{
			return;
		}

		final String[] fileNames = plugin.getExpansionManager().getFolder().list((dir, name) -> name.endsWith(".jar"));
		if (fileNames == null || fileNames.length == 0)
		{
			return;
		}

		suggestByParameter(Arrays.stream(fileNames), suggestions, params.isEmpty() ? null : params.get(0));
	}

}
