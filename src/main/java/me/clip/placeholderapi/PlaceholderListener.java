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
package me.clip.placeholderapi;

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

import java.util.Collection;
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
    PlaceholderExpansion expansion = event.getExpansion();
    if (expansion instanceof Listener) {
      HandlerList.unregisterAll((Listener) expansion);
    }

    if (expansion instanceof Taskable) {
      ((Taskable) expansion).stop();
    }

    if (expansion instanceof Cacheable) {
      ((Cacheable) expansion).clear();
    }

    if (plugin.getExpansionCloud() != null) {

      CloudExpansion ex = plugin.getExpansionCloud()
          .getCloudExpansion(expansion.getName());

      if (ex != null) {
        ex.setHasExpansion(false);
        ex.setShouldUpdate(false);
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onPluginUnload(PluginDisableEvent event) {
    // A plugin name cannot be null.
    String name = event.getPlugin().getName();
    if (name.equals(plugin.getName())) return;

    for (PlaceholderHook hook : PlaceholderAPI.PLACEHOLDERS.values()) {
      if (hook.isExpansion()) {
        PlaceholderExpansion ex = (PlaceholderExpansion) hook;

        if (ex.getRequiredPlugin() == null) continue;

        if (ex.getRequiredPlugin().equalsIgnoreCase(name)) {
          if (PlaceholderAPI.unregisterExpansion(ex)) {
            plugin.getLogger().info("Unregistered placeholder expansion: " + ex.getIdentifier());
          }
        }
      }
    }
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    for (PlaceholderHook hook : PlaceholderAPI.PLACEHOLDERS.values()) {
      if (hook instanceof Cleanable) {
        ((Cleanable) hook).cleanup(event.getPlayer());
      }
    }
  }
}
