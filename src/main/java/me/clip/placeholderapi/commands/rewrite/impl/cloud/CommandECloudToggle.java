package me.clip.placeholderapi.commands.rewrite.impl.cloud;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.rewrite.PlaceholderCommand;
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
	public void evaluate(final @NotNull PlaceholderAPIPlugin plugin, final @NotNull CommandSender sender, final @NotNull String alias, final @NotNull @Unmodifiable List<String> params)
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
					"&7The ECloud Manager is already " + (desiredState ? "enabled" : "disabled"));
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
				"&aThe ECloud Manager has been " + (desiredState ? "enabled" : "disabled"));
	}

}
