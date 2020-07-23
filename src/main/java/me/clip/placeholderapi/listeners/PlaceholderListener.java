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
package me.clip.placeholderapi.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.PlaceholderHook;
import me.clip.placeholderapi.events.ExpansionUnregisterEvent;
import me.clip.placeholderapi.expansion.Cacheable;
import me.clip.placeholderapi.expansion.Cleanable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Taskable;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class PlaceholderListener implements Listener {

    private final PlaceholderAPIPlugin plugin;

    public PlaceholderListener(PlaceholderAPIPlugin instance) {
        plugin = instance;
        Bukkit.getPluginManager().registerEvents(this, instance);
    }

    @EventHandler
    public void onExpansionUnregister(ExpansionUnregisterEvent event) {
        if (event.getExpansion() instanceof Listener) {
            HandlerList.unregisterAll((Listener) event.getExpansion());
        }

        if (event.getExpansion() instanceof Taskable) {
            ((Taskable) event.getExpansion()).stop();
        }

        if (event.getExpansion() instanceof Cacheable) {
            ((Cacheable) event.getExpansion()).clear();
        }

        if (plugin.getPlaceholderAPIConfig().isCloudEnabled()) {
            CloudExpansion ex = plugin.getExpansionCloud().getCloudExpansion(event.getExpansion().getName()).orElse(null);
            if (ex != null) {
                ex.setHasExpansion(false);
                ex.setShouldUpdate(false);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPluginUnload(PluginDisableEvent e) {
        String n = e.getPlugin().getName();

        if (n.equals(plugin.getName())) {
            return;
        }

        Map<String, PlaceholderHook> hooks = PlaceholderAPI.getPlaceholders();

        for (Entry<String, PlaceholderHook> entry : hooks.entrySet()) {
            PlaceholderHook hook = entry.getValue();

            if (hook instanceof PlaceholderExpansion) {
                PlaceholderExpansion expansion = (PlaceholderExpansion) hook;

                if (expansion.getRequiredPlugin() == null) {
                    continue;
                }

                if (expansion.getRequiredPlugin().equalsIgnoreCase(n)) {
                    if (PlaceholderAPI.unregisterExpansion(expansion)) {
                        plugin.getLogger().info("Unregistered placeholder expansion: " + expansion.getIdentifier());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Set<PlaceholderExpansion> expansions = PlaceholderAPI.getExpansions();

        if (expansions.isEmpty()) {
            return;
        }

        for (PlaceholderExpansion ex : expansions) {
            if (ex instanceof Cleanable) {
                ((Cleanable) ex).cleanup(e.getPlayer());
            }
        }
    }
}
