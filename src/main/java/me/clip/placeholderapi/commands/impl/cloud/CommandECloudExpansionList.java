package me.clip.placeholderapi.commands.impl.cloud;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class CommandECloudExpansionList extends PlaceholderCommand
{

	private static final int PAGE_SIZE = 3;


	@Unmodifiable
	private static final Set<String> OPTIONS = ImmutableSet.of("all", "installed");


	public CommandECloudExpansionList()
	{
		super("list");
	}


	@Override
	public void evaluate(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, @NotNull final String alias, @NotNull @Unmodifiable final List<String> params)
	{
		if (params.isEmpty())
		{
			Msg.msg(sender,
					"&cYou must specify an option. [all, {author}, installed]");
			return;
		}


		@Unmodifiable final Map<Integer, CloudExpansion> expansions = getExpansions(params.get(0), plugin);

		final int page;

		if (params.size() < 2)
		{
			page = 1;
		}
		else
		{
			//noinspection UnstableApiUsage
			final Integer parsed = Ints.tryParse(params.get(1));
			if (parsed == null)
			{
				Msg.msg(sender,
						"&cPage number must be an integer.");
				return;
			}

			final int limit = (int) Math.ceil((double) expansions.size() / PAGE_SIZE);

			if (parsed < 1 || parsed > limit)
			{
				Msg.msg(sender,
						"&cPage number must be in the range &8[&a1&7..&a" + limit + "&8]");
				return;
			}

			page = parsed;
		}


		final StringBuilder        builder = new StringBuilder();
		final List<CloudExpansion> values  = getPage(expansions, page - 1, PAGE_SIZE);


		switch (params.get(0).toLowerCase())
		{
			case "all":
				builder.append("&bAll Expansions");
				break;
			case "installed":
				builder.append("&bInstalled Expansions");
				break;
			default:
				builder.append("&bExpansions by &6")
					   .append(params.get(0));
				break;
		}

		builder.append(" &bPage&7: &a")
			   .append(page)
			   .append('\n');

		int index = ((page - 1) * PAGE_SIZE) + 1;
		for (final CloudExpansion expansion : values)
		{
			builder.append("&8")
				   .append(index++)
				   .append(". ")
				   .append(expansion.shouldUpdate() ? "&e" : "&a")
				   .append(expansion.getName())
				   .append('\n')
				   .append("    &bAuthor: &f")
				   .append(expansion.getAuthor())
				   .append('\n')
				   .append("    &bVerified: ")
				   .append(expansion.isVerified() ? "&a&l✔&r" : "&c&l❌&r")
				   .append('\n')
				   .append("    &bLatest Version: &f")
				   .append(expansion.getLatestVersion())
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
			suggestByParameter(Sets.union(OPTIONS, plugin.getExpansionCloud().getCloudAuthorNames()).stream(), suggestions, params.isEmpty() ? null : params.get(0));
			return;
		}

		final Map<Integer, CloudExpansion> expansions = getExpansions(params.get(0), plugin);

		suggestByParameter(IntStream.rangeClosed(1, (int) Math.ceil((double) expansions.size() / PAGE_SIZE)).mapToObj(Objects::toString), suggestions, params.get(1));
	}


	@NotNull
	private static List<CloudExpansion> getPage(@NotNull final Map<Integer, CloudExpansion> expansions, final int page, final int pageSize)
	{
		if (expansions.isEmpty())
		{
			return Collections.emptyList();
		}

		final int head = (page * pageSize);
		final int tail = (head + pageSize);

		return IntStream.range(head, tail).mapToObj(expansions::get).filter(Objects::nonNull).collect(Collectors.toList());
	}

	@NotNull
	private static Map<Integer, CloudExpansion> getExpansions(@NotNull final String target, @NotNull final PlaceholderAPIPlugin plugin)
	{
		switch (target.toLowerCase())
		{
			case "all":
				return plugin.getExpansionCloud().getCloudExpansions();
			case "installed":
				return plugin.getExpansionCloud().getAllInstalled();
			default:
				return plugin.getExpansionCloud().getAllByAuthor(target);
		}
	}

}
