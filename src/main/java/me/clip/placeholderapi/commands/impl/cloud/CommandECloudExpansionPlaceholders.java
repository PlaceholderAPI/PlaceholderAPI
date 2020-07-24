package me.clip.placeholderapi.commands.impl.cloud;

import com.google.common.collect.Lists;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CommandECloudExpansionPlaceholders extends PlaceholderCommand
{

	public CommandECloudExpansionPlaceholders()
	{
		super("placeholders");
	}

	@Override
	public void evaluate(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, @NotNull final String alias, @NotNull @Unmodifiable final List<String> params)
	{
		if (params.isEmpty())
		{
			Msg.msg(sender,
					"&cYou must specify the name of the expansion.");
			return;
		}

		final CloudExpansion expansion = plugin.getCloudExpansionManager().findCloudExpansionByName(params.get(0)).orElse(null);
		if (expansion == null)
		{
			Msg.msg(sender,
					"&cThere is no expansion with the name: &f" + params.get(0));
			return;
		}

		final List<String> placeholders = expansion.getPlaceholders();
		if (placeholders == null || placeholders.isEmpty())
		{
			Msg.msg(sender,
					"&cThat expansion does not have placeholders listed.");
			return;
		}

		final List<List<String>> partitions = Lists.partition(placeholders.stream().sorted().collect(Collectors.toList()), 10);

		Msg.msg(sender,
				"&6" + placeholders.size() + "&7 placeholders: &a",
				partitions.stream().map(partition -> "  " + String.join(", ", partition)).collect(Collectors.joining("\n")));

	}

	@Override
	public void complete(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, @NotNull final String alias, @NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions)
	{
		if (params.size() > 1)
		{
			return;
		}

		final Stream<String> names = plugin.getCloudExpansionManager()
										   .getCloudExpansions()
										   .values()
										   .stream()
										   .map(CloudExpansion::getName)
										   .map(name -> name.replace(' ', '_'));

		suggestByParameter(names, suggestions, params.isEmpty() ? null : params.get(0));
	}

}
