package me.clip.placeholderapi.commands.rewrite.impl;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.rewrite.PlaceholderCommand;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class CommandVersion extends PlaceholderCommand
{

	public CommandVersion()
	{
		super("version");
	}


	@Override
	public void evaluate(final @NotNull PlaceholderAPIPlugin plugin, final @NotNull CommandSender sender, final @NotNull String alias, final @NotNull List<String> params)
	{
		final PluginDescriptionFile description = plugin.getDescription();

		Msg.msg(sender,
				"&b&lPlaceholderAPI &e(&f" + description.getVersion() + "&e)",
				"&fAuthors&8: &6" + description.getAuthors(),
				"&fPAPI Commands&8: &b/papi &7help",
				"&fECloud Commands&8: &b/papi &7ecloud");
	}

}
