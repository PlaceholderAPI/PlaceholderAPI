package me.clip.placeholderapi.commands.impl.cloud;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public final class CommandECloudToggle extends PlaceholderCommand
{

	public CommandECloudToggle()
	{
		super("toggle", "enable", "disable");
	}


	@Override
	public void evaluate(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, @NotNull final String alias, @NotNull @Unmodifiable final List<String> params)
	{
		final boolean desiredState;
		final boolean currentState = plugin.getPlaceholderAPIConfig().isCloudEnabled();

		switch (alias.toLowerCase())
		{
			case "enable":
				desiredState = true;
				break;
			case "disable":
				desiredState = false;
				break;
			default:
				desiredState = !currentState;
				break;
		}

		if (desiredState == currentState)
		{
			Msg.msg(sender,
					"&7The eCloud Manager is already " + (desiredState ? "enabled" : "disabled"));
			return;
		}

		plugin.getPlaceholderAPIConfig().setCloudEnabled(desiredState);

		if (desiredState)
		{
			plugin.enableCloud();
		}
		else
		{
			plugin.disableCloud();
		}

		Msg.msg(sender,
				"&aThe eCloud Manager has been " + (desiredState ? "enabled" : "disabled"));
	}

}
