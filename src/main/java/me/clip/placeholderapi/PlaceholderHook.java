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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @deprecated This class will be completely removed in the next release, please use {@link me.clip.placeholderapi.expansion.PlaceholderExpansion}
 */
@Deprecated
@ApiStatus.NonExtendable
@ApiStatus.ScheduledForRemoval(inVersion = "2.11.0")
public abstract class PlaceholderHook
{

  @Nullable
  public String onRequest(@Nullable final OfflinePlayer player, @NotNull final String params)
  {
    if (player != null && player.isOnline())
    {
      return onPlaceholderRequest((Player) player, params);
    }

    return onPlaceholderRequest(null, params);
  }

  /**
   * @deprecated This method will be completely removed, please use {@link me.clip.placeholderapi.expansion.PlaceholderExpansion#onRequest(OfflinePlayer, String)}
   */
  @Nullable
  @Deprecated
  public String onPlaceholderRequest(@Nullable final Player player, @NotNull final String params)
  {
    return null;
  }

}
