package me.clip.placeholderapi.commands.rewrite.impl.cloud;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.rewrite.PlaceholderCommand;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class CommandECloud extends PlaceholderCommand
{

	@Unmodifiable
	private static final List<PlaceholderCommand> COMMANDS = ImmutableList.of(new CommandECloudEnable(),
																			  new CommandECloudDisable());


	@NotNull
	@Unmodifiable
	private final Map<String, PlaceholderCommand> commands;


	public CommandECloud()
	{
		super("ecloud");

		final ImmutableMap.Builder<String, PlaceholderCommand> commands = ImmutableMap.builder();

		for (final PlaceholderCommand command : COMMANDS)
		{
			command.getLabels().forEach(label -> commands.put(label, command));
		}

		this.commands = commands.build();
	}


	@Override
	public void evaluate(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, @NotNull final String alias, @NotNull final @Unmodifiable List<String> params)
	{
		if (params.isEmpty())
		{
			return; // send help message
		}

		final String             search = params.get(0).toLowerCase();
		final PlaceholderCommand target = commands.get(search);

		if (target == null)
		{
			Msg.msg(sender, "&cUnknown command &7ecloud " + search);
			return;
		}

		final String permission = target.getPermission();
		if (permission != null && !permission.isEmpty() && !sender.hasPermission(permission))
		{
			Msg.msg(sender, "&cYou do not have permission to do this!");
			return;
		}

		target.evaluate(plugin, sender, search, params.subList(1, params.size()));
	}

	@Override
	public void complete(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, @NotNull final String alias, @NotNull final @Unmodifiable List<String> params, @NotNull final List<String> suggestions)
	{
		if (params.size() <= 1)
		{
			final Stream<String> targets = filterByPermission(sender, commands.values().stream()).map(PlaceholderCommand::getLabels).flatMap(Collection::stream);
			suggestByParameter(targets, suggestions, params.isEmpty() ? null : params.get(0));

			return; // send sub commands
		}

		final String             search = params.get(0).toLowerCase();
		final PlaceholderCommand target = commands.get(search);

		if (target == null)
		{
			return;
		}

		target.complete(plugin, sender, search, params.subList(1, params.size()), suggestions);
	}

}
