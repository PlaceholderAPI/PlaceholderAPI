package me.clip.placeholderapi.commands.impl.cloud;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.util.Msg;
import me.rayzr522.jsonmessage.JSONMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class CommandECloudExpansionList extends PlaceholderCommand
{

	private static final int PAGE_SIZE = 10;


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

		final List<CloudExpansion> expansions = Lists.newArrayList(getExpansions(params.get(0), plugin));

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

		expansions.sort(Comparator.comparing(CloudExpansion::getLastUpdate).reversed());

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


		if (sender instanceof Player)
		{
			Msg.msg(sender, builder.toString());

			final int limit = (int) Math.ceil((double) expansions.size() / PAGE_SIZE);

			final JSONMessage message = getMessage(values, page);

			if (limit > 1)
			{
				message.newline();

				message.then("◀")
					   .color(page <= 1 ? ChatColor.GRAY : ChatColor.DARK_GRAY);
				if (page > 1)
				{
					message.runCommand("/papi ecloud list " + params.get(0) + " " + (page - 1));
				}

				message.then(" " + page + " ").color(ChatColor.GREEN);

				message.then("▶")
					   .color(page >= limit ? ChatColor.GRAY : ChatColor.DARK_GRAY);
				if (page < limit)
				{
					message.runCommand("/papi ecloud list " + params.get(0) + " " + (page + 1));
				}
			}

			message.send(((Player) sender));
		}
		else
		{
			int index = ((page - 1) * PAGE_SIZE) + 1;
			for (final CloudExpansion expansion : values)
			{
				builder.append("\n")
				           .append("&8")
					   .append(index++)
					   .append(". ")
					   .append((expansion.shouldUpdate() ? "&6" : expansion.hasExpansion() ? "&a" : "&7"))
					   .append(expansion.getName())
					   .append(" &8[&bAuthor: &f")
					   .append(expansion.getAuthor())
					   .append("&8, ")
					   .append("&bVerified: ")
					   .append(expansion.isVerified() ? "&a&l✔&r" : "&c&l❌&r")
					   .append("&8, ")
					   .append("&bLatest Version: &f")
					   .append(expansion.getLatestVersion())
					   .append("&8]");
			}

			Msg.msg(sender, builder.toString());
		}

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

		suggestByParameter(IntStream.rangeClosed(1, (int) Math.ceil((double) getExpansions(params.get(0), plugin).size() / PAGE_SIZE)).mapToObj(Objects::toString), suggestions, params.get(1));
	}


	@NotNull
	private static List<CloudExpansion> getPage(@NotNull final List<CloudExpansion> expansions, final int page, final int pageSize)
	{
		if (expansions.isEmpty())
		{
			return Collections.emptyList();
		}

		final int head = (page * pageSize);
		final int tail = Math.min(expansions.size(), head + pageSize);

		if (expansions.size() < head)
		{
			return Collections.emptyList();
		}

		return IntStream.range(head, tail).mapToObj(expansions::get).filter(Objects::nonNull).collect(Collectors.toList());
	}

	@NotNull
	private static Collection<CloudExpansion> getExpansions(@NotNull final String target, @NotNull final PlaceholderAPIPlugin plugin)
	{
		switch (target.toLowerCase())
		{
			case "all":
				return plugin.getExpansionCloud().getCloudExpansions().values();
			case "installed":
				return plugin.getExpansionCloud().getAllInstalled().values();
			default:
				return plugin.getExpansionCloud().getAllByAuthor(target).values();
		}
	}

	@NotNull
	private static JSONMessage getMessage(@NotNull final List<CloudExpansion> expansions, final int page)
	{
		final SimpleDateFormat format = PlaceholderAPIPlugin.getDateFormat();

		final StringBuilder tooltip = new StringBuilder();
		final JSONMessage   message = JSONMessage.create();

		int index = ((page - 1) * PAGE_SIZE) + 1;
		for (int i = 0; i < expansions.size(); i++)
		{
			final CloudExpansion expansion = expansions.get(i);
			tooltip.append("&bClick to download this expansion!")
				   .append('\n')
				   .append('\n')
				   .append("&bAuthor: &f")
				   .append(expansion.getAuthor())
				   .append('\n')
				   .append("&bVerified: ")
				   .append(expansion.isVerified() ? "&a&l✔&r" : "&c&l❌&r")
				   .append('\n')
				   .append("&bLatest Version: &f")
				   .append(expansion.getLatestVersion())
				   .append('\n')
				   .append("&bReleased: &f")
				   .append(format.format(expansion.getLastUpdate()));

			final String description = expansion.getDescription();
			if (description != null && !description.isEmpty())
			{
				tooltip.append('\n')
					   .append('\n')
					   .append("&f")
					   .append(description.replace("\r", "").trim());
			}

			message.then(Msg.color("&8" + (index++) + ".&r " + (expansion.shouldUpdate() ? "&6" : expansion.hasExpansion() ? "&a" : "&7") + expansion.getName()));

			message.tooltip(Msg.color(tooltip.toString()));
			message.suggestCommand("/papi ecloud download " + expansion.getName());

			if (i < expansions.size() - 1)
			{
				message.newline();
			}

			tooltip.setLength(0);
		}

		return message;
	}

}
