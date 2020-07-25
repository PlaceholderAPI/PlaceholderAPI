package me.clip.placeholderapi.expansion.manager;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.events.ExpansionRegisterEvent;
import me.clip.placeholderapi.events.ExpansionUnregisterEvent;
import me.clip.placeholderapi.expansion.Cacheable;
import me.clip.placeholderapi.expansion.Cleanable;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Taskable;
import me.clip.placeholderapi.expansion.VersionSpecific;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.util.FileUtil;
import me.clip.placeholderapi.util.Msg;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.logging.Level;

public final class LocalExpansionManager implements Listener
{

	@NotNull
	private static final String EXPANSIONS_FOLDER_NAME = "expansions";


	@NotNull
	private final File                 folder;
	@NotNull
	private final PlaceholderAPIPlugin plugin;

	@NotNull
	private final Map<String, PlaceholderExpansion> expansions = new HashMap<>();


	public LocalExpansionManager(@NotNull final PlaceholderAPIPlugin plugin)
	{
		this.plugin = plugin;
		this.folder = new File(plugin.getDataFolder(), EXPANSIONS_FOLDER_NAME);

		if (!this.folder.exists() && !folder.mkdirs())
		{
			plugin.getLogger().log(Level.WARNING, "failed to create expansions folder!");
		}
	}

	public void load(@NotNull final CommandSender sender)
	{
		registerAll(sender);
	}

	public void kill()
	{
		unregisterAll();
	}


	@NotNull
	public File getExpansionsFolder()
	{
		return folder;
	}

	public int getExpansionsCount()
	{
		return expansions.size();
	}


	@NotNull
	@Unmodifiable
	public Collection<String> getIdentifiers()
	{
		return ImmutableSet.copyOf(expansions.keySet());
	}

	@NotNull
	@Unmodifiable
	public Collection<PlaceholderExpansion> getExpansions()
	{
		return ImmutableSet.copyOf(expansions.values());
	}


	@Nullable
	public PlaceholderExpansion getExpansion(@NotNull final String identifier)
	{
		return expansions.get(identifier.toLowerCase());
	}


	@NotNull
	public Optional<PlaceholderExpansion> findExpansionByName(@NotNull final String name)
	{
		return expansions.values().stream().filter(expansion -> name.equalsIgnoreCase(expansion.getName())).findFirst();
	}

	@NotNull
	public Optional<PlaceholderExpansion> findExpansionByIdentifier(@NotNull final String identifier)
	{
		return Optional.ofNullable(getExpansion(identifier));
	}


	/**
	 * Do not call this method yourself, use {@link PlaceholderExpansion#register()}
	 */
	public boolean register(@NotNull final PlaceholderExpansion expansion)
	{
		final String identifier = expansion.getIdentifier();
		if (identifier == null)
		{
			return false;
		}

		if (expansion instanceof Configurable)
		{
			Map<String, Object> defaults = ((Configurable) expansion).getDefaults();
			String              pre      = "expansions." + expansion.getIdentifier() + ".";
			FileConfiguration   cfg      = plugin.getConfig();
			boolean             save     = false;

			if (defaults != null)
			{
				for (Map.Entry<String, Object> entries : defaults.entrySet())
				{
					if (entries.getKey() == null || entries.getKey().isEmpty())
					{
						continue;
					}

					if (entries.getValue() == null)
					{
						if (cfg.contains(pre + entries.getKey()))
						{
							save = true;
							cfg.set(pre + entries.getKey(), null);
						}
					}
					else
					{
						if (!cfg.contains(pre + entries.getKey()))
						{
							save = true;
							cfg.set(pre + entries.getKey(), entries.getValue());
						}
					}
				}
			}

			if (save)
			{
				plugin.saveConfig();
				plugin.reloadConfig();
			}
		}

		if (expansion instanceof VersionSpecific)
		{
			VersionSpecific nms = (VersionSpecific) expansion;
			if (!nms.isCompatibleWith(PlaceholderAPIPlugin.getServerVersion()))
			{
				plugin.getLogger().info("Your server version is not compatible with expansion: " + expansion.getIdentifier() + " version: " + expansion.getVersion());
				return false;
			}
		}

		final PlaceholderExpansion removed = expansions.get(expansion.getIdentifier());
		if (removed != null && !unregister(removed))
		{
			return false;
		}

		final ExpansionRegisterEvent event = new ExpansionRegisterEvent(expansion);
		Bukkit.getPluginManager().callEvent(event);

		if (event.isCancelled())
		{
			return false;
		}

		expansions.put(expansion.getIdentifier(), expansion);

		if (expansion instanceof Listener)
		{
			Bukkit.getPluginManager().registerEvents(((Listener) expansion), plugin);
		}

		plugin.getLogger().info("Successfully registered expansion: " + expansion.getIdentifier());

		if (expansion instanceof Taskable)
		{
			((Taskable) expansion).start();
		}

		if (plugin.getPlaceholderAPIConfig().isCloudEnabled())
		{
			final Optional<CloudExpansion> cloudExpansion = plugin.getCloudExpansionManager().findCloudExpansionByName(identifier);
			if (cloudExpansion.isPresent())
			{
				cloudExpansion.get().setHasExpansion(true);
				cloudExpansion.get().setShouldUpdate(!cloudExpansion.get().getLatestVersion().equals(expansion.getVersion()));
			}
		}

		return true;
	}

