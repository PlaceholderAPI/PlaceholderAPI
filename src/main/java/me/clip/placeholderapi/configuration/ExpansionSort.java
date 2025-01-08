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

package me.clip.placeholderapi.configuration;

import java.util.Comparator;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import org.jetbrains.annotations.NotNull;

public enum ExpansionSort implements Comparator<CloudExpansion> {

  NAME(Comparator.comparing(CloudExpansion::getName)),
  AUTHOR(Comparator.comparing(CloudExpansion::getAuthor)),
  LATEST(Comparator.comparing(CloudExpansion::getLastUpdate).reversed());


  @NotNull
  private final Comparator<CloudExpansion> comparator;

  ExpansionSort(@NotNull final Comparator<CloudExpansion> comparator) {
    this.comparator = comparator;
  }


  @Override
  public final int compare(final CloudExpansion expansion1, final CloudExpansion expansion2) {
    return comparator.compare(expansion1, expansion2);
  }

}
