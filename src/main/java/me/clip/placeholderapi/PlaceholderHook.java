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

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public abstract class PlaceholderHook {
    /**
     * Called when a placeholder value is requested from this hook.
     *
     * @param player {@link OfflinePlayer} to request the placeholder value for, null if not needed for a
     *               player
     * @param params String passed to the hook to determine what value to return
     * @return value for the requested player and params
     */
    public String onRequest(OfflinePlayer player, String params) {
        if (player != null && player.isOnline()) {
            return onPlaceholderRequest((Player) player, params);
        }

        return onPlaceholderRequest(null, params);
    }

    public PlaceholderAPIPlugin getPlaceholderAPI() {
        return PlaceholderAPIPlugin.getInstance();
    }

    /**
     * Called when a placeholder is requested from this hook.
     *
     * @param player {@link Player} to request the placeholder value for, null if not needed for a player
     * @param params String passed to the hook to determine what value to return
     * @return value for the requested player and params
     */
    public String onPlaceholderRequest(Player player, String params) {
        return null;
    }

    public boolean isExpansion() {
        return this instanceof PlaceholderExpansion;
    }

    public boolean isRelational() {
        return this instanceof Relational;
    }
}
