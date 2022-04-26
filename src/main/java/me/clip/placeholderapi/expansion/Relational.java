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

package me.clip.placeholderapi.expansion;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This interface should be used when your {@link me.clip.placeholderapi.expansion.PlaceholderExpansion PlaceholderExpansion}
 * should support relational placeholders.
 * 
 * <p>The difference between relational placeholders and normal ones is, that relational ones start
 * with a {@code rel_} prefix and have two Players provided, instead of one.
 * <br>The main purpose is to return a String based on the "relationship" between the two players
 * (i.e. if both are within the same world).
 */
public interface Relational {

  /**
   * This method will be called whenever a valid placeholder in the format {@code %rel_<expansion>_<identifier>%}
   * is found.
   * 
   * @param one The first player to use for comparison.
   * @param two The second player to use for comparison
   * @param identifier String right after {@code %rel_<expansion>_} and before the second percent symbol.
   * @return Possibly-null String, depending on what the expansion returns.
   */
  @Nullable
  String onPlaceholderRequest(Player one, Player two, @NotNull String identifier);
}
