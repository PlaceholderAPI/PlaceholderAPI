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
 * Implementing this interface allows your {@link me.clip.placeholderapi.expansion.PlaceholderExpansion PlaceholderExpansion}
 * to be used as a relational placeholder expansion.
 * 
 * <p>Relational placeholders take two Players as input and are always prefixed with {@code rel_},
 * so {@code %foo_bar%} becomes {@code %rel_foo_bar%}
 */
public interface Relational {

  /**
   * This method is called whenever a placeholder starting with {@code rel_} is called.
   * 
   * @param one The first player used for the placeholder.
   * @param two The second player used for the placeholder.
   * @param identifier The text right after the expansion's name (%expansion_<b>identifier</b>%)
   * @return Parsed String from the expansion.
   */
  String onPlaceholderRequest(Player one, Player two, String identifier);
}
