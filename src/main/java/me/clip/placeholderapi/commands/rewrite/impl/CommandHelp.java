package me.clip.placeholderapi.commands.rewrite.impl;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.rewrite.PlaceholderCommand;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class CommandHelp extends PlaceholderCommand
{

	public CommandHelp()
	{
		super("help");
	}


	@Override
	public void evaluate(final @NotNull PlaceholderAPIPlugin plugin, final @NotNull CommandSender sender, final @NotNull String alias, final @NotNull List<String> params)
	{
		final PluginDescriptionFile description = plugin.getDescription();

		Msg.msg(sender,
				"&b&lPlaceholderAPI &aHelp &e(&f" + description.getVersion() + "&e)",
				"&b/papi",
				"  &fView plugin info/version",
				"&b/papi &7list",
				"  &fList active expansions",
				"&b/papi &7info &a{placeholder name}",
				"  &fView information for a specific expansion",
				"&b/papi &7parse &a{me/player name} &9{message}",
				"  &fParse a message with placeholders",
				"&b/papi &7bcparse &a{me/player name} &9{message}",
				"  &fParse a message with placeholders and broadcast it",
				"&b/papi &7parserel &a{player one} {player two} &9{message}",
				"  &fParse a message with relational placeholders",
				"&b/papi &7register &a{file name}",
				"  &fRegister an expansion by the name of the file",
				"&b/papi &7unregister &a{expansion name}",
				"  &fUnregister an expansion by name",
				"&b/papi &7reload",
				"  &fReload the config of PAPI");
	}

}
