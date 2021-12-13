/*
 * This file is part of PlaceholderAPI
 *
 * PlaceholderAPI
 * Copyright (c) 2015 - 2021 PlaceholderAPI Team
 *
 * PlaceholderAPI is free software: you can redistribute it and/or modify
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

package me.clip.placeholderapi.util;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * For the record, I am not sorry.
 */
public final class Format {

  private Format() {}

  @NotNull
  public static Optional<List<String>> tablify(@NotNull final Align align,
      @NotNull final List<List<String>> rows) {
    return findSpacing(rows)
        .map(spacing -> buildFormat(align, spacing))
        .map(format -> rows.stream()
            .map(
                row -> String.format(format, row.toArray()).substring(align == Align.RIGHT ? 2 : 0))
            .collect(toList()));
  }

  @NotNull
  private static String buildFormat(@NotNull final Align align, final int[] spacing) {
    return stream(spacing)
        .mapToObj(space -> "%" + (align == Align.LEFT ? "-" : "") + (space + 2) + "s")
        .collect(joining());
  }

  @NotNull
  private static Optional<int[]> findSpacing(@NotNull final List<List<String>> rows) {
    return rows.stream()
        .map(row -> row.stream().mapToInt(String::length).toArray())
        .reduce((l, r) -> range(0, min(l.length, r.length)).map(i -> max(l[i], r[i])).toArray());
  }

  public enum Align {
    LEFT, RIGHT
  }

}
