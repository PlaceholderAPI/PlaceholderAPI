/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2024 PlaceholderAPI Team
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

package me.clip.placeholderapi.expansion;

import org.bukkit.entity.Player;

/**
 * Classes implementing this interface will have a {@link #cleanup(Player) cleanup void} that is
 * called by PlaceholderAPI whenever a Player leaves the server.
 * 
 * <p>This can be useful for cases where you keep data of the player in a cache or similar
 * and want to free up space whenever they leave.
 *
 * @author Ryan McCarthy
 */
public interface Cleanable {

  /**
   * Called when a player leaves the server
   *
   * @param p (@link Player} who left the server
   */
  void cleanup(Player p);
}
