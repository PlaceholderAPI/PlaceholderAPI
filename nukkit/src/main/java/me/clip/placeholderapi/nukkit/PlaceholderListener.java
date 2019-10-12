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
package me.clip.placeholderapi.nukkit;

import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.plugin.PluginDisableEvent;
import me.clip.placeholderapi.nukkit.expansion.cloud.CloudExpansion;
import me.clip.placeholderapi.nukkit.event.ExpansionUnregisterEvent;
import me.clip.placeholderapi.nukkit.expansion.Cacheable;
import me.clip.placeholderapi.nukkit.expansion.Cleanable;
import me.clip.placeholderapi.nukkit.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.nukkit.expansion.Taskable;

import java.util.Map;
import java.util.Set;

public class PlaceholderListener implements Listener {
    private PlaceholderAPIPlugin plugin;

    public PlaceholderListener(PlaceholderAPIPlugin instance) {
        plugin = instance;
        Server.getInstance().getPluginManager().registerEvents(this, instance);
    }

    @EventHandler
    public void onExpansionUnregister(ExpansionUnregisterEvent e) {
        if (e.getExpansion() instanceof Listener) {
            HandlerList.unregisterAll((Listener) e.getExpansion());
        }

        if (e.getExpansion() instanceof Taskable) {
            ((Taskable) e.getExpansion()).stop();
        }

        if (e.getExpansion() instanceof Cacheable) {
            ((Cacheable) e.getExpansion()).clear();
        }

        if (plugin.getExpansionCloud() != null) {
            CloudExpansion ex = plugin.getExpansionCloud().getCloudExpansion(e.getExpansion().getName());

            if (ex != null) {
                ex.setHasExpansion(false);
                ex.setShouldUpdate(false);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPluginUnload(PluginDisableEvent e) {
        String n = e.getPlugin().getName();

        if (n == null) {
            return;
        }

        if (n.equals(plugin.getName())) {
            return;
        }

        Map<String, PlaceholderHook> hooks = PlaceholderAPI.getPlaceholders();

        for (Map.Entry<String, PlaceholderHook> hook : hooks.entrySet()) {
            PlaceholderHook i = hook.getValue();

            if (i instanceof PlaceholderExpansion) {
                PlaceholderExpansion ex = (PlaceholderExpansion) i;

                if (ex.getRequiredPlugin() == null) {
                    continue;
                }

                if (ex.getRequiredPlugin().equalsIgnoreCase(n)) {
                    if (PlaceholderAPI.unregisterExpansion(ex)) {
                        plugin.getLogger().info("Unregistered placeholder expansion: " + ex.getIdentifier());
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