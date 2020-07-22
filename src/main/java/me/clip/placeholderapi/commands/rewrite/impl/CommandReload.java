package me.clip.placeholderapi.commands.rewrite.impl;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.rewrite.PlaceholderCommand;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class CommandReload extends PlaceholderCommand
{

	public CommandReload()
	{
		super("reload");
	}

	@Override
	public void evaluate(final @NotNull PlaceholderAPIPlugin plugin, final @NotNull CommandSender sender, final @NotNull String alias, final @NotNull List<String> params)
	{
		plugin.reloadConf(sender);
		Msg.msg(sender, "&fPlaceholder&7API &bconfiguration reloaded!");
	}

}
