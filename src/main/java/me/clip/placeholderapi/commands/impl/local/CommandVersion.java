package me.clip.placeholderapi.commands.impl.local;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public final class CommandVersion extends PlaceholderCommand
{

	public CommandVersion()
	{
		super("version");
	}


	@Override
	public void evaluate(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, @NotNull final String alias, @NotNull @Unmodifiable final List<String> params)
	{
		final PluginDescriptionFile description = plugin.getDescription();

		Msg.msg(sender,
				"&b&lPlaceholderAPI &e(&f" + description.getVersion() + "&e)",
				"&fAuthors&8: &6" + description.getAuthors(),
				"&fPAPI Commands&8: &b/papi &7help",
				"&feCloud Commands&8: &b/papi &7ecloud");
	}

}
