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

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

import java.util.Map;

public class ServerLoadEventListener implements Listener {

    private final PlaceholderAPIPlugin plugin;

    public ServerLoadEventListener(PlaceholderAPIPlugin instance) {
        plugin = instance;
        Bukkit.getPluginManager().registerEvents(this, instance);
    }

    /**
     * This method will be called when the server is first loaded
     *
     * The goal of the method is to register all the expansions as soon as possible
     * especially before players can join
     *
     * This will ensure no issues with expanions and hooks.
     * @param e the server load event
     */
    @EventHandler
    public void onServerLoad(ServerLoadEvent e) {
        plugin.getLogger().info("Placeholder expansion registration initializing...");
        final Map<String, PlaceholderHook> alreadyRegistered = PlaceholderAPI.getPlaceholders();
        plugin.getExpansionManager().registerAllExpansions();

        if (alreadyRegistered != null && !alreadyRegistered.isEmpty()) {
            alreadyRegistered.forEach(PlaceholderAPI::registerPlaceholderHook);
        }
    }
}
