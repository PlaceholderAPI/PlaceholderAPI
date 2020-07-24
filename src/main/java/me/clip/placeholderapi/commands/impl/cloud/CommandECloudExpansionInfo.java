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

public final class CommandECloudExpansionInfo extends PlaceholderCommand
{

	public CommandECloudExpansionInfo()
	{
		super("info");
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

		final CloudExpansion expansion = plugin.getExpansionCloud().getCloudExpansion(params.get(0)).orElse(null);
		if (expansion == null)
		{
			Msg.msg(sender,
					"&cThere is no expansion with the name: &f" + params.get(0));
			return;
		}

		final StringBuilder builder = new StringBuilder();

		builder.append("&bExpansion: &f")
			   .append(expansion.shouldUpdate() ? "&e" : "&a")
			   .append(expansion.getName())
			   .append('\n')
			   .append("&bAuthor: &f")
			   .append(expansion.getAuthor())
			   .append('\n')
			   .append("&bVerified: ")
			   .append(expansion.isVerified() ? "&a&l✔" : "&c&l❌")
			   .append('\n');

		if (params.size() < 2)
		{
			builder.append("&bLatest Version: &f")
				   .append(expansion.getLatestVersion())
				   .append('\n')
				   .append("&bReleased: &f")
				   .append(expansion.getTimeSinceLastUpdate())
				   .append(" ago")
				   .append('\n')
				   .append("&bRelease Notes: &f")
				   .append(expansion.getVersion().getReleaseNotes())
				   .append('\n');
		}
		else
		{
			final CloudExpansion.Version version = expansion.getVersion(params.get(1));
			if (version == null)
			{
				Msg.msg(sender,
						"&cCould not find specified version: &f" + params.get(1),
						"&7Versions: &a" + expansion.getAvailableVersions());
				return;
			}

			builder.append("&bVersion: &f")
				   .append(version.getVersion())
				   .append('\n')
				   .append("&bRelease Notes: &f")
				   .append(version.getReleaseNotes())
				   .append('\n')
				   .append("&bDownload URL: &f")
				   .append(version.getUrl())
				   .append('\n');
		}

		Msg.msg(sender, builder.toString());
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
