package me.clip.placeholderapi.commands.impl.cloud;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public final class CommandECloudRefresh extends PlaceholderCommand
{

	public CommandECloudRefresh()
	{
		super("refresh");
	}

	@Override
	public void evaluate(final @NotNull PlaceholderAPIPlugin plugin, final @NotNull CommandSender sender, final @NotNull String alias, final @NotNull @Unmodifiable List<String> params)
	{
		plugin.getExpansionCloud().clean();
		plugin.getExpansionCloud().fetch(plugin.getPlaceholderAPIConfig().cloudAllowUnverifiedExpansions());

		Msg.msg(sender,
				"&aThe ECloud Manager has been refreshed!");
	}

}