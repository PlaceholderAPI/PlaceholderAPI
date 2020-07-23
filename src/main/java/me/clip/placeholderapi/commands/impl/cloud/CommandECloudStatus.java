package me.clip.placeholderapi.commands.impl.cloud;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.expansion.cloud.ExpansionCloudManager;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public final class CommandECloudStatus extends PlaceholderCommand
{

	public CommandECloudStatus()
	{
		super("status");
	}

	@Override
	public void evaluate(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, @NotNull final String alias, @NotNull @Unmodifiable final List<String> params)
	{
		final ExpansionCloudManager manager = plugin.getExpansionCloud();

		final int updateCount    = manager.getCloudUpdateCount();
		final int authorCount    = manager.getCloudAuthorCount();
		final int expansionCount = manager.getCloudExpansions().size();

		final StringBuilder builder = new StringBuilder();

		builder.append("&bThere are &a").append(expansionCount).append("&b expansions available on the cloud.").append('\n');
		builder.append("&7A total of &f").append(authorCount).append("&7 authors have contributed to the expansion cloud.").append('\n');

		if (updateCount > 0)
		{
			builder.append("&eYou have &a").append(updateCount).append("&e expansions installed that have updates available.");
		}

		Msg.msg(sender,builder.toString());
	}

}
