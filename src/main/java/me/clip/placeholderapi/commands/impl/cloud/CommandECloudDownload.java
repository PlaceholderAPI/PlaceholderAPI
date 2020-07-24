package me.clip.placeholderapi.commands.impl.cloud;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public final class CommandECloudDownload extends PlaceholderCommand
{

	public CommandECloudDownload()
	{
		super("download");
	}

	@Override
	public void evaluate(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, @NotNull final String alias, @NotNull @Unmodifiable final List<String> params)
	{
		if (params.isEmpty())
		{
			Msg.msg(sender,
					"&cYou must supply the name of a cloud expansion.");
			return;
		}

		final CloudExpansion expansion = plugin.getExpansionCloud().getCloudExpansion(params.get(0)).orElse(null);
		if (expansion == null)
		{
			Msg.msg(sender,
					"&cCould not find expansion named: &f" + params.get(0));
			return;
		}

		final CloudExpansion.Version version;
		if (params.size() < 2)
		{
			version = expansion.getVersion(expansion.getLatestVersion());
			if (version == null)
			{
				Msg.msg(sender,
						"&cCould not find latest version for expansion.");
				return;
			}
		}
		else
		{
			version = expansion.getVersion(params.get(1));
			if (version == null)
			{
				Msg.msg(sender,
						"&cCould not find specified version: &f" + params.get(1),
						"&7Versions: &a" + expansion.getAvailableVersions());
				return;
			}
		}

		plugin.getExpansionCloud().downloadExpansion(expansion, version).whenComplete((file, exception) -> {
			if (exception != null)
			{
				Msg.msg(sender,
						"&cFailed to download expansion: &e" + exception.getMessage());
				return;
			}

			Msg.msg(sender,
					"&aSuccessfully downloaded expansion to file: &e" + file.getName());

			plugin.getExpansionCloud().clean();
			plugin.getExpansionCloud().fetch(plugin.getPlaceholderAPIConfig().cloudAllowUnverifiedExpansions());
		});
	}

	@Override
	public void complete(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, @NotNull final String alias, @NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions)
	{
		if (params.size() > 2)
		{
			return;
		}

		if (params.size() <= 1)
		{
			final Stream<String> names = plugin.getExpansionCloud().getCloudExpansions().values().stream().map(CloudExpansion::getName).map(name -> name.replace(' ', '_'));
			suggestByParameter(names, suggestions, params.isEmpty() ? null : params.get(0));
			return;
		}

		final Optional<CloudExpansion> expansion = plugin.getExpansionCloud().getCloudExpansion(params.get(0));
		if (!expansion.isPresent())
		{
			return;
		}

		suggestByParameter(expansion.get().getAvailableVersions().stream(), suggestions, params.get(1));
	}

}
