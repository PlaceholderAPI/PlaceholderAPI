/*
 *
 * PlaceholderAPI
 * Copyright (C) 2018 Ryan McCarthy
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

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class ExpansionManager {

	private PlaceholderAPIPlugin plugin;

	private final Map<String, PlaceholderExpansion> cache = new HashMap<>();

	public ExpansionManager(PlaceholderAPIPlugin instance) {
		plugin = instance;
	}

	public void clean() {
		cache.clear();
	}

	public PlaceholderExpansion getCachedExpansion(String plugin) {
		return cache.getOrDefault(plugin, null);
	}

	public boolean removeCachedExpansion(String identifier) {
		return cache.remove(identifier) != null;
	}

	public PlaceholderExpansion getRegisteredExpansion(String name) {
		for (Entry<String, PlaceholderHook> hook : PlaceholderAPI.getPlaceholders().entrySet()) {
			if (hook.getValue() instanceof PlaceholderExpansion) {
				if (name.equalsIgnoreCase(hook.getKey())) {
					return (PlaceholderExpansion) hook.getValue();
				}
			}
		}
		return null;
	}

	public boolean registerExpansion(PlaceholderExpansion c) {

		if (c == null || c.getIdentifier() == null) {
			return false;
		}

		if (c instanceof Configurable) {

			Map<String, Object> defaults = ((Configurable) c).getDefaults();
			String pre = "expansions." + c.getIdentifier() + ".";
			FileConfiguration cfg = plugin.getConfig();

			boolean save = false;

			for (Entry<String, Object> entries : defaults.entrySet()) {
				if (entries.getKey() == null || entries.getKey().isEmpty()) {
					continue;
				}

				if (entries.getValue() == null) {
					if (cfg.contains(pre + entries.getKey())) {
						save = true;
						cfg.set(pre + entries.getKey(), null);
					}
				} else {
					if (!cfg.contains(pre + entries.getKey())) {
						save = true;
						cfg.set(pre + entries.getKey(), entries.getValue());
					}
				}
			}

			if (save) {
				plugin.saveConfig();
				plugin.reloadConfig();
			}
		}

		if (c instanceof VersionSpecific) {
			VersionSpecific nms = (VersionSpecific) c;
			if (!nms.isCompatibleWith(PlaceholderAPIPlugin.getServerVersion())) {
				plugin.getLogger().info("Your server version is not compatible with expansion: " + c.getIdentifier()
						+ " version: " + c.getVersion());
				return false;
			}
		}

		if (!c.canRegister()) {
			if (c.getPlugin() != null) {
				cache.put(c.getPlugin().toLowerCase(), c);
			}
			return false;
		}

		if (!c.register()) {
			if (c.getPlugin() != null) {
				cache.put(c.getPlugin().toLowerCase(), c);
			}
			return false;
		}

		if (c instanceof Listener) {
			Listener l = (Listener) c;
			Bukkit.getPluginManager().registerEvents(l, plugin);
		}

		plugin.getLogger().info("Successfully registered expansion: " + c.getIdentifier());

		if (c instanceof Taskable) {
			((Taskable) c).start();
		}
		return true;
	}

	public void registerAllExpansions() {

		if (plugin == null) {
			return;
		}

		List<Class<?>> subs = FileUtil.getClasses("expansions", PlaceholderExpansion.class);

		if (subs == null || subs.isEmpty()) {
			return;
		}

		for (Class<?> klass : subs) {

			if (klass == null) {
				continue;
			}

			try {

				PlaceholderExpansion ex = null;

				Constructor<?>[] c = klass.getConstructors();

				if (c.length == 0) {
					ex = (PlaceholderExpansion) klass.newInstance();
				} else {
					for (Constructor<?> con : c) {
						if (con.getParameterTypes().length == 0) {
							ex = (PlaceholderExpansion) klass.newInstance();
							break;
						}
					}
				}

				if (ex == null) {
					continue;
				}

				if (registerExpansion(ex)) {
					if (plugin.getExpansionCloud() != null) {
						CloudExpansion ce = plugin.getExpansionCloud().getCloudExpansion(ex.getIdentifier());
						if (ce != null) {
							ce.setHasExpansion(true);
							if (!ce.getVersion().equals(ex.getVersion())) {
								ce.setShouldUpdate(true);
							}
						}
					}
				}

			} catch (Throwable t) {
				plugin.getLogger().severe("Failed to load placeholder expansion from class: " + klass.getName());
				plugin.getLogger().severe(t.getMessage());
			}
		}
	}
}
