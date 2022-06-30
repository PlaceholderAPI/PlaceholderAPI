/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2021 PlaceholderAPI Team
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

@Deprecated
public abstract class PlaceholderHook {

  /**
   * Please see {@link me.clip.placeholderapi.expansion.PlaceholderExpansion#parsePlaceholders(OfflinePlayer, String) PlaceholderExpansion#parsePlaceholder(OfflinePlayer, String)}
   * for a full description of what this method is used for.
   * 
   * @param player Possibly-null OfflinePlayer instance to use.
   * @param params String after {@code %<expansion>_} and before the second percent symbol.
   * @return Possibly-null String depending on the Expansions returned value.
   * @deprecated Planned for removal in 2.14.0
   *             <br>Please use {@link me.clip.placeholderapi.expansion.PlaceholderExpansion#parsePlaceholders(OfflinePlayer, String) parsePlaceholders(OfflinePlayer, String)}
   *             instead
   */
  @Nullable
  @Deprecated
  public String onRequest(final OfflinePlayer player, @NotNull final String params) {
    if (player != null && player.isOnline()) {
      return onPlaceholderRequest((Player) player, params);
    }

    return onPlaceholderRequest(null, params);
  }

  /**
   * Please see {@link me.clip.placeholderapi.expansion.PlaceholderExpansion#parsePlaceholders(Player, String) PlaceholderExpansion#parsePlaceholder(Player, String)}
   * for a full description of what this method is used for.
   *
   * @param player Possibly-null Player instance to use.
   * @param params String after {@code %<expansion>_} and before the second percent symbol.
   * @return Possibly-null String depending on the Expansions returned value.
   * 
   * @deprecated Planned for removal in 2.14.0
   *             <br>Please use {@link me.clip.placeholderapi.expansion.PlaceholderExpansion#parsePlaceholders(Player, String) parsePlaceholders(Player, String)}
   *             instead
   */
  @Nullable
  @Deprecated
  public String onPlaceholderRequest(final Player player, @NotNull final String params) {
    return null;
  }
}
