package me.clip.placeholderapi.commands.impl.cloud;

import com.google.common.collect.Lists;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * please don't flame me for this code, I will fix this shit later.
 */
public final class CommandECloudUpdate extends PlaceholderCommand
{

	public CommandECloudUpdate()
	{
		super("update");
	}


	@Override
	public void evaluate(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, @NotNull final String alias, @NotNull @Unmodifiable final List<String> params)
	{
		if (params.isEmpty())
		{
			Msg.msg(sender,
					"&cYou must define 'all' or the name of an expansion to update.");
			return;
		}

		final boolean              multiple   = params.get(0).equalsIgnoreCase("all");
		final List<CloudExpansion> expansions = new ArrayList<>();

		// gather target expansions
		if (multiple)
		{
			expansions.addAll(plugin.getCloudExpansionManager().getCloudExpansionsInstalled().values());
		}
		else
		{
			plugin.getCloudExpansionManager().findCloudExpansionByName(params.get(0)).ifPresent(expansions::add);
		}

		// remove the ones that are the latest version
		expansions.removeIf(expansion -> !expansion.shouldUpdate());

		if (expansions.isEmpty())
		{
			Msg.msg(sender,
					"&cNo updates available for " + (!multiple ? "this expansion." : "your active expansions."));
			return;
		}

		Msg.msg(sender,
				"&aUpdating expansions: " + expansions.stream().map(CloudExpansion::getName).collect(Collectors.joining("&7, &6", "&8[&6", "&8]&r")));

		downloadExpansions(plugin, expansions)
				.thenCompose(files -> discoverExpansions(plugin, files))
				.whenComplete((classes, exception) -> {
					if (exception != null)
					{
						Msg.msg(sender,
								"&cFailed to update expansions: &e" + exception.getMessage());
						return;
					}

					Msg.msg(sender,
							"&aSuccessfully downloaded updates, registering new versions.");

					Bukkit.getScheduler().runTask(plugin, () -> {
						final String message = classes.stream()
													  .map(plugin.getLocalExpansionManager()::register)
													  .filter(Optional::isPresent)
													  .map(Optional::get)
													  .map(expansion -> "  &a" + expansion.getName() + " &f" + expansion.getVersion())
													  .collect(Collectors.joining("\n"));
						Msg.msg(sender,
								"&7Registered expansions:",
								message);
					});
				});
	}

	@Override
	public void complete(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final CommandSender sender, @NotNull final String alias, @NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions)
	{
		if (params.size() > 1)
		{
			return;
		}

		final List<CloudExpansion> installed = Lists.newArrayList(plugin.getCloudExpansionManager().getCloudExpansionsInstalled().values());
		installed.removeIf(expansion -> !expansion.shouldUpdate());

		if (!installed.isEmpty() && (params.isEmpty() || "all".startsWith(params.get(0).toLowerCase())))
		{
			suggestions.add("all");
		}

		suggestByParameter(installed.stream().map(CloudExpansion::getName).map(name -> name.replace(" ", "_")), suggestions, params.isEmpty() ? null : params.get(0));
	}


	public static CompletableFuture<List<File>> downloadExpansions(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final List<CloudExpansion> expansions)
	{
		final List<CompletableFuture<File>> futures = expansions.stream()
																.map(expansion -> plugin.getCloudExpansionManager().downloadExpansion(expansion, expansion.getVersion()))
																.collect(Collectors.toList());

		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).thenApplyAsync(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
	}

	public static CompletableFuture<List<Class<? extends PlaceholderExpansion>>> discoverExpansions(@NotNull final PlaceholderAPIPlugin plugin, @NotNull final List<File> files)
	{
		final List<CompletableFuture<List<Class<? extends PlaceholderExpansion>>>> futures = files.stream()
																								  .map(file -> plugin.getLocalExpansionManager().findExpansionsInFile(file))
																								  .collect(Collectors.toList());

		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).thenApplyAsync(v -> futures.stream().map(CompletableFuture::join).flatMap(Collection::stream).collect(Collectors.toList()));
	}

}
