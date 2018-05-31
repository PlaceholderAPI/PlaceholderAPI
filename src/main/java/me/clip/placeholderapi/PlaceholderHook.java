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
package me.clip.placeholderapi;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public abstract class PlaceholderHook {

  /**
   * called when a placeholder is requested from this hook
   * @param p {@link OfflinePlayer} to request the placeholder value for, null if not needed for a player
   * @param params String passed to the hook to determine what value to return
   * @return value for the requested player and params
   */
	public Object onPlaceholderRequest(OfflinePlayer p, String params) {
		if (p != null && p.isOnline()) {
			return onPlaceholderRequest((Player) p, params);
		}
		return onPlaceholderRequest(null, params);
	}

  /**
   * @deprecated  As of versions greater than 2.8.7, use {@link #onPlaceholderRequest(OfflinePlayer p, String params)}
   */
  @Deprecated
	public String onPlaceholderRequest(Player p, String params) {
		return null;
	}
}
