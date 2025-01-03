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


package me.clip.placeholderapi.util;

import java.time.Duration;

public class TimeUtil {

  /**
   * Retrieves the remaining time in the specified format.
   *
   * @param seconds the total number of seconds to convert
   * @param type    the desired time format (DAYS, HOURS, MINUTES, SECONDS)
   * @return a string representing the remaining value in the specified format
   */
  public static String getRemaining(final int seconds, final TimeFormat type) {
    return String.valueOf(type.convert(seconds));
  }

  /**
   * Formats the given duration into a human-readable string.
   *
   * @param duration the {@link Duration} to be formatted (e.g., Duration.of(20, ChronoUnit.SECONDS))
   * @return a formatted string representing the duration (e.g., "1d 3h 20m 15s")
   */
  public static String getTime(final Duration duration) {
    return TimeFormat.DAYS.formatTime(duration);
  }

  /**
   * Formats the given number of seconds into a human-readable string.
   *
   * @param seconds the total number of seconds to format
   * @return a formatted string representing the time (e.g., "1d 3h 20m 15s")
   */
  public static String getTime(final int seconds) {
    return TimeFormat.DAYS.formatTime(seconds);
  }

  /**
   * Formats the given number of seconds into a human-readable string.
   *
   * @param seconds the total number of seconds to format
   * @return a formatted string representing the time (e.g., "1d 3h 20m 15s")
   */
  public static String getTime(final long seconds) {
    return TimeFormat.DAYS.formatTime((int) seconds);
  }
}
