package me.clip.placeholderapi.commands.impl.local;

import com.google.common.collect.Lists;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class CommandList extends PlaceholderCommand
{

	public CommandList()
	{
		super("list");
	}


	@Override
	public void evaluate(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, @NotNull final String alias, @NotNull @Unmodifiable final List<String> params)
	{
		final Set<String> identifiers = PlaceholderAPI.getRegisteredIdentifiers();
		if (identifiers.isEmpty())
		{
			Msg.msg(sender, "&6There are no placeholder hooks active!");
			return;
		}

		final List<List<String>> partitions = Lists.partition(identifiers.stream().sorted().collect(Collectors.toList()), 10);

		Msg.msg(sender,
				"&6" + identifiers.size() + "&7 placeholder hook(s) active: &a",
				partitions.stream().map(partition -> "  " + String.join(", ", partition)).collect(Collectors.joining("\n")));
	}

}