	public Optional<PlaceholderExpansion> register(@NotNull final Class<? extends PlaceholderExpansion> clazz)
	{
		final PlaceholderExpansion expansion = createExpansionInstance(clazz);
		if (expansion == null || !expansion.register())
		{
			return Optional.empty();
		}

		return Optional.of(expansion);
	}

	public boolean unregister(@NotNull final PlaceholderExpansion expansion)
	{
		if (expansions.remove(expansion.getIdentifier()) == null)
		{
			return false;
		}


		Bukkit.getPluginManager().callEvent(new ExpansionUnregisterEvent(expansion));


		if (expansion instanceof Listener)
		{
			HandlerList.unregisterAll((Listener) expansion);
		}

		if (expansion instanceof Taskable)
		{
			((Taskable) expansion).stop();
		}

		if (expansion instanceof Cacheable)
		{
			((Cacheable) expansion).clear();
		}

		if (plugin.getPlaceholderAPIConfig().isCloudEnabled())
		{
			plugin.getCloudExpansionManager().findCloudExpansionByName(expansion.getName()).ifPresent(cloud -> {
				cloud.setHasExpansion(false);
				cloud.setShouldUpdate(false);
			});
		}

		return true;
	}


	private void registerAll(@NotNull final CommandSender sender)
	{
		plugin.getLogger().info("Placeholder expansion registration initializing...");

		findExpansionsOnDisk().whenCompleteAsync((classes, exception) -> {
			if (exception != null)
			{
				plugin.getLogger().log(Level.SEVERE, "failed to load class files of expansions", exception);
				return;
			}

			Bukkit.getScheduler().runTask(plugin, () -> {
				final long registered = classes.stream().map(this::register).filter(Optional::isPresent).count();
				Msg.msg(sender,
						registered == 0 ? "&6No expansions were registered!" : registered + "&a placeholder hooks successfully registered!");
			});
		});
	}

	private void unregisterAll()
	{
		for (final PlaceholderExpansion expansion : Sets.newHashSet(expansions.values()))
		{
			if (expansion.persist())
			{
				continue;
			}

			unregister(expansion);
		}
	}


	@NotNull
	public CompletableFuture<List<Class<? extends PlaceholderExpansion>>> findExpansionsOnDisk()
	{
		return CompletableFuture.supplyAsync(() -> {
			try
			{
				return FileUtil.getClasses(getExpansionsFolder(), PlaceholderExpansion.class);
			}
			catch (final IOException | ClassNotFoundException ex)
			{
				throw new CompletionException(ex);
			}
		});
	}

	@NotNull
	public CompletableFuture<List<Class<? extends PlaceholderExpansion>>> findExpansionsInFile(@NotNull final File file)
	{
		return CompletableFuture.supplyAsync(() -> {
			try
			{
				final List<@NotNull Class<? extends PlaceholderExpansion>> classes = FileUtil.getClasses(getExpansionsFolder(), PlaceholderExpansion.class, file.getName());
				if (classes.size() > 1)
				{
					throw new IllegalStateException("multiple expansion classes in one file!");
				}

				return classes;
			}
			catch (final Exception ex)
			{
				throw new CompletionException(ex);
			}
		});
	}


	@Nullable
	public PlaceholderExpansion createExpansionInstance(@NotNull final Class<? extends PlaceholderExpansion> clazz)
	{
		try
		{
			return clazz.getDeclaredConstructor().newInstance();
		}
		catch (final Throwable ex)
		{
			plugin.getLogger().log(Level.SEVERE, "Failed to load placeholder expansion from class: " + clazz.getName(), ex);
			return null;
		}
	}


	@EventHandler
	public void onQuit(@NotNull final PlayerQuitEvent event)
	{
		for (final PlaceholderExpansion expansion : getExpansions())
		{
			if (!(expansion instanceof Cleanable))
			{
				continue;
			}

			((Cleanable) expansion).cleanup(event.getPlayer());
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPluginDisable(@NotNull final PluginDisableEvent event)
	{
		final String name = event.getPlugin().getName();
		if (name.equals(plugin.getName()))
		{
			return;
		}

		for (final PlaceholderExpansion expansion : getExpansions())
		{
			if (!name.equalsIgnoreCase(expansion.getRequiredPlugin()))
			{
				continue;
			}

			unregister(expansion);
			plugin.getLogger().info("Unregistered placeholder expansion: " + expansion.getName());
		}
	}

}
