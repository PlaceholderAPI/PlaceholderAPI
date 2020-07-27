/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2020 PlaceholderAPI Team
 *
 * PlaceholderAPI free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlaceholderAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package me.clip.placeholderapi;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @deprecated This class will be completely removed in the next release, please use {@link me.clip.placeholderapi.expansion.PlaceholderExpansion}
 */
@Deprecated
public abstract class PlaceholderHook
{

	/**
	 * called when a placeholder value is requested from this hook
	 *
	 * @param player {@link OfflinePlayer} to request the placeholder value for, null if not needed for a
	 *               player
	 * @param params String passed to the hook to determine what value to return
	 * @return value for the requested player and params
	 * @deprecated This method will be completely removed, please use {@link me.clip.placeholderapi.expansion.PlaceholderExpansion#onRequest(OfflinePlayer, String)}
	 */
	@Nullable
	@Deprecated
	public String onRequest(@Nullable final OfflinePlayer player, @NotNull final String params)
	{
		if (player != null && player.isOnline())
		{
			return onPlaceholderRequest((Player) player, params);
		}

		return onPlaceholderRequest(null, params);
	}

	/**
	 * called when a placeholder is requested from this hook
	 *
	 * @param player {@link Player} to request the placeholder value for, null if not needed for a player
	 * @param params String passed to the hook to determine what value to return
	 * @return value for the requested player and params
	 *
	 * @deprecated This method will be completely removed, please use {@link me.clip.placeholderapi.expansion.PlaceholderExpansion#onRequest(OfflinePlayer, String)}
	 */
	@Nullable
	@Deprecated
	public String onPlaceholderRequest(@Nullable final Player player, @NotNull final String params)
	{
		return null;
	}

}
