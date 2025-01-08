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
import java.time.temporal.ChronoUnit;
import java.util.StringJoiner;

public class TimeUtil {

  public static String getRemaining(final int seconds, final TimeFormat type) {
    return getRemaining((long) seconds, type);
  }

  public static String getRemaining(final long seconds, final TimeFormat type) {
    switch (type) {
      default:
        return String.valueOf(seconds);

      case SECONDS:
        return String.valueOf(seconds % 60);

      case MINUTES:
        return String.valueOf((seconds / 60) % 60);

      case HOURS:
        return String.valueOf((seconds / 3600) % 24);

      case DAYS:
        return String.valueOf(seconds / 86400);
    }
  }

  /**
   * Format the given value with s, m, h and d (seconds, minutes, hours and days)
   *
   * @param duration {@link Duration} (eg, Duration.of(20, {@link ChronoUnit#SECONDS}) for 20
   *                 seconds)
   * @return formatted time
   */
  public static String getTime(final Duration duration) {
    return getTime(duration.getSeconds());
  }

  public static String getTime(final int seconds) {
    return getTime((long) seconds);
  }

  public static String getTime(long seconds) {
    final StringJoiner joiner = new StringJoiner(" ");

    long minutes = seconds / 60;
    long hours = minutes / 60;
    final long days = hours / 24;

    seconds %= 60;
    minutes %= 60;
    hours %= 24;

    if (days > 0) {
      joiner.add(days + "d");
    }

    if (hours > 0) {
      joiner.add(hours + "h");
    }

    if (minutes > 0) {
      joiner.add(minutes + "m");
    }

    if (seconds > 0) {
      joiner.add(seconds + "s");
    }

    return joiner.toString();
  }
}
