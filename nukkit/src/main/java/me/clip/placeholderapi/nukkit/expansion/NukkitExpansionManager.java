/*
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
 */
package me.clip.placeholderapi.nukkit.expansion;

import cn.nukkit.Server;
import cn.nukkit.event.Listener;
import cn.nukkit.utils.Config;
import me.clip.placeholderapi.common.PlaceholderAPI;
import me.clip.placeholderapi.common.PlaceholderHook;
import me.clip.placeholderapi.common.expansion.Configurable;
import me.clip.placeholderapi.common.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.common.expansion.PlatformSpecific;
import me.clip.placeholderapi.common.expansion.Taskable;
import me.clip.placeholderapi.common.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.common.expansion.cloud.ExpansionCloudManager;
import me.clip.placeholderapi.common.util.FileUtil;
import me.clip.placeholderapi.nukkit.PlaceholderAPINukkitPlugin;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;

public final class NukkitExpansionManager {
    private PlaceholderAPINukkitPlugin plugin;

    public NukkitExpansionManager(PlaceholderAPINukkitPlugin plugin) {
        this.plugin = plugin;
        File f = new File(plugin.getDataFolder(), "expansions");
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    public PlaceholderExpansion getRegisteredExpansion(String name) {
        for (Map.Entry<String, PlaceholderHook> hook : PlaceholderAPI.getPlaceholders().entrySet()) {
            if (hook.getValue() instanceof PlaceholderExpansion) {
                if (name.equalsIgnoreCase(hook.getKey())) {
                    return (PlaceholderExpansion) hook.getValue();
                }
            }
        }

        return null;
    }

    public boolean registerExpansion(PlaceholderExpansion expansion) {
        if (expansion == null || expansion.getIdentifier() == null) {
            return false;
        }

        if (expansion instanceof Configurable) {
            Map<String, Object> defaults = ((Configurable) expansion).getDefaults();
            String pre = "expansions." + expansion.getIdentifier() + ".";
            Config cfg = plugin.getConfig();
            boolean save = false;
            if (defaults != null) {
                for (Map.Entry<String, Object> entries : defaults.entrySet()) {
                    if (entries.getKey() == null || entries.getKey().isEmpty()) {
                        continue;
                    }

                    if (entries.getValue() == null) {
                        if (cfg.exists(pre + entries.getKey())) {
                            save = true;
                            cfg.set(pre + entries.getKey(), null);
                        }
                    } else {
                        if (!cfg.exists(pre + entries.getKey())) {
                            save = true;
                            cfg.set(pre + entries.getKey(), entries.getValue());
                        }
                    }
                }
            }

            if (save) {
                plugin.saveConfig();
                plugin.reloadConfig();
            }
        }

        if (expansion instanceof PlatformSpecific) {
            PlatformSpecific platform = (PlatformSpecific) expansion;
            if (!platform.isCompatibleWith(PlaceholderAPINukkitPlugin.getClassInstance().getPlatform())) {
                plugin.getLogger().info("Your server type is not compatible with expansion: " + expansion.getIdentifier() + " version: " + expansion.getVersion());
                return false;
            }
        }

        if (!expansion.canRegister()) {
            return false;
        }

        if (!expansion.register()) {
            return false;
        }

        if (expansion instanceof Listener) {
            Listener l = (Listener) expansion;
            Server.getInstance().getPluginManager().registerEvents(l, plugin);
        }

        plugin.getLogger().info("Successfully registered expansion: " + expansion.getIdentifier());

        if (expansion instanceof Taskable) {
            ((Taskable) expansion).start();
        }

        if (plugin.getExpansionCloud() != null) {
            CloudExpansion ce = ExpansionCloudManager.getCloudExpansion(expansion.getIdentifier());
            if (ce != null) {
                ce.setHasExpansion(true);
                if (!ce.getLatestVersion().equals(expansion.getVersion())) {
                    ce.setShouldUpdate(true);
                }
            }
        }

        return true;
    }

    public PlaceholderExpansion registerExpansion(String fileName) {
        List<Class<?>> subs = FileUtil.getClasses("expansions", fileName, PlaceholderExpansion.class);
        if (subs == null || subs.isEmpty()) {
            return null;
        }

        PlaceholderExpansion ex = createInstance(subs.get(0));
        if (registerExpansion(ex)) {
            return ex;
        }

        return null;
    }

    public void registerAllExpansions() {
        if (plugin == null) {
            return;
        }

        List<Class<?>> subs = FileUtil.getClasses("expansions", null, PlaceholderExpansion.class);

        if (subs == null || subs.isEmpty()) {
            return;
        }

        for (Class<?> klass : subs) {
            PlaceholderExpansion ex = createInstance(klass);
            if (ex != null) {
                registerExpansion(ex);
            }
        }
    }

    private PlaceholderExpansion createInstance(Class<?> klass) {
        if (klass == null) {
            return null;
        }

        PlaceholderExpansion ex = null;

        if (!PlaceholderExpansion.class.isAssignableFrom(klass)) {
            return null;
        }

        try {
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
        } catch (Throwable t) {
            plugin.getLogger().error("Failed to init placeholder expansion from class: " + klass.getName());
            plugin.getLogger().error(t.getMessage());
        }

        return ex;
    }
}