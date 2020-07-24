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
package me.clip.placeholderapi.expansion;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.PlaceholderHook;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.util.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

public final class ExpansionManager
{

	@NotNull
	private final File                 folder;
	@NotNull
	private final PlaceholderAPIPlugin plugin;

	public ExpansionManager(@NotNull final PlaceholderAPIPlugin plugin)
	{
		this.plugin = plugin;
		this.folder = new File(plugin.getDataFolder(), "expansions");

		if (!this.folder.exists() && !folder.mkdirs())
		{
			plugin.getLogger().log(Level.WARNING, "failed to create expansions folder!");
		}
	}


	@NotNull
	public File getFolder()
	{
		return folder;
	}

	public void initializeExpansions()
	{
		plugin.getLogger().info("Placeholder expansion registration initializing...");

		final Map<String, PlaceholderHook> registered = PlaceholderAPI.getPlaceholders();
		registerAllExpansions();

		if (!registered.isEmpty()) {
			registered.forEach(PlaceholderAPI::registerPlaceholderHook);
		}
	}



	public PlaceholderExpansion getRegisteredExpansion(String name)
	{
		for (Entry<String, PlaceholderHook> hook : PlaceholderAPI.getPlaceholders().entrySet())
		{
			if (hook.getValue() instanceof PlaceholderExpansion)
			{
				if (name.equalsIgnoreCase(hook.getKey()))
				{
					return (PlaceholderExpansion) hook.getValue();
				}
			}
		}

		return null;
	}

	public boolean registerExpansion(@NotNull final PlaceholderExpansion expansion)
	{
		if (expansion.getIdentifier() == null)
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
				for (Entry<String, Object> entries : defaults.entrySet())
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
				plugin.getLogger()
					  .info(
							  "Your server version is not compatible with expansion: " + expansion.getIdentifier()
							  + " version: " + expansion.getVersion());
				return false;
			}
		}

		if (!expansion.canRegister() || !expansion.register())
		{
			return false;
		}

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
			final CloudExpansion cloudExpansion = plugin.getExpansionCloud().getCloudExpansion(expansion.getIdentifier()).orElse(null);

			if (cloudExpansion != null)
			{
				cloudExpansion.setHasExpansion(true);
				if (!cloudExpansion.getLatestVersion().equals(expansion.getVersion()))
				{
					cloudExpansion.setShouldUpdate(true);
				}
			}
		}

		return true;
	}


	@Nullable
	public PlaceholderExpansion registerExpansion(@NotNull final String fileName)
	{
		final List<Class<? extends PlaceholderExpansion>> subs = FileUtil.getClasses(folder, PlaceholderExpansion.class, fileName);
		if (subs.isEmpty())
		{
			return null;
		}

		// only register the first instance found as an expansion jar should only have 1 class
		// extending PlaceholderExpansion
		final PlaceholderExpansion expansion = createInstance(subs.get(0));
		if (expansion != null && registerExpansion(expansion))
		{
			return expansion;
		}

		return null;
	}

	public void registerAllExpansions()
	{
		final List<@NotNull Class<? extends PlaceholderExpansion>> subs = FileUtil.getClasses(folder, PlaceholderExpansion.class);
		if (subs.isEmpty())
		{
			return;
		}

		for (final Class<? extends PlaceholderExpansion> clazz : subs)
		{
			final PlaceholderExpansion expansion = createInstance(clazz);
			if (expansion == null)
			{
				continue;
			}

			try
			{
				registerExpansion(expansion);
			}
			catch (final Exception ex)
			{
				plugin.getLogger().log(Level.WARNING, "Couldn't register " + expansion.getIdentifier() + " expansion", ex);
			}
		}
	}

	@Nullable
	private PlaceholderExpansion createInstance(@NotNull final Class<? extends PlaceholderExpansion> clazz)
	{
		try
		{
			return clazz.getDeclaredConstructor().newInstance();
		}
		catch (final Throwable ex)
		{
			plugin.getLogger().log(Level.SEVERE, "Failed to load placeholder expansion from class: " + clazz.getName(), ex);
		}

		return null;
	}

}
