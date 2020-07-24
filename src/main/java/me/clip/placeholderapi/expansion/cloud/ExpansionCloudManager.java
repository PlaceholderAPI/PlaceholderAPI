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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Collectors;

public final class ExpansionCloudManager
{

	private static final String API_URL = "http://api.extendedclip.com/v2/";
	private static final Gson   GSON    = new Gson();


	@NotNull
	private final File                 folder;
	@NotNull
	private final PlaceholderAPIPlugin plugin;


	@NotNull
	private final Map<Integer, CloudExpansion>                 expansions  = new TreeMap<>();
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
	public Map<Integer, CloudExpansion> getCloudExpansions()
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
	public Map<Integer, CloudExpansion> getAllByAuthor(@NotNull final String author)
	{
		if (expansions.isEmpty())
		{
			return Collections.emptyMap();
		}

		final AtomicInteger index = new AtomicInteger();

		return expansions.values()
						 .stream()
						 .filter(expansion -> author.equalsIgnoreCase(expansion.getAuthor()))
						 .collect(Collectors.toMap(($) -> index.incrementAndGet(), Function.identity()));
	}

	@NotNull
	@Unmodifiable
	public Map<Integer, CloudExpansion> getAllInstalled()
	{
		if (expansions.isEmpty())
		{
			return Collections.emptyMap();
		}

		final AtomicInteger index = new AtomicInteger();

		return expansions.values()
						 .stream()
						 .filter(CloudExpansion::hasExpansion)
						 .collect(Collectors.toMap(($) -> index.incrementAndGet(), Function.identity()));
	}


	public void clean()
	{
		expansions.clear();

		downloading.values().forEach(future -> future.cancel(true));
		downloading.clear();
	}

	public void fetch(boolean allowUnverified)
	{
		plugin.getLogger().info("Fetching available expansion information...");

		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
			final Map<String, CloudExpansion> data = new HashMap<>();

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(API_URL).openStream())))
			{
				data.putAll(GSON.fromJson(reader, new TypeToken<Map<String, CloudExpansion>>()
				{
				}.getType()));
			}
			catch (Exception ex)
			{
				if (plugin.getPlaceholderAPIConfig().isDebugMode())
				{
					ex.printStackTrace();
				}
				else
				{
					plugin.getLogger().warning("Unable to fetch expansions!\nThere was an error with the server host connecting to the PlaceholderAPI eCloud (https://api.extendedclip.com/v2/)");
				}
			}

			final List<CloudExpansion> unsorted = new ArrayList<>();

			data.forEach((name, cexp) -> {
				if ((allowUnverified || cexp.isVerified()) && cexp.getLatestVersion() != null && cexp.getVersion(cexp.getLatestVersion()) != null)
				{
					cexp.setName(name);

					PlaceholderExpansion ex = plugin.getExpansionManager().getRegisteredExpansion(cexp.getName());

					if (ex != null && ex.isRegistered())
					{
						cexp.setHasExpansion(true);
						if (!ex.getVersion().equals(cexp.getLatestVersion()))
						{
							cexp.setShouldUpdate(true);
						}
					}

					unsorted.add(cexp);
				}
			});

			unsorted.sort(Comparator.comparing(CloudExpansion::getLastUpdate).reversed());

			int count = 0;
			for (CloudExpansion e : unsorted)
			{
				expansions.put(count++, e);
			}

			plugin.getLogger().info(count + " placeholder expansions are available on the cloud.");

			long updates = getCloudUpdateCount();

			if (updates > 0)
			{
				plugin.getLogger().info(updates + " installed expansions have updates available.");
			}
		});
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
