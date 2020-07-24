/*
 *
 * PlaceholderAPI
 * Copyright (C) 2019 Ryan McCarthy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */
package me.clip.placeholderapi.expansion.cloud;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Collectors;

public final class ExpansionCloudManager
{

	private static final String API_URL = "http://api.extendedclip.com/v2/";

	private static final Gson GSON = new Gson();
	private static final Type TYPE = new TypeToken<Map<String, CloudExpansion>>() {}.getType();


	@NotNull
	private final File                 folder;
	@NotNull
	private final PlaceholderAPIPlugin plugin;


	@NotNull
	private final Map<String, CloudExpansion>                  expansions  = new TreeMap<>();
	@NotNull
	private final Map<CloudExpansion, CompletableFuture<File>> downloading = new HashMap<>();


	public ExpansionCloudManager(@NotNull final PlaceholderAPIPlugin plugin)
	{
		this.plugin = plugin;
		this.folder = new File(plugin.getDataFolder(), "expansions");

		if (!this.folder.exists() && !this.folder.mkdirs())
		{
			plugin.getLogger().severe("Failed to create expansions directory!");
		}
	}


	@NotNull
	@Unmodifiable
	public Map<String, CloudExpansion> getCloudExpansions()
	{
		return ImmutableMap.copyOf(expansions);
	}

	@NotNull
	@Unmodifiable
	public Set<String> getCloudAuthorNames()
	{
		return ImmutableSet.copyOf(expansions.values().stream().map(CloudExpansion::getAuthor).collect(Collectors.toSet()));
	}

	public int getCloudAuthorCount()
	{
		return expansions.values()
						 .stream()
						 .collect(Collectors.groupingBy(CloudExpansion::getAuthor, Collectors.counting()))
						 .size();
	}

	@NotNull
	public Optional<CloudExpansion> getCloudExpansion(String name)
	{
		return expansions.values()
						 .stream()
						 .filter(ex -> ex.getName().replace(' ', '_').equalsIgnoreCase(name.replace(' ', '_')))
						 .findFirst();
	}


	public int getCloudUpdateCount()
	{
		return ((int) PlaceholderAPI.getExpansions()
									.stream()
									.filter(ex -> getCloudExpansion(ex.getName()).map(CloudExpansion::shouldUpdate).isPresent())
									.count());
	}

	@NotNull
	@Unmodifiable
	public Map<String, CloudExpansion> getAllByAuthor(@NotNull final String author)
	{
		if (expansions.isEmpty())
		{
			return Collections.emptyMap();
		}

		return expansions.values()
						 .stream()
						 .filter(expansion -> author.equalsIgnoreCase(expansion.getAuthor()))
						 .collect(Collectors.toMap(CloudExpansion::getName, Function.identity()));
	}

	@NotNull
	@Unmodifiable
	public Map<String, CloudExpansion> getAllInstalled()
	{
		if (expansions.isEmpty())
		{
			return Collections.emptyMap();
		}

		return expansions.values()
						 .stream()
						 .filter(CloudExpansion::hasExpansion)
						 .collect(Collectors.toMap(CloudExpansion::getName, Function.identity()));
	}


	public void clean()
	{
		expansions.clear();

		downloading.values().forEach(future -> future.cancel(true));
		downloading.clear();
	}

	@NotNull
	public CompletableFuture<Map<String, CloudExpansion>> fetch(boolean allowUnverified)
	{
		plugin.getLogger().info("Fetching available expansion information...");

		CompletableFuture<Map<String, CloudExpansion>> future = CompletableFuture.supplyAsync(() -> {
			final Map<String, CloudExpansion> values = new HashMap<>();

			try
			{
				//noinspection UnstableApiUsage
				final String json = Resources.toString(new URL(API_URL), StandardCharsets.UTF_8);
				values.putAll(GSON.fromJson(json, TYPE));
			}
			catch (final IOException ex)
			{
				throw new CompletionException(ex);
			}

			values.values().removeIf(value -> value.getLatestVersion() == null || value.getVersion(value.getLatestVersion()) == null);

			return values;
		});


		if (!allowUnverified)
		{
			future = future.thenApplyAsync((values) -> {
				values.values().removeIf(expansion -> !expansion.isVerified());
				return values;
			});
		}


		future = future.thenApplyAsync((values) -> {

			values.forEach((name, expansion) -> {
				expansion.setName(name);

				final PlaceholderExpansion local = plugin.getExpansionManager().getRegisteredExpansion(name);
				if (local != null && local.isRegistered())
				{
					expansion.setHasExpansion(true);
					expansion.setShouldUpdate(!local.getVersion().equals(expansion.getLatestVersion()));
				}
			});

			return values;
		});

		future.whenComplete((expansions, exception) -> {

			if (exception != null)
			{
				plugin.getLogger().log(Level.WARNING, "failed to download expansion information", exception);
				return;
			}

			this.expansions.putAll(expansions);
		});

		return future;
	}


	public boolean isDownloading(@NotNull final CloudExpansion expansion)
	{
		return downloading.containsKey(expansion);
	}


	@NotNull
	public CompletableFuture<@NotNull File> downloadExpansion(@NotNull final CloudExpansion expansion, @NotNull final CloudExpansion.Version version)
	{
		final CompletableFuture<File> previous = downloading.get(expansion);
		if (previous != null)
		{
			return previous;
		}

		final File file = new File(folder, "Expansion-" + expansion.getName() + ".jar");

		final CompletableFuture<File> download = CompletableFuture.supplyAsync(() -> {

			try (final ReadableByteChannel source = Channels.newChannel(new URL(version.getUrl()).openStream()); final FileOutputStream target = new FileOutputStream(file))
			{
				target.getChannel().transferFrom(source, 0, Long.MAX_VALUE);
			}
			catch (final IOException ex)
			{
				throw new CompletionException(ex);
			}

			return file;
		});

		download.whenCompleteAsync((value, exception) -> {
			downloading.remove(expansion);

			if (exception != null)
			{
				plugin.getLogger().log(Level.SEVERE, "failed to download " + expansion.getName() + ":" + version.getVersion(), exception);
			}
		});

		downloading.put(expansion, download);

		return download;
	}

}
